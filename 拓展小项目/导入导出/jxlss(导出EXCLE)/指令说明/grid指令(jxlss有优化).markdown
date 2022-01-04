动态列生成，可以动态指定生成的列名和数据   
[官网说明](http://jxls.sourceforge.net/reference/grid_command.html)

指令属性：

| 属性 | 类型 | 作用 |
|---|---|---|
| headers | Iterable<String> | 列名集合 |
| props | String<br>String[]`仅jxlss支持` | 对应的数据集合的字段名，如果是字符串类型则字段名用英文逗号分割 |
| data | Iterable | 数据集合 |
| areas | Area[2] | 指定显示列名的区域和显示数据的区域 |

该指令包含两个必要元素    
显示列名元素：
```
${header}
```
显示数据元素
```
${cell}
```

- 示例   

excel模板文件：  
![excel模板](img/170740_600cdf08_1424806.png "excel模板.png")   
生成结果：   
![生成结果](img/170836_5f115e33_1424806.png "生成结果.png")


- 代码

``` java
//列名集合
String[] headers = {"姓名", "民族", "籍贯", "住址", "联系电话"};
//数据
List<Employee> list = getEmployees();
//对应数据的字段名
String[] props = {"name", "nation", "nativePlace", "address", "address"};

JxlsBuilder jxlsBuilder = JxlsBuilder
        .getBuilder("dome/grid.xlsx")
        .out("D:/out.xlsx")
        .putVar("headers", Arrays.asList(headers))
        .putVar("props", props)
        .putVar("list", list)
        .build();
```