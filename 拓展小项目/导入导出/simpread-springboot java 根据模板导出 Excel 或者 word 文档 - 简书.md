\> 本文由 \[简悦 SimpRead\](http://ksria.com/simpread/) 转码， 原文地址 \[www.jianshu.com\](https://www.jianshu.com/p/1cf5c64b2f35)

在使用 react +antd 的时候，展示一个用户信息的基本列表，做了个导出 execl 和 word 的测试。为了方便记忆 ，所以记录下来。  
一、导出 excel  
两种方式去实现，第一种利用 poi 直接导出，第二种是利用 jxls 根据模板导出。

第一种方式 poi：  
1、首先添加依赖 poi

```
<!--导出导入 excel-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.12</version>
        </dependency>
```

2、看看前端界面按钮等等

![](http://upload-images.jianshu.io/upload_images/16959478-a6be92ef08a63e91.png) 主界面. png

```
//导出Excel
      exportExcelFile = () => {
          window.location.href=\`${base}/t-stu/downloadExcel\`;
      }
 ...
 <Button type="primary" style={{marginLeft:"10px"}} onClick={this.exportExcelFile}>导出Excel</Button>
```

3、后台处理数据填充

```
/\*\*
     \* 导出excel
     \* @param session
     \* @param response
     \* @return
     \*/
    @RequestMapping(value="/downloadExcel",produces = {"application/excel;charset=UTF-8"})
    public ResponseEntity<byte\[\]> download(HttpSession session, HttpServletResponse response){

        List<TStu> upList = itStuService.list();
//        for (int i=0;i<upList.size();i++){
////            //给报名表中的student 属性赋值
////            upList.get(i).setStudent(studentDao.findOne(upList.get(i).getStudentId()));
////        }
        String file;
        List<Map<String,Object>> list=ExcelUtils.createExcelRecord(upList);


        String columnNames\[\]={"id","姓名","性别"};//列名
        String keys\[\] = {"stuid","stuname","stusex"};//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
          ExcelUtils.createWorkBook(list,keys,columnNames).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte\[\] content = os.toByteArray();
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType("application/excel");
//        httpHeaders.add("Content-Disposition", "attachment;file).getBytes()));
        try{
            httpHeaders.add("Content-disposition","attachment;file));
        }catch (Exception e){
            e.printStackTrace();
        }

//        response.setHeader("Content-disposition","attachment;file));

        ResponseEntity<byte\[\]> responseEntity = new ResponseEntity<byte\[\]>(content,httpHeaders, HttpStatus.OK);
        return responseEntity;
    }
```

其中 ExcelUtils.java 为

```
package com.dv.dvademo.Util;

import com.dv.dvademo.entity.TStu;
import org.apache.poi.ss.usermodel.\*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ExcelUtils {
    /\*\*
     \* 创建excel文档，
     \* list 数据
     \*
     \* @param keys        list中map的key数组集合
     \* @param columnNames excel的列名
     \*/
    public static Workbook createWorkBook(List<Map<String, Object>> list, String\[\] keys, String columnNames\[\]) {
        // 创建excel工作簿
        Workbook wb = new XSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(list.get(0).get("sheetName").toString());
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for (int i = 0; i < keys.length; i++) {
            sheet.setColumnWidth((short) i, (short) (35.7 \* 200));
//            sheet.setcolumn
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT\_BOLD);
//        f.setCharSet(Font.SYMBOL\_CHARSET);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

//        Font f3=wb.createFont();
//        f3.setFontHeightInPoints((short) 10);
//        f3.setColor(IndexedColors.RED.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER\_DASH\_DOT);
        cs.setBorderRight(CellStyle.BORDER\_THIN);
        cs.setBorderTop(CellStyle.BORDER\_THIN);
        cs.setBorderBottom(CellStyle.BORDER\_THIN);
        cs.setAlignment(CellStyle.ALIGN\_LEFT);
//        cs.set
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER\_THIN);
        cs2.setBorderRight(CellStyle.BORDER\_THIN);
        cs2.setBorderTop(CellStyle.BORDER\_THIN);
        cs2.setBorderBottom(CellStyle.BORDER\_THIN);
        cs2.setAlignment(CellStyle.ALIGN\_CENTER);
        //设置列名
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnNames\[i\]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (short i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            // 在row行上创建一个方格
            for (short j = 0; j < keys.length; j++) {
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(keys\[j\]) == null ? " " : list.get(i).get(keys\[j\]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }

    public static List<Map<String, Object>> createExcelRecord(List<TStu> competitionSignUps) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < competitionSignUps.size(); j++) {
            TStu tt = competitionSignUps.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
//            String keys\[\]    =     {"studentNumber","studentName","studentSex","studentSpecialy","studentClass","time"};//map中的key
            mapValue.put("stuid", tt.getStuid());
            mapValue.put("stuname", tt.getStuname());
            mapValue.put("stusex", tt.getStusex());

            listmap.add(mapValue);
        }
        return listmap;
    }

    public static void responseDownloadWorkbook(HttpServletRequest request, HttpServletResponse response, String fileName, Workbook workbook) throws IOException {
        String agent = request.getHeader("User-Agent").toUpperCase();

        String encodedfileName = "";
        if (agent.indexOf("MSIE") != -1 || agent.indexOf("TRIDENT") != -1) { // IE
            encodedfileName = URLEncoder.encode(fileName, "utf-8");
        } else if (agent.indexOf("CHROME") != -1 || agent.indexOf("FIREFOX") != -1) { // 谷歌或火狐
            encodedfileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
        } else {
            encodedfileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
        }

        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\\"" + encodedfileName + "\\"");

        // 取出流中的数据
        ServletOutputStream os = response.getOutputStream();
        workbook.write(os);
        os.write(new byte\[\]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});// UTF-8
        os.flush();
        os.close();
    }
}
```

实体 TStu.java

```
package com.dv.dvademo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/\*\*
 \* <p>
 \* 
 \* </p>
 \*
 \* @author dv
 \* @since 2019-04-26
 \*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T\_STU")
public class TStu extends Model<TStu> {

    private static final long serialVersionUID = 1L;

    @TableId(value="STUID")
    private String stuid;

    @TableField("STUNAME")
    private String stuname;

    @TableField("STUSEX")
    private String stusex;


    @Override
    protected Serializable pkVal() {
        return this.stuid;
    }

}
```

效果图展示  
点击导出 Excel 按钮

![](http://upload-images.jianshu.io/upload_images/16959478-6e915c0844832528.png) 点击导出 Excel.png

打开刚才下载的用户列表 excel 文件

![](http://upload-images.jianshu.io/upload_images/16959478-5b46e5b83b0a1fd8.png) excel 内容. png

第二种方式 jxls：  
1、首先添加依赖

```
<!--jxls导出依赖jar包-->
        <dependency>
            <groupId>net.sf.jxls</groupId>
            <artifactId>jxls-core</artifactId>
            <version>1.0.6</version>
            <scope>compile</scope>
        </dependency>
```

2、创建用户列表 Excel 模板：用户列表 excel 模板. xlsx，复制到 resource/templates 目录下

![](http://upload-images.jianshu.io/upload_images/16959478-5e3ddb58ef7c57f6.png) excel 模板. png

前端代码：

```
//导出Excel
      exportExcelFile = () => {
          // window.location.href=\`${base}/t-stu/downloadExcel\`;
          window.location.href=\`${base}/t-stu/ExcelByMuban\`;
      }
```

后台代码：

```
/\*\*
     \* 导出excel 根据模板
     \*/
    @RequestMapping(value="/ExcelByMuban")
    public void ExportExcelByMuban(HttpServletRequest request,HttpServletResponse response) throws IOException , ParsePropertyException{

        List<TStu> list = itStuService.list();

        String fileName =  "用户列表信息.xlsx";
//        String filePath = this.getClass().getClassLoader().getResource("/templates/用户列表excel模板.xlsx").getPath();
        Map<String, Object> beans = new HashMap<String, Object>();

        try{
            XLSTransformer transformer = new XLSTransformer();
            //第一种方式
//        Resource resource = new ClassPathResource("/templates/用户列表excel模板.xlsx");
//        File sourceFile =  resource.getFile();
//        第二种方式
            InputStream is = this.getClass().getResourceAsStream("/templates/用户列表excel模板.xlsx");
            beans.put("dataList", list);
//        InputStream is = new BufferedInputStream(new FileInputStream(sourceFile));

            Workbook resultWorkbook = transformer.transformXLS(is, beans);
            is.close();

            ExcelUtils.responseDownloadWorkbook(request, response, fileName, resultWorkbook);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
```

根据模板生成的 excel 文件如下：

![](http://upload-images.jianshu.io/upload_images/16959478-99d345fff42b5d8a.png) excel 生成内容. png

二、导出 word 文档  
这里采用 freemarker 模板的形式  
1、首先添加依赖

```
<!-- 导出word文档-->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.20</version>
        </dependency>
```

2、新建一个 word 模板

![](http://upload-images.jianshu.io/upload_images/16959478-05f69277dd1b5108.png) word 文档模板. png

3、测试模板如上图，其中 list 是列表循环。然后另存为. xml 文件，直接修改该文件的后缀为. ftl

![](http://upload-images.jianshu.io/upload_images/16959478-405e57c5c6154d31.png) xml.png

![](http://upload-images.jianshu.io/upload_images/16959478-8c7a066ff34166b0.png) ftl.png

之后下载可以打开. ftl 的工具，lz 这里使用的是 sublime text 工具打开

![](http://upload-images.jianshu.io/upload_images/16959478-c1f8df487a33d01d.png) ftl 文件内容. png

然后保存放入 springboot 项目的 resource 下的 templates，如图：

![](http://upload-images.jianshu.io/upload_images/16959478-369b2d412f15cd2d.png) ftl 目录位置. png

前端代码：

```
//导出Word
      exportWordFile = ()=>{
          window.location.href=\`${base}/t-stu/downloadWord\`;

          // axios.post(\`${base}/t-stu/downloadWord\`, {
            
          // })
          // .then(function (response) {
          //   console.log("test",response);
          // })
          // .catch(function (error) {
          //   console.log(error);
          // });
      }
```

后台代码：

```
/\*\*
     \* 导出wordd文档
     \*/
    @RequestMapping(value="/downloadWord")
    public void ExportWord(HttpServletRequest request,HttpServletResponse response) throws IOException{

        List<TStu> upList = itStuService.list();

        Map dataMap = new HashMap();
        dataMap.put("title","你好");

        dataMap.put("listInfo",upList);
        dataMap.put("content","自动填充的内容");
//        dataMap.put("content2","自动填充的标题");  //可以放入多个键值对，对应模板的键
        
//        Date date = new Date();

        String name = WordUtil.createDoc(dataMap,"test用户列表.ftl");
        
//       System.out.println("打印路径"+path);
        WordUtil.responseDownloadFile(request,response,WordUtil.path,name);

        //删除文件
//        File f = new File(WordUtil.path+name);
//        f.delete();

    }
```

其中 WordUtil.java

```
package com.dv.dvademo.Util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.\*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class WordUtil {
    @Autowired
    private static Logger logger = LogManager.getLogger(WordUtil.class);



//    @Value("${com.dv.path}")
    public static String path;//文件路径

//    private static String url;

    // 记得去掉static
    @Value("${com.dv.path}")
    public void setDriver(String path) {
        WordUtil.path= path;
    }

    private static final String FTL\_FP = "/templates/"; //模板路径
    //private static final String FTL\_FP = "/etc/ftl/"; //模板路径

    //解决中文乱码
//    fileName = URLDecoder.decode(fileName, "UTF-8");



    private static Configuration configuration = null;
    static{
        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");//设置默认的编码
        //读配置文件
//        path = PropertiesUtil.get("FILE\_PATH")+"/";

    }

    /\*\*
     \*
     \* @param dataMap 数据库数据
     \* @param ftl  替换的模板
     \* @return
     \* @throws IOException
     \*/
    public static String createDoc(Map dataMap, String ftl) throws IOException {

        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
        // ftl文件存放路径
        //configuration.setClassForTemplateLoading(,FTL\_FP);
//        TemplateLoader templateLoader = new FileTemplateLoader(new File(FTL\_FP));
//        configuration.setDirectoryForTemplateLoading(new File(FTL\_FP));
//        TemplateLoader templateLoader = new ClassTemplateLoader(WordUtil.class, FTL\_FP);
//        configuration.setTemplateLoader(templateLoader);
        configuration.setClassForTemplateLoading(WordUtil.class,FTL\_FP);
        Template t = null;
        try {
            // test.ftl为要装载的模板
            t = configuration.getTemplate(ftl);
            t.setEncoding("utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 输出文档路径及名称
        String file\_;

        System.out.println(path);
        File outFile = new File(path+file\_name);
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
        } catch (Exception e1) {
            logger.error(e1);
            file\_name = "";
        }
        try {
            t.process(dataMap, out);
            out.close();
        } catch (TemplateException e2) {
            logger.error(e2);
            file\_name = "";
        } catch (IOException e) {
            logger.error(e);
            file\_name = "";
        }
        return file\_name;
    }

    /\*\*
     \* 通用文件下载方法
     \*
     \* @param response
     \* @param filePath
     \*            文件绝对路径
     \*            this.getClass().getClassLoader().getResource("file").getPath
     \*            ();
     \* @param fileName
     \*            文件名
     \* @throws IOException
     \*/
    public static void responseDownloadFile(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) throws IOException {
        // 读到流中
        InputStream inStream = new FileInputStream(filePath + fileName);// 文件的存放路径

        String agent = request.getHeader("User-Agent").toUpperCase();

        String encodedfileName = "";
        if (agent.indexOf("MSIE") != -1 || agent.indexOf("TRIDENT") != -1) { // IE
            encodedfileName = URLEncoder.encode(fileName, "utf-8");
        } else if (agent.indexOf("CHROME") != -1 || agent.indexOf("FIREFOX") != -1) { // 谷歌或火狐
            encodedfileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
        } else {
            encodedfileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
        }

        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\\"" + encodedfileName + "\\"");

        // 循环取出流中的数据
        byte\[\] b = new byte\[100\];
        int len;
        while ((len = inStream.read(b)) > 0)
            response.getOutputStream().write(b, 0, len);
        inStream.close();
    }
}
```

点击导出 word 文档按钮下载打开 word 内容如下

![](http://upload-images.jianshu.io/upload_images/16959478-3ecae9089298960a.png) word 文档内容. png

完结!