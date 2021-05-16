# 使用 jxls2.X 导出excel文件

**2018年4月27日更新**
已更新最新版本，移步到最版本 **jxlss** http://blog.csdn.net/lnktoking/article/details/79195500

jxls是基于POI API的Excel报表生成工具，可以更简单、灵活的生成我们想要的Excel格式。我们只需要准备写好格式的Excel文件，在文件需要填充内容的位置加上jxls指令，然后就可以导出我们最终想要的Excel。

```
注意：这里讲的是jxls2，不是jxls1。jxls2不再使用<jx:xx/>标签，改为在批注中写指令，这样模版看起来更简洁。
```

jxls2 api首页：http://jxls.sourceforge.net/index.html
jxls2更多的介绍请看官网和自行搜索，小弟不擅长写这些东东，请多多谅解。

下面看例子：

## Excel模版文件

![Excel模版文件](https://img-blog.csdn.net/20161026113809571)

所有批注
![所有批注](https://img-blog.csdn.net/20161026113852415)

最终导出Excel文件效果
![最终导出Excel文件效果](https://img-blog.csdn.net/20161026114033760)

下面是项目案例

## 项目结构如下

![项目结构](https://img-blog.csdn.net/20161026114340730)

下面是java代码

## Demo.java

```java
public class Demo {
    static byte[] imgBytes;
    static{
        try {
            InputStream ins = new FileInputStream(Demo.class.getClassLoader().getResource("img/img.jpg").getFile());
            imgBytes = IOUtils.toByteArray(ins);
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        HashMap<String, Object> beans = new HashMap<String, Object>();
        beans.put("title", "这是标题");
        beans.put("list", getList());
        beans.put("logoImg", imgBytes);
        beans.put("startTime", new Date());
        beans.put("endTime", new Date());
        beans.put("exportTime", new Date());
        beans.put("url", "http://www.baidu.com");
        beans.put("company", "LK_King");
        File template = JxlsUtil.getTemplate("demo.xlsx");
        File out = new File("D:/out.xlsx");
        JxlsUtil.exportExcel(template, out, beans);
        System.out.println("导出成功");
    }

    static List<User> getList(){
        List<User> list = new ArrayList<User>();
        User zs = new User("张三");
        User ls = new User("李四");
        User ww = new User("王五");
        for (int i = 1; i <= 3; i++) {
            zs.getClients().add(new User("客户"+i, true, imgBytes));
        }
        for (int i = 1; i <= 2; i++) {
            ww.getClients().add(new User("客户"+i, false, null));
        }
        list.add(zs);
        list.add(ls);
        list.add(ww);
        return list;
    }
}
```

## User.java

```java
public class User {
    private String name;
    private boolean sign;
    private byte[] imgBytes;
    private List<User> clients = new ArrayList<User>();

    public User(String name) {
        super();
        this.name = name;
    }

    public User(String name, boolean sign, byte[] imgBytes) {
        super();
        this.name = name;
        this.sign = sign;
        this.imgBytes = imgBytes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean getSign() {
        return sign;
    }
    public void setSign(boolean sign) {
        this.sign = sign;
    }
    public byte[] getImgBytes() {
        return imgBytes;
    }
    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }

    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        this.clients = clients;
    }
}
```

## JxlsUtil.java

```java
public class JxlsUtil {
    static{
        //添加自定义指令（可覆盖jxls原指令）
        XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("each", EachCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("link", LinkCommand.class);
    }
    /** jxls模版文件目录 */
    private final static String TEMPLATE_PATH = "jxlsTemplate";
    /**
     * 导出excel
     * @param is - excel文件流
     * @param os - 生成模版输出流
     * @param beans - 模版中填充的数据
     * @throws IOException 
     */
    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> beans) throws IOException {
        Context context = new Context();
        if (beans != null) {
            for (String key : beans.keySet()) {
                context.putVar(key, beans.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("jx", new JxlsUtil());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);
        jxlsHelper.processTemplate(context, transformer);
    }

    /**
     * 导出excel
     * @param xlsPath excel文件
     * @param outPath 输出文件
     * @param beans 模版中填充的数据
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void exportExcel(String xlsPath, String outPath, Map<String, Object> beans) throws FileNotFoundException, IOException {
            exportExcel(new FileInputStream(xlsPath), new FileOutputStream(outPath), beans);
    }

    /**
     * 导出excel
     * @param xls excel文件
     * @param out 输出文件
     * @param beans 模版中填充的数据
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void exportExcel(File xls, File out, Map<String, Object> beans) throws FileNotFoundException, IOException {
            exportExcel(new FileInputStream(xls), new FileOutputStream(out), beans);
    }
    /**
     * 获取jxls模版文件
     */
    public static File getTemplate(String name){
        String templatePath = JxlsUtil.class.getClassLoader().getResource(TEMPLATE_PATH).getPath();
        File template = new File(templatePath, name);
        if(template.exists()){
            return template;
        }
        return null;
    }

    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回第一个不为空的对象
    public Object defaultIfNull(Object... objs) {
        for (Object o : objs) {
            if (o != null)
                return o;
        }
        return null;
    }

    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }
}
```

## EachCommand.java

```java
/**
 * 扩展jxls each命令
 * 增加retainEmpty属性，当items为null或size为0时，也保留当前一行数据的格式
 * 循环增加下标变量“var_index”。如var="item"，获取下标方法：${item_index}
 */
public class EachCommand extends AbstractCommand {
    public enum Direction {RIGHT, DOWN}

    private String var;
    private String items;
    private String select;
    private Area area;
    private Direction direction = Direction.DOWN;
    private CellRefGenerator cellRefGenerator;
    private String multisheet;

    private String retainEmpty; //当集合大小为0时，是否最少保留一行空行数据

    public EachCommand() {
    }

    /**
     * @param var       name of the key in the context to contain each collection items during iteration
     * @param items     name of the collection bean in the context
     * @param direction defines processing by rows (DOWN - default) or columns (RIGHT)
     */
    public EachCommand(String var, String items, Direction direction) {
        this.var = var;
        this.items = items;
        this.direction = direction == null ? Direction.DOWN : direction;
    }

    public EachCommand(String var, String items, Area area) {
        this(var, items, area, Direction.DOWN);
    }

    public EachCommand(String var, String items, Area area, Direction direction) {
        this(var, items, direction);
        if (area != null) {
            this.area = area;
            addArea(this.area);
        }
    }

    /**
     * @param var              name of the key in the context to contain each collection items during iteration
     * @param items            name of the collection bean in the context
     * @param area             body area for this command
     * @param cellRefGenerator generates target cell ref for each collection item during iteration
     */
    public EachCommand(String var, String items, Area area, CellRefGenerator cellRefGenerator) {
        this(var, items, area, (Direction) null);
        this.cellRefGenerator = cellRefGenerator;
    }

    /**
     * Gets iteration directino
     *
     * @return current direction for iteration
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets iteration direction
     *
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setDirection(String direction) {
        this.direction = Direction.valueOf(direction);
    }

    /**
     * Gets defined cell ref generator
     *
     * @return current {@link CellRefGenerator} instance or null
     */
    public CellRefGenerator getCellRefGenerator() {
        return cellRefGenerator;
    }

    public void setCellRefGenerator(CellRefGenerator cellRefGenerator) {
        this.cellRefGenerator = cellRefGenerator;
    }

    public String getName() {
        return "each";
    }

    /**
     * Gets current variable name for collection item in the context during iteration
     *
     * @return collection item key name in the context
     */
    public String getVar() {
        return var;
    }

    /**
     * Sets current variable name for collection item in the context during iteration
     *
     * @param var
     */
    public void setVar(String var) {
        this.var = var;
    }

    /**
     * Gets collection bean name
     *
     * @return collection bean name in the context
     */
    public String getItems() {
        return items;
    }

    /**
     * Sets collection bean name
     *
     * @param items collection bean name in the context
     */
    public void setItems(String items) {
        this.items = items;
    }

    /**
     * Gets current 'select' expression for filtering out collection items
     *
     * @return current 'select' expression or null if undefined
     */
    public String getSelect() {
        return select;
    }

    /**
     * Sets current 'select' expression for filtering collection
     *
     * @param select filtering expression
     */
    public void setSelect(String select) {
        this.select = select;
    }

    /**
     * @return Context variable name holding a list of Excel sheet names to output the collection to
     */
    public String getMultisheet() {
        return multisheet;
    }

    /**
     * Sets name of context variable holding a list of Excel sheet names to output the collection to
     * @param multisheet
     */
    public void setMultisheet(String multisheet) {
        this.multisheet = multisheet;
    }

    @Override
    public Command addArea(Area area) {
        if (area == null) {
            return this;
        }
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'each' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @SuppressWarnings("rawtypes")
    public Size applyAt(CellRef cellRef, Context context) {
        Collection itemsCollection = Util.transformToCollectionObject(getTransformationConfig().getExpressionEvaluator(), items, context);
        int width = 0;
        int height = 0;
        int index = 0;
        CellRefGenerator cellRefGenerator = this.cellRefGenerator;
        if (cellRefGenerator == null && multisheet != null) {
            List<String> sheetNameList = extractSheetNameList(context);
            cellRefGenerator = new SheetNameGenerator(sheetNameList, cellRef);
        }
        CellRef currentCell = cellRefGenerator != null ? cellRefGenerator.generateCellRef(index, context) : cellRef;
        JexlExpressionEvaluator selectEvaluator = null;
        if (select != null) {
            selectEvaluator = new JexlExpressionEvaluator(select);
        }
        for (Object obj : itemsCollection) {
            context.putVar(var, obj);
            context.putVar(var+"_index", index);
            if (selectEvaluator != null && !Util.isConditionTrue(selectEvaluator, context)) {
                context.removeVar(var);
                context.removeVar(var+"_index");
                continue;
            }
            Size size = area.applyAt(currentCell, context);
            index++;
            if (cellRefGenerator != null) {
                width = Math.max(width, size.getWidth());
                height = Math.max(height, size.getHeight());
                if(index < itemsCollection.size()) {
                    currentCell = cellRefGenerator.generateCellRef(index, context);
                }
            } else if (direction == Direction.DOWN) {
                currentCell = new CellRef(currentCell.getSheetName(), currentCell.getRow() + size.getHeight(), currentCell.getCol());
                width = Math.max(width, size.getWidth());
                height += size.getHeight();
            } else {
                currentCell = new CellRef(currentCell.getSheetName(), currentCell.getRow(), currentCell.getCol() + size.getWidth());
                width += size.getWidth();
                height = Math.max(height, size.getHeight());
            }
            context.removeVar(var);
            context.removeVar(var+"_index");
        }
        if("true".equalsIgnoreCase(retainEmpty) && width == 0 && height == 0){
            return area.applyAt(currentCell, context);
        }
        return new Size(width, height);
    }

    @SuppressWarnings("unchecked")
    private List<String> extractSheetNameList(Context context) {
        try {
            return (List<String>) context.getVar(multisheet);
        } catch (Exception e) {
            throw new JxlsException("Failed to get sheet names from " + multisheet, e);
        }
    }

    public String getRetainEmpty() {
        return retainEmpty;
    }

    public void setRetainEmpty(String retainEmpty) {
        this.retainEmpty = retainEmpty;
    }

}
```

## ImageCommand.java

```java
/**
 * 解决图片最少占4个单元格才显示的问题并保留原来单元格样式
 */
public class ImageCommand extends AbstractCommand {

    private byte[] imageBytes;
    private ImageType imageType = ImageType.PNG;
    private Area area;
    /**
     * Expression that can be evaluated to image byte array byte[]
     */
    private String src;
    private String text; //无法读取图片时的提示

    public ImageCommand() {
    }

    public ImageCommand(String image, ImageType imageType) {
        this.src = image;
        this.imageType = imageType;
    }

    public ImageCommand(byte[] imageBytes, ImageType imageType) {
        this.imageBytes = imageBytes;
        this.imageType = imageType;
    }

    /**
     * @return src expression producing image byte array
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src expression resulting in image byte array
     */
    public void setSrc(String src) {
        this.src = src;
    }

    public void setImageType(String strType){
        imageType = ImageType.valueOf(strType);
    }

    @Override
    public Command addArea(Area area) {
        if( super.getAreaList().size() >= 1){
            throw new IllegalArgumentException("You can add only a single area to 'image' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    public String getName() {
        return "image";
    }

    public Size applyAt(CellRef cellRef, Context context) {
        if( area == null ){
            throw new IllegalArgumentException("No area is defined for image command");
        }
        Transformer transformer = getTransformer();
        Size size = area.getSize();
        //获取图片显示区域是时候，多加一行和一列，获取完之后再恢复原来大小
        size.setWidth(size.getWidth() + 1);
        size.setHeight(size.getHeight() + 1);
        AreaRef areaRef = new AreaRef(cellRef, size);
        size.setWidth(size.getWidth() - 1);
        size.setHeight(size.getHeight() - 1);
        byte[] imgBytes = imageBytes;
        if( src != null ){
            Object imgObj = getTransformationConfig().getExpressionEvaluator().evaluate(src, context.toMap());
            if(imgObj != null){
                if( !(imgObj instanceof byte[]) ){
                    throw new IllegalArgumentException("src value must contain image bytes (byte[])");
                }
                imgBytes = (byte[]) imgObj;
            }
        }
        if(imgBytes != null){
            transformer.addImage(areaRef, imgBytes, imageType);
        }
        area.applyAt(cellRef, context); //恢复原有的样式
        if(imgBytes == null && StringUtils.isNotBlank(text)){
            PoiTransformer poi = (PoiTransformer)transformer;
            Sheet sheet = poi.getWorkbook().getSheet(cellRef.getSheetName());
            Row row = sheet.getRow(cellRef.getRow());
            if(row != null && row.getCell(cellRef.getCol()) != null){
                row.getCell(cellRef.getCol()).setCellValue(text);
            }
        }
        return size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

```

## LinkCommand.java

```java
/**
 * url超链接
 */
public class LinkCommand extends AbstractCommand {
    private String url;     //url地址（必选）
    private String title;   //url显示标题（可选）
    private Area area;

    @Override
    public String getName() {
        return "link";
    }

    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'link' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        if(StringUtils.isBlank(url)){
            throw new NullPointerException("url不能为空");
        }
        area.applyAt(cellRef, context);
        Transformer transformer = this.getTransformer();
        if(transformer instanceof PoiTransformer){
            poiLink(cellRef, context, (PoiTransformer)transformer);
        }else if(transformer instanceof JexcelTransformer){
            jxcelLink(cellRef, context, (JexcelTransformer)transformer);
        }
        return new Size(1, 1);
    }

    protected void poiLink(CellRef cellRef, Context context, PoiTransformer transformer){
        Object urlObj = getTransformationConfig().getExpressionEvaluator().evaluate(url, context.toMap());
        if(urlObj == null)
            return;

        String url = urlObj.toString();
        String title = url;
        if(StringUtils.isNotBlank(this.title)){
            Object titleObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.title, context.toMap());
            if(titleObj != null)
                title = titleObj.toString();
        }

        Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());
        Row row = sheet.getRow(cellRef.getRow());
        if(row == null){
            row = sheet.createRow(cellRef.getRow());
        }
        Cell cell = row.getCell(cellRef.getCol());
        if(cell == null){
            cell = row.createCell(cellRef.getCol());
        }
        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cell.setCellFormula("HYPERLINK(\"" + url+ "\",\"" + title + "\")");
        if(!url.equals(title)){
            cell.setCellValue(title);
        }

        CellStyle linkStyle = cell.getCellStyle();
        Font cellFont= transformer.getWorkbook().createFont();
        cellFont.setUnderline((byte) 1);
        cellFont.setColor(HSSFColor.BLUE.index);
        linkStyle.setFont(cellFont);
    }

    protected void jxcelLink(CellRef cellRef, Context context, JexcelTransformer transformer){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
```

## MergeCommand.java

```java
/**
 * @author lk
 * 合并单元格命令
 */
public class MergeCommand extends AbstractCommand {
    private String cols;    //合并的列数
    private String rows;    //合并的行数
    private Area area;
    private CellStyle cellStyle;    //第一个单元格的样式

    @Override
    public String getName() {
        return "merge";
    }

    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'merge' command");
        }
        this.area = area;
        return super.addArea(area);
    }

    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        int rows = 1, cols = 1;
        if(StringUtils.isNotBlank(this.rows)){
            Object rowsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.rows, context.toMap());
            if(rowsObj != null && NumberUtils.isDigits(rowsObj.toString())){
                rows = NumberUtils.toInt(rowsObj.toString());
            }
        }
        if(StringUtils.isNotBlank(this.cols)){
            Object colsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.cols, context.toMap());
            if(colsObj != null && NumberUtils.isDigits(colsObj.toString())){
                cols = NumberUtils.toInt(colsObj.toString());
            }
        }

        if(rows > 1 || cols > 1){
            Transformer transformer = this.getTransformer();
            if(transformer instanceof PoiTransformer){
                return poiMerge(cellRef, context, (PoiTransformer)transformer, rows, cols);
            }else if(transformer instanceof JexcelTransformer){
                return jexcelMerge(cellRef, context, (JexcelTransformer)transformer, rows, cols);
            }
        }
        area.applyAt(cellRef, context);
        return new Size(1, 1);
    }

    protected Size poiMerge(CellRef cellRef, Context context, PoiTransformer transformer, int rows, int cols){
        Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());
        CellRangeAddress region = new CellRangeAddress(
                cellRef.getRow(), 
                cellRef.getRow() + rows - 1, 
                cellRef.getCol(), 
                cellRef.getCol() + cols - 1);
        sheet.addMergedRegion(region);

        //合并之后单元格样式会丢失，以下操作将合并后的单元格恢复成合并前第一个单元格的样式
        area.applyAt(cellRef, context);
        if(cellStyle == null){
            PoiCellData cellData = (PoiCellData)transformer.getCellData(cellRef);
            cellStyle = cellData.getCellStyle();
        }
        setRegionStyle(cellStyle, region, sheet);
        return new Size(cols, rows);
    }

    protected Size jexcelMerge(CellRef cellRef, Context context, JexcelTransformer transformer, int rows, int cols){
        try {
            transformer.getWritableWorkbook().getSheet(cellRef.getSheetName())
                .mergeCells(
                        cellRef.getRow(), 
                        cellRef.getCol(), 
                        cellRef.getRow() + rows - 1 , 
                        cellRef.getCol() + cols - 1);
            area.applyAt(cellRef, context);
        } catch (WriteException e) {
            throw new IllegalArgumentException("合并单元格失败");
        }
        return new Size(cols, rows);
    }

    private static void setRegionStyle(CellStyle cs, CellRangeAddress region, Sheet sheet) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cs);
            }
        }
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}

```

[项目源码下载](http://download.csdn.net/detail/lnktoking/9664506)
[jxls 2.3.0下载](http://download.csdn.net/detail/lnktoking/9664484)
[项目所依赖的jar包下载](http://download.csdn.net/detail/lnktoking/9664491)