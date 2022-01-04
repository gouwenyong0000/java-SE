jx:each(items="list" var="item" lastCell="AREA_LAST_CELL")   
迭代指令，可以横向向右或竖向向下输出集合内容，也可以输出多个表格。   
[官网说明](http://jxls.sourceforge.net/reference/each_command.html)

指令属性：

| 属性 | 作用 |
|---|---|
| items | 要迭代的集合 |
| var | 集合每一项的上下文名字 |
| area | 输出内容的单元格区域，可空，如果空则以lastCell属性指定区域为准 |
| direction | 输出内容方向：DOWN（向下，默认）、RIGHT（向右），只有这两种 |
| select | 筛选条件 |
| groupBy | 分组属性 |
| groupOrder | 分组排序：‘desc’ or ‘asc’ |
| cellRefGenerator | 目标单元格引用创建的自定义策略（我还没搞懂这个） |
| multisheet | Sheet表格名，如果指定了这个属性，则是生成多个表格 |
| lastCell | 指令解析区域 |

- 示例   

excel模板文件：   
![excel模板](img/180828_b9e26cc0_1424806.png "excel模板.png")   
生成结果：   
![生成结果](img/181016_90bf6548_1424806.png "生成结果.png")


- multisheet 生成多表格

代码：
``` java
//list分别有张三、李四、王五 3 条数据
List<Employee> list = getEmployees();
//表格名，类型必需为List<String>
List<String> sheetNames = new ArrayList<>();
for (int i = 0; i < list.size(); i++) {
    sheetNames.add(list.get(i).getName());
}
JxlsBuilder jxlsBuilder = JxlsBuilder
    .getBuilder("dome/employees.xlsx")
    .out("D:/out.xlsx")
    .ignoreImageMiss(true)
    .putVar("list", list)
    //生成的每个表格名
    .putVar("sheetNames", sheetNames)
    //删除模板表格
    .removeSheet("Sheet")
    .build();
```

excel模板文件：  
![模板文件](img/183208_205fd695_1424806.png "模板文件.png")   
生成结果：  
![生成结果](img/183248_d75f58dc_1424806.png "生成结果.png")