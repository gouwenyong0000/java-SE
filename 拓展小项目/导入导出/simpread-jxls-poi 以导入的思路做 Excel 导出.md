\> 本文由 \[简悦 SimpRead\](http://ksria.com/simpread/) 转码， 原文地址 \[www.jianshu.com\](https://www.jianshu.com/p/674c163064bd)

一、分析

        原生的 poi 导出，最麻烦的就是导出 excel 样式的设置，十分麻烦。于是有一种想法，就是以导入的思路做导出，就像我们知道的那样，导入 excel 进内存也必然对象化了，那么我们只要替换对应的业务数据不就可以了吗？

        正如所想的那样，可以实现的，只是有人做到了，也就是有现成的工具类可以使用，说道本文的主角，那就是 jxls-poi. 看名字也知道必然依赖 poi，没错必须依赖 poi，只是 poi 的很多版本存在兼容的问题，略微有点坑。

        _jxls_是一个简单的、轻量级的 excel 导出库, 使用特定的标记在 excel 模板文件中来定义输出格式和布局，底层应该是利用反射做的映射，这里本文不针对 jxls 的具体实现原理做探究，本文重点在于记录一下 jxls 的使用。(有空再做研究，有空的话或者需要的话)。

        jxls-poi 的使用相对来说，是比较简单的，但是其功能是比较强大的。最关键的是对 jxls 的标记语言的熟悉与使用，当然不熟悉也没关系，因为我也不是很熟悉的。本文记录的使用案例是基本对导出 excel 来说是通用的，多 sheet 页，当然还没用多行遍历集合数据。重在在使用中进行摸索，不采坑不进步~

二、导出模板

批注的使用
```properties
// 指定模板的范围
jx:area(lastCell="L56") 
//items 对导出数据进行遍历，var 遍历出的每一项数据，lastCell 指定模板的范围，multisheet 多 sheet 的对应 sheet 名称集合
jx:each(items="aiFormulaResultData", var="data", lastCell="L56" multisheet="aiFormulaResultDataSheetNames")
```

![](http://upload-images.jianshu.io/upload_images/18372048-f16d921272942166.jpg) 导出模板. xlsx ![](http://upload-images.jianshu.io/upload_images/18372048-a1373a24e715aa1b.jpg) 指定模板的范围

三、业务代码
```java
@RequestMapping("/downloadExcel.action")
public void downloadExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {

    List data =dataService.getData();
    List dataSheetNames =new ArrayList<>(data .size());
    //sheetName
    String templateSheetName1 = ConstVar.SHEET\_DATA1;
    String templateSheetName2 = ConstVar.SHEET\_DATA2;
    for (int i =0; i < data.size(); i++) {
        dataSheetNames.add((String)data.get(i).get("code"));
    }
    Map bindDataMap =new HashMap<>();
    bindDataMap.put("aiChemicalDataSheetNames", aiChemicalDataSheetNames);
    bindDataMap.put("aiChemicalDataList", chemicalData);
    // 获取获取模板  国际化
    String templateFileName = RequestContextUtils.getLocale(request).equals(Locale.SIMPLIFIED\_CHINESE) ? ConstVar.AI_EXCEL_CHINESE_NAME : ConstVar.AI_EXCEL_ENGLISH_NAME;

    File tempTargetFile =new File(CommonUtil.getGuid() +".xlsx");
    InputStream inputStreamClassPath =null;
    InputStream inputStream =null;

try {

    inputStreamClassPath =new ClassPathResource("downloadTemplateExcel/" + templateFileName).getInputStream();

    // 读取模板 - 写入数据 - 删除模板 sheet

    JxlsExportTemplateExcelUtils.exportExcelAndDeleteTemplateSheet(inputStreamClassPath, tempTargetFile, bindDataMap, templateSheetName1, templateSheetName2);

    inputStream =new FileInputStream(tempTargetFile);

    // 设置 response 头信息

    response.reset();
    String title = CommonUtil.encodingFileName(I18nUtils.getMessage("chemical.pdfTitleName"));
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Length", String.valueOf(inputStream.available()));
    response.setHeader("Content-Disposition","attachment;file);
    OutputStream out = response.getOutputStream();

    // 创建缓冲区

    FileCopyUtils.copy(new BufferedInputStream(inputStream),new BufferedOutputStream(out));
}catch (Exception e) {
    logger.error("下载 EXCEL 出现异常" + CommonUtil.getExceptionDetail(e));
}finally {

    if (inputStream !=null) {
        inputStream.close();
    }

    if (inputStreamClassPath !=null) {

        inputStreamClassPath.close();

    }

    if (tempTargetFile !=null && tempTargetFile.exists()) {

        tempTargetFile.delete();

    }

}

}
```
四、工具类`JxlsExportTemplateExcelUtils`

```java
// 对外提供
public static void exportExcelAndDeleteTemplateSheet(InputStream templateFileInputStreamn, File tempTargetFile, Map bindDataMap, String... templateSheetNames)throws FileNotFoundException, IOException {

exportExcel(templateFileInputStreamn, tempTargetFile, bindDataMap, templateSheetNames);

}

// 多种重载

private static void exportExcel(InputStream is, File out, Map model, String... sheetNames)throws IOException {

    OutputStream os =new FileOutputStream(out);

Context context = PoiTransformer.createInitialContext();

	if (model !=null) {

	for (Map.Entry entry : model.entrySet()) {
		context.putVar(entry.getKey(), entry.getValue());
	}

}

JxlsHelper jxlsHelper = JxlsHelper.getInstance();

// 必须要这个，否者表格函数统计会错乱

   jxlsHelper.getInstance().setUseFastFormulaProcessor(false).processTemplate(context, jxlsHelper.createTransformer(is, os));

// 删除对应的模板 sheet 页

    deleteSheet(out, sheetNames);

}

// 删除对应的模板 sheet 页

public static void deleteSheet(File file, String... sheetNames) {

try {

FileInputStream fis =new FileInputStream(file);

XSSFWorkbook wb =new XSSFWorkbook(fis);

fileWrite(file, wb);

// 删除 Sheet

        for (String sheetName : sheetNames) {

wb.removeSheetAt(wb.getSheetIndex(sheetName));

}

fileWrite(file, wb);

fis.close();

}catch (Exception e) {

//e.printStackTrace();

    }

}
```
五、maven 依赖
```xml
<!-- 项目中已使用 poi，此处排除 -->
<dependency>
    <groupId>org.jxls</groupId>
    <artifactId>jxls-poi</artifactId>
    <version>1.1.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--POI-->

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
```
六、总结

整体来说，是相对简单的，思路与想法很重要，只要你能想到的，一般就已经有人做到了，而且做得还不错。但是需要在实践中发现问题，解决问题。