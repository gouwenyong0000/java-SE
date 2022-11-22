jx:image(lastCell="D10" src="image" imageType="PNG" size="auto" scaleX="1" scaleY="1")    
插入图片，原`jxls`的`image`指令是没有`scaleX`，`scaleY`，`size`这3个属性的，同时`src`也只支持byte[]类型数据。jxlss在jxls原来的基础上增加了上面3个属性，同时src增加支持图片路径String类型。

指令属性：

| 属性 | 类型 | 作用 |
| --- | --- | --- |
| src | String`仅jxlss支持` <br> byte[] <br> JxlsImage`仅jxlss支持` | 图片源，必填 |
| imageType | String | 图片格式，大写，如果图片源src为byte[] 则必填 |
| size`仅jxlss支持` | String | 图片显示大小，值为：<br> auto:默认，自适应单元格大小<br> original:显示原图大小 |
| scaleX`仅jxlss支持` <br> scaleY`仅jxlss支持` | Double | 图片大小控制，通过poi的`Picture.resize(scaleX, scaleX)`方法控制，详情看[文档](http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/Picture.html) |
| lastCell | 单元格 | 图片插入最后坐标单元格 |

- 示例 
  

代码：   
``` java
String outPath = "D:/out_image.xlsx";
String imgRoot = TestJxls.class.getClassLoader().getResource("jxls_templates").getPath();
String imgPath = "dome/zhangsan.jpg";

byte[] imageData = JxlsUtil.me().getImageData(imgRoot.concat("/").concat(imgPath));
JxlsImage jxlsImage = JxlsUtil.me().getJxlsImage(imgRoot.concat("/").concat(imgPath));
JxlsBuilder jxlsBuilder = JxlsBuilder
        .getBuilder("dome/image.xlsx")
        .out(outPath)
        .imageRoot(imgRoot)
        .putVar("imageStr", imgPath)
        .putVar("imageData", imageData)
        .putVar("jxlsImage", jxlsImage)
        .build();
```
excel模板文件：   
![excel模板](img/111623_89f4ec02_1424806.png "excel模板")
生成结果：
![生成结果](img/111458_d18fe063_1424806.png "生成结果.png")
