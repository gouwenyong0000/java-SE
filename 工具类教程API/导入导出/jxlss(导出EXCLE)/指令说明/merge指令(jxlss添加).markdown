jx:merge(cols="2" rows="2" minCols="2" minRows="2" lastCell="A1")   
合并单元格，从指令单元格开始合并。此为`jxlss`自定义指令，原`jxls`没有该指令。

指令属性：

| 属性 | 作用 |
|---|---|
| cols | 合并的列数 |
| rows | 合并的行数 |
| minCols | 最小合并的列数 |
| minRows | 最小合并的行数 |
| lastCell | 合并区域，如果上面4个没值则以这个单元格的区域合并。<br>该属性填目标合并范围最后一个单元格 |

- 示例   

excel模板文件：   
![excel模板](img/182654_436a6779_1424806.png "excel模板.png")   

生成结果：   
![输入图片说明](img/141954_21b16452_1424806.png "屏幕截图.png")