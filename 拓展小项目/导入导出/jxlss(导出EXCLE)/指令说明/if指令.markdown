jx:if(condition="表达式" lastCell="F9" areas=["A9:F9","A18:F18"])  
if指令，如果 condition 为 true 则在标签位置输出 areas[0] 的区域（也就是A9:F9），如果 condition 为 false 则在标签位置输出 areas[1] 的区域（也就是A18:F18）     
[官网说明](http://jxls.sourceforge.net/reference/if_command.html)

指令属性：

| 属性 | 作用 |
|---|---|
| condition | 条件表达式，返回true或false |
| areas | 区域数组["A9:F9","A18:F18"]，第一个是if区域，第二个是else区域，条件成功输出if区域，不成功则输出else区域 |
| lastCell | 指令解析区域 |


模板示例   
![模板](img/190829_7ccbafdc_1424806.png "模板.png")   

如果条件成立则输出`if区域`，需要注意的是`if区域`输出的位置是指令的位置   
![成立](img/190954_10c7ce41_1424806.png "成立.png")

如果条件不成立则输出`else区域`   
![输入图片说明](img/191137_7defdf74_1424806.png "屏幕截图.png")
