# XWPFDocument 创建和读取 Office Word 文档基础篇（一）

注：有不正确的地方还望大神能够指出，抱拳了 老铁！



参考 API:http://poi.apache.org/apidocs/org/apache/poi/xwpf/usermodel/XWPFDocument.html

主要参考文章 1：http://www.cnblogs.com/Springmoon-venn/p/5494602.html

主要参考文章 2：http://elim.iteye.com/blog/2049110

主要参考文章 3：http://doc.okbase.net/oh_Maxy/archive/154764.html

## 结构介绍

建议大家使用 office word 来创建文档。（wps 和 word 结构有些不一样）

IBodyElement ------------------- 迭代器（段落和表格）

XWPFComment ------------------- 评论（个人理解应该是批注）

XWPFSDT

XWPFFooter ------------------- 页脚

XWPFFootnotes ------------------- 脚注

XWPFHeader ------------------- 页眉

XWPFHyperlink ------------------- 超链接

XWPFNumbering ------------------- 编号（我也不知是啥...）

XWPFParagraph ------------------- 段落

XWPFPictureData ------------------- 图片

XWPFStyles ------------------- 样式（设置多级标题的时候用）

XWPFTable ------------------- 表格



### 1、正文段落



一个文档包含多个段落，一个段落包含多个 Runs，一个 Runs 包含多个 Run，Run 是文档的最小单元

获取所有段落：`List<XWPFParagraph> paragraphs = word.getParagraphs();`

获取一个段落中的所有 Runs：`List<XWPFRun> xwpfRuns = xwpfParagraph.getRuns();`

获取一个 Runs 中的一个 Run：`XWPFRun run = xwpfRuns.get(index);`

XWPFRun-- 代表具有相同属性的一段文本



### 2、正文表格

一个文档包含多个表格，一个表格包含多行，一行包含多列（格），每一格的内容相当于一个完整的文档

获取所有表格：`List<XWPFTable> xwpfTables = doc.getTables();`

获取一个表格中的所有行：`List<XWPFTableRow> xwpfTableRows = xwpfTable.getRows();`

获取一行中的所有列：`List<XWPFTableCell> xwpfTableCells = xwpfTableRow.getTableCells();`

获取一格里的内容：`List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();`

之后和正文段落一样

注：

1. 表格的一格相当于一个完整的 docx 文档，只是没有页眉和页脚。里面可以有表格，使用 xwpfTableCell.getTables() 获取，and so on
2. 在 poi 文档中段落和表格是完全分开的，如果在两个段落中有一个表格，在 poi 中是没办法确定表格在段落中间的。（当然除非你本来知道了，这句是废话）。只有文档的格式固定，才能正确的得到文档的结构

个人理解：我不能确定表格所处的位置（第一个段落后面 ，还是第二个段落后面...）

### 3、页眉：

一个文档可以有多个页眉, 页眉里面可以包含段落和表格

获取文档的页眉：List<XWPFHeader> headerList = doc.getHeaderList();

获取页眉里的所有段落：List<XWPFParagraph> paras = header.getParagraphs();

获取页眉里的所有表格：List<XWPFTable> tables = header.getTables();

之后就一样了

### 4、页脚：

页脚和页眉基本类似，可以获取表示页数的角标

## 言归正传 ------- 干货：

## 1、通过 XWPFDocument 读：段落 + 表格

### a、获取文档的所有段落

```java
InputStream is = new FileInputStream("D:\\table.docx");  
XWPFDocument doc = new XWPFDocument(is);  
List<XWPFParagraph> paras = doc.getParagraphs();
```

获取段落内容

```java
for (XWPFParagraph para : paras) {  
    //当前段落的属性  
//CTPPr pr = para.getCTP().getPPr();  
System.out.println(para.getText());  
}
```

### b、获取文档中所有的表格 

```java
List<XWPFTable> tables = doc.getTables();  
List<XWPFTableRow> rows;  
List<XWPFTableCell> cells;  

for (XWPFTable table : tables) {  
    //表格属性  
    CTTblPr pr = table.getCTTbl().getTblPr();  
    //获取表格对应的行  
    rows = table.getRows();  
    for (XWPFTableRow row : rows) {  
        //获取行对应的单元格  
        cells = row.getTableCells();  
        for (XWPFTableCell cell : cells) {  
            System.out.println(cell.getText());;  
        }  
    }  
}
```

##  2、XWPFDocument 生成 word

直接 new 一个空的 XWPFDocument，之后再往这个 XWPFDocument 里面填充内容，然后再把它写入到对应的输出流中。

### 新建一个文档

```java
XWPFDocument doc = new XWPFDocument();
//创建一个段落
XWPFParagraph para = doc.createParagraph();
 
//一个XWPFRun代表具有相同属性的一个区域：一段文本
XWPFRun run = para.createRun();
run.setBold(true); //加粗
run.setText("加粗的内容");
run = para.createRun();
run.setColor("FF0000");
run.setText("红色的字。");
OutputStream os = new FileOutputStream("D:\\simpleWrite.docx");
//把doc输出到输出流
doc.write(os);
this.close(os);
```



### 新建一个表格

```java
//XWPFDocument doc = new XWPFDocument();  
//创建一个5行5列的表格  
XWPFTable table = doc.createTable(5, 5);  
//这里增加的列原本初始化创建的那5行在通过getTableCells()方法获取时获取不到，但通过row新增的就可以。  
//table.addNewCol(); //给表格增加一列，变成6列  
table.createRow(); //给表格新增一行，变成6行  
List<XWPFTableRow> rows = table.getRows();  
//表格属性  
CTTblPr tablePr = table.getCTTbl().addNewTblPr();  
//表格宽度  
CTTblWidth width = tablePr.addNewTblW();  
width.setW(BigInteger.valueOf(8000));  
XWPFTableRow row;  
List<XWPFTableCell> cells;  
XWPFTableCell cell;  
int rowSize = rows.size();  
int cellSize;  
for (int i=0; i<rowSize; i++) {  
 row = rows.get(i);  
 //新增单元格  
 row.addNewTableCell();  
 //设置行的高度  
 row.setHeight(500);  
 //行属性  
//CTTrPr rowPr = row.getCtRow().addNewTrPr();  
 //这种方式是可以获取到新增的cell的。  
//List<CTTc> list = row.getCtRow().getTcList();  
 cells = row.getTableCells();  
 cellSize = cells.size();  
 for (int j=0; j<cellSize; j++) {  
    cell = cells.get(j);  
    if ((i+j)%2==0) {  
        //设置单元格的颜色  
        cell.setColor("ff0000"); //红色  
    } else {  
        cell.setColor("0000ff"); //蓝色  
    }  
    //单元格属性  
    CTTcPr cellPr = cell.getCTTc().addNewTcPr();  
    cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
    if (j == 3) {  
        //设置宽度  
        cellPr.addNewTcW().setW(BigInteger.valueOf(3000));  
    }  
    cell.setText(i + ", " + j);  
 }
}  
//文件不存在时会自动创建  
OutputStream os = new FileOutputStream("D:\\table.docx");  
//写入文件  
doc.write(os);  
this.close(os);
```

段落内容替换

```java
/** 
* 替换段落里面的变量 
* @param para 要替换的段落 
* @param params 参数 
*/  
private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {  
  List<XWPFRun> runs;  
  Matcher matcher;  
  if (this.matcher(para.getParagraphText()).find()) {  
     runs = para.getRuns();  
     for (int i=0; i<runs.size(); i++) {  
        XWPFRun run = runs.get(i);  
        String runText = run.toString();  
        matcher = this.matcher(runText);  
        if (matcher.find()) {  
            while ((matcher = this.matcher(runText)).find()) {  
               runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));  
            }  
            //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，  
            //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。  
            para.removeRun(i);  
            para.insertNewRun(i).setText(runText);  
        }  
     }  
  }  
}
```

直接调用 XWPFRun 的 setText() 方法设置文本时，在底层会重新创建一个 XWPFRun，把文本附加在当前文本后面，所以我们不能直接设值，需要先删除当前 run, 然后再自己手动插入一个新的 run。

// 抽取 word docx 文件中的图片

```java
String path ="D://abc.docx";  
File file = new File(path);  
try {  
  FileInputStream fis = new FileInputStream(file);  
  XWPFDocument document = new XWPFDocument(fis);  
  XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);  
  String text = xwpfWordExtractor.getText();  
  System.out.println(text);  
  List<XWPFPictureData> picList = document.getAllPictures();  
  for (XWPFPictureData pic : picList) {  
    System.out.println(pic.getPictureType() + file.separator + pic.suggestFileExtension()  
        +file.separator+pic.getFileName());  
    byte[] bytev = pic.getData();  
    FileOutputStream fos = new FileOutputStream("D:\\abc\\docxImage\\"+pic.getFileName());   
    fos.write(bytev);  
  }  
  fis.close();  
} catch (IOException e) {  
  e.printStackTrace();  
}  
}
```

### 多级标题结构

```java
/**
 * 自定义样式方式写word，参考statckoverflow的源码
 * 
 * @throws IOException
 */
public static void writeSimpleDocxFile() throws IOException {
    XWPFDocument docxDocument = new XWPFDocument();

    // 老外自定义了一个名字，中文版的最好还是按照word给的标题名来，否则级别上可能会乱
    addCustomHeadingStyle(docxDocument, "标题 1", 1);
    addCustomHeadingStyle(docxDocument, "标题 2", 2);

    // 标题1
    XWPFParagraph paragraph = docxDocument.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText("标题 1");
    paragraph.setStyle("标题 1");

    // 标题2
    XWPFParagraph paragraph2 = docxDocument.createParagraph();
    XWPFRun run2 = paragraph2.createRun();
    run2.setText("标题 2");
    paragraph2.setStyle("标题 2");

    // 正文
    XWPFParagraph paragraphX = docxDocument.createParagraph();
    XWPFRun runX = paragraphX.createRun();
    runX.setText("正文");

    // word写入到文件
    FileOutputStream fos = new FileOutputStream("D:/myDoc2.docx");
    docxDocument.write(fos);
    fos.close();
}

/**
 * 增加自定义标题样式。这里用的是stackoverflow的源码
 * 
 * @param docxDocument 目标文档
 * @param strStyleId 样式名称
 * @param headingLevel 样式级别
 */
private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

    CTStyle ctStyle = CTStyle.Factory.newInstance();
    ctStyle.setStyleId(strStyleId);

    CTString styleName = CTString.Factory.newInstance();
    styleName.setVal(strStyleId);
    ctStyle.setName(styleName);

    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
    indentNumber.setVal(BigInteger.valueOf(headingLevel));

    // lower number > style is more prominent in the formats bar
    ctStyle.setUiPriority(indentNumber);

    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
    ctStyle.setUnhideWhenUsed(onoffnull);

    // style shows up in the formats bar
    ctStyle.setQFormat(onoffnull);

    // style defines a heading of the given level
    CTPPr ppr = CTPPr.Factory.newInstance();
    ppr.setOutlineLvl(indentNumber);
    ctStyle.setPPr(ppr);

    XWPFStyle style = new XWPFStyle(ctStyle);

    // is a null op if already defined
    XWPFStyles styles = docxDocument.createStyles();

    style.setType(STStyleType.PARAGRAPH);
    styles.addStyle(style);

}
```

# java使用POI操作XWPFDocument中的XWPFRun（文本）对象的属性详解]

我用的是office word 2016版

XWPFRun是XWPFDocument中的一段文本对象（就是一段文字）

创建文档对象

`XWPFDocument docxDocument = new XWPFDocument();`

创建段落对象

`XWPFParagraph paragraphX = docxDocument.createParagraph();`

创建文本对象（今天的主角：XWPFRun）
`XWPFRun runX = paragraphX.createRun();`

 

```java
//默认：宋体（wps）/等线（office2016） 5号 两端对齐 单倍间距
runX.setText("舜发于畎亩之中， 傅说举于版筑之间， 胶鬲举于鱼盐之中， 管夷吾举于士...");
runX.setBold(false);//加粗
runX.setCapitalized(false);//我也不知道这个属性做啥的
//runX.setCharacterSpacing(5);//这个属性报错
runX.setColor("BED4F1");//设置颜色--十六进制
runX.setDoubleStrikethrough(false);//双删除线
runX.setEmbossed(false);//浮雕字体----效果和印记（悬浮阴影）类似
//runX.setFontFamily("宋体");//字体
runX.setFontFamily("华文新魏", FontCharRange.cs);//字体，范围----效果不详
runX.setFontSize(14);//字体大小
runX.setImprinted(false);//印迹（悬浮阴影）---效果和浮雕类似
runX.setItalic(false);//斜体（字体倾斜）
//runX.setKerning(1);//字距调整----这个好像没有效果
runX.setShadow(true);//阴影---稍微有点效果（阴影不明显）
//runX.setSmallCaps(true);//小型股------效果不清楚
//runX.setStrike(true);//单删除线（废弃）
runX.setStrikeThrough(false);//单删除线（新的替换Strike）
//runX.setSubscript(VerticalAlign.SUBSCRIPT);//下标(吧当前这个run变成下标)---枚举
//runX.setTextPosition(20);//设置两行之间的行间距//runX.setUnderline(UnderlinePatterns.DASH_LONG);//各种类型的下划线（枚举）//runX0.addBreak();//类似换行的操作（html的  br标签）runX0.addTab();//tab键runX0.addCarriageReturn();//回车键注意：addTab()和addCarriageReturn() 对setText()的使用先后顺序有关：比如先执行addTab,再写Text这是对当前这个Text的Table，反之是对下一个run的Text的Tab效果

```



# java使用POI操作XWPFDocument中的XWPFParagraph（段落）对象的属性略解



创建文本对象

`XWPFDocument docxDocument = new XWPFDocument();`

创建段落对象

`XWPFParagraph paragraphX = docxDocument.createParagraph();`

 



```java
//XWPFParagraph 段落属性
//paragraphX.addRun(runX0);//似乎并没有什么卵用
//paragraphX.removeRun(1);//按数组下标删除run(文本)
paragraphX.setAlignment(ParagraphAlignment.LEFT);//对齐方式
//paragraphX.setBorderBetween(Borders.LIGHTNING_1);//边界 （但是我设置了好几个值都没有效果）
//paragraphX.setFirstLineIndent(100);//首行缩进：-----效果不详
//paragraphX.setFontAlignment(3);//字体对齐方式：1左对齐 2居中3右对齐
//paragraphX.setIndentationFirstLine(2);//首行缩进：-----效果不详
//paragraphX.setIndentationHanging(1);//指定缩进，从父段落的第一行删除，将第一行上的缩进移回到文本流方向的开头。-----效果不详
//paragraphX.setIndentationLeft(2);//-----效果不详
//paragraphX.setIndentationRight(2);//-----效果不详
//paragraphX.setIndentFromLeft(2);//-----效果不详
//paragraphX.setIndentFromRight(2);//-----效果不详
//paragraphX.setNumID(new BigInteger("3"));//设置段落编号-----有效果看不懂（仅仅是整段缩进4个字）
//paragraphX.setPageBreak(true);//段前分页
//paragraphX.setSpacingAfter(1);//指定文档中此段最后一行以绝对单位添加的间距。-----效果不详
//paragraphX.setSpacingBeforeLines(2);//指定在该行的第一行中添加行单位之前的间距-----效果不详
//paragraphX.setStyle("标题 3");//段落样式：需要结合addCustomHeadingStyle(docxDocument, "标题 3", 3)配合使用
paragraphX.setVerticalAlignment(TextAlignment.BOTTOM);//文本对齐方式(我猜在table里面会有比较明显得到效果)
paragraphX.setWordWrapped(true);//这个元素指定一个消费者是否应该突破拉丁语文本超过一行的文本范围，打破单词跨两行（打破字符水平）或移动到以下行字（打破字级）-----(我没看懂:填个false还报异常了)
```

[
  ](javascript:void(0);)

# java使用POI操作XWPFDocument 生成Word实战（一）

注：我使用的word 2016
功能简介：
（1）使用jsoup解析html得到我用来生成word的文本（这个你们可以忽略）
（2）生成word、设置页边距、设置页脚（页码），设置页码（文本）

 

## 一、解析html

```java
Document doc = Jsoup.parseBodyFragment(contents);
Element body = doc.body();
Elements es = body.getAllElements();
```

## 二、循环Elements获取我需要的html标签



```java
boolean tag = false;
for (Element e : es) {
    //跳过第一个（默认会把整个对象当做第一个）
    if(!tag) {
        tag = true;
        continue;
    }
    //创建段落：生成word（核心）
    createXWPFParagraph(docxDocument,e);
}
```



## 三、生成段落



```java
/**
 * 构建段落
 * @param docxDocument
 * @param e
 */
public static void createXWPFParagraph(XWPFDocument docxDocument, Element e){
    XWPFParagraph paragraph = docxDocument.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText(e.text());
    run.setTextPosition(35);//设置行间距
    
    if(e.tagName().equals("titlename")){
        paragraph.setAlignment(ParagraphAlignment.CENTER);//对齐方式

        run.setBold(true);//加粗
        run.setColor("000000");//设置颜色--十六进制
        run.setFontFamily("宋体");//字体
        run.setFontSize(24);//字体大小
        
    }else if(e.tagName().equals("h1")){
        addCustomHeadingStyle(docxDocument, "标题 1", 1);
        paragraph.setStyle("标题 1");
        
        run.setBold(true);
        run.setColor("000000");
        run.setFontFamily("宋体");
        run.setFontSize(20);
    }else if(e.tagName().equals("h2")){
        addCustomHeadingStyle(docxDocument, "标题 2", 2);
        paragraph.setStyle("标题 2");
        
        run.setBold(true);
        run.setColor("000000");
        run.setFontFamily("宋体");
        run.setFontSize(18);
    }else if(e.tagName().equals("h3")){
        addCustomHeadingStyle(docxDocument, "标题 3", 3);
        paragraph.setStyle("标题 3");
        
        run.setBold(true);
        run.setColor("000000");
        run.setFontFamily("宋体");
        run.setFontSize(16);
    }else if(e.tagName().equals("p")){
        //内容
        paragraph.setAlignment(ParagraphAlignment.BOTH);//对齐方式
        paragraph.setIndentationFirstLine(WordUtil.ONE_UNIT);//首行缩进：567==1厘米
        
        run.setBold(false);
        run.setColor("001A35");
        run.setFontFamily("宋体");
        run.setFontSize(14);
        //run.addCarriageReturn();//回车键
    }else if(e.tagName().equals("break")){
        paragraph.setPageBreak(true);//段前分页(ctrl+enter)
    }
}    
```



## 四、设置页边距



```java
/**
* 设置页边距 (word中1厘米约等于567) 
* @param document
* @param left
* @param top
* @param right
* @param bottom
*/
public static void setDocumentMargin(XWPFDocument document, String left,String top, String right, String bottom) {  
CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
CTPageMar ctpagemar = sectPr.addNewPgMar();
if (StringUtils.isNotBlank(left)) {  
  ctpagemar.setLeft(new BigInteger(left));  
}  
if (StringUtils.isNotBlank(top)) {  
  ctpagemar.setTop(new BigInteger(top));  
}  
if (StringUtils.isNotBlank(right)) {  
  ctpagemar.setRight(new BigInteger(right));  
}  
if (StringUtils.isNotBlank(bottom)) {  
  ctpagemar.setBottom(new BigInteger(bottom));  
}  
} 
```



## 五、创建页眉



```java
/**
 * 创建默认页眉
 *
 * @param docx XWPFDocument文档对象
 * @param text 页眉文本
 * @return 返回文档帮助类对象，可用于方法链调用
 * @throws XmlException XML异常
 * @throws IOException IO异常
 * @throws InvalidFormatException 非法格式异常
 * @throws FileNotFoundException 找不到文件异常
 */
public static void createDefaultHeader(final XWPFDocument docx, final String text){
    CTP ctp = CTP.Factory.newInstance();
    XWPFParagraph paragraph = new XWPFParagraph(ctp, docx);
    ctp.addNewR().addNewT().setStringValue(text);
    ctp.addNewR().addNewT().setSpace(SpaceAttribute.Space.PRESERVE);
    CTSectPr sectPr = docx.getDocument().getBody().isSetSectPr() ? docx.getDocument().getBody().getSectPr() : docx.getDocument().getBody().addNewSectPr();
    XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);
    XWPFHeader header = policy.createHeader(STHdrFtr.DEFAULT, new XWPFParagraph[] { paragraph });
    header.setXWPFDocument(docx);
}}
```



## 六、创建页脚



```java
/**
 * 创建默认的页脚(该页脚主要只居中显示页码)
 * 
 * @param docx
 *            XWPFDocument文档对象
 * @return 返回文档帮助类对象，可用于方法链调用
 * @throws XmlException
 *             XML异常
 * @throws IOException
 *             IO异常
 */
public static void createDefaultFooter(final XWPFDocument docx) {
    // TODO 设置页码起始值
    CTP pageNo = CTP.Factory.newInstance();
    XWPFParagraph footer = new XWPFParagraph(pageNo, docx);
    CTPPr begin = pageNo.addNewPPr();
    begin.addNewPStyle().setVal(STYLE_FOOTER);
    begin.addNewJc().setVal(STJc.CENTER);
    pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);
    pageNo.addNewR().addNewInstrText().setStringValue("PAGE   \\* MERGEFORMAT");
    pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
    CTR end = pageNo.addNewR();
    CTRPr endRPr = end.addNewRPr();
    endRPr.addNewNoProof();
    endRPr.addNewLang().setVal(LANG_ZH_CN);
    end.addNewFldChar().setFldCharType(STFldCharType.END);
    CTSectPr sectPr = docx.getDocument().getBody().isSetSectPr() ? docx.getDocument().getBody().getSectPr() : docx.getDocument().getBody().addNewSectPr();
    XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);
    policy.createFooter(STHdrFtr.DEFAULT, new XWPFParagraph[] { footer });
}
```



## 七、自定义标题样式（这个在我另一篇word基础中也有提及）



```java
/**
 * 增加自定义标题样式。这里用的是stackoverflow的源码
 * 
 * @param docxDocument 目标文档
 * @param strStyleId 样式名称
 * @param headingLevel 样式级别
 */
private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

    CTStyle ctStyle = CTStyle.Factory.newInstance();
    ctStyle.setStyleId(strStyleId);

    CTString styleName = CTString.Factory.newInstance();
    styleName.setVal(strStyleId);
    ctStyle.setName(styleName);

    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
    indentNumber.setVal(BigInteger.valueOf(headingLevel));

    // lower number > style is more prominent in the formats bar
    ctStyle.setUiPriority(indentNumber);

    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
    ctStyle.setUnhideWhenUsed(onoffnull);

    // style shows up in the formats bar
    ctStyle.setQFormat(onoffnull);

    // style defines a heading of the given level
    CTPPr ppr = CTPPr.Factory.newInstance();
    ppr.setOutlineLvl(indentNumber);
    ctStyle.setPPr(ppr);

    XWPFStyle style = new XWPFStyle(ctStyle);

    // is a null op if already defined
    XWPFStyles styles = docxDocument.createStyles();

    style.setType(STStyleType.PARAGRAPH);
    styles.addStyle(style);

}
```



## 八、设置页码大小及纸张方向



```java
/** 
 * 设置页面大小及纸张方向 landscape横向
 * @param document
 * @param width
 * @param height
 * @param stValue
 */
public void setDocumentSize(XWPFDocument document, String width,String height, STPageOrientation.Enum stValue) {
    CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
    CTPageSz pgsz = sectPr.isSetPgSz() ? sectPr.getPgSz() : sectPr.addNewPgSz();
    pgsz.setH(new BigInteger(height));
    pgsz.setW(new BigInteger(width));
    pgsz.setOrient(stValue);
}
```



## 九、效果展示

![img](https://images2017.cnblogs.com/blog/803841/201709/803841-20170919172548056-234046575.png)

![img](https://images2017.cnblogs.com/blog/803841/201709/803841-20170919172723009-304603940.png)

![img](https://images2017.cnblogs.com/blog/803841/201709/803841-20170919172817900-1901848655.png)

 

 ![img](https://images2017.cnblogs.com/blog/803841/201709/803841-20170919172945696-1775798437.png)

 

十、demo源码及生成的word文件（相应的jar包大家可以去阿里的maven仓库下载）

 demo：https://pan.baidu.com/s/1jHFLniI

![img](https://images2017.cnblogs.com/blog/803841/201709/803841-20170919173558446-1912992990.png)

 