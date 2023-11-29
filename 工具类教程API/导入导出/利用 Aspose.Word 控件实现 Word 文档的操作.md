利用 Aspose.Word 控件实现 Word 文档的操作

Aspose 系列的控件，功能都挺好，之前一直在我的 Winform 开发框架中用 Aspose.Cell 来做报表输出，可以实现多样化的报表设计及输出，由于一般输出的内容比较正规化或者多数是表格居多，所以一般使用 Aspose.Cell 来实现我想要的各种 Excel 报表输出。虽然一直也知道 Aspose.Word 是用来生成 Word 文档的，而且深信其也是一个很强大的控件，但一直没用用到，所以就不是很熟悉。



偶然一次机会，一个项目的报表功能指定需要导出为 Word 文档，因此寻找了很多篇文章，不过多数介绍的比较简单一点，于是也参考了官方的帮助介绍，终于满足了客户的需求。下面我由浅入深来介绍这个控件在实际业务中的使用过程吧。



### 1、二维表格的 Word 操作



日常中，常见的内容输出就是二维表格的方式，表头比较固定，内容每行一条，那么在实际的使用控件我们该如何操作呢，其实这个控件这方面介绍的文章很多，参考一下就能做出来了。其实介绍这个就是要说明书签的重要性，这个在 Aspose.Cell 控件也是如此，书签除了可以用来替换内容，还可以用来标记内容输入的开始位置等等功能。



首先我们在一个空白的 Word 文档中绘制一个表格头，然后再换行的开始插入一个标签引用，插入书签有两种方式，一种是在 Word（2007、2010）的【插入】-【书签】中插入制定位置的书签引用，如下所示。



![img](https://pic002.cnblogs.com/images/2012/8867/2012082923414283.png)

一种是在 Word 的自定义快速访问工具栏上添加其他命令，如下步骤所示



![img](https://pic002.cnblogs.com/images/2012/8867/2012082923400533.png)

前者插入的书签是没有文字或者特别的标记，但是确实存在，后者会插入一个灰色块作为占位符，如下所示，我这这个二维表格的例子里面使用后者进行测试（两者同等效果的）

![img](https://pic002.cnblogs.com/images/2012/8867/2012082923432379.png)





这样设计好 Word 模板后，下一步就是如何利用代码生成二维表格了。首先这里提示一下，就是我故意设置了每个表格单元格的宽度不同，所以也就要求生成的行要和头部对应，所以表格生成每行之前，肯定要获得对应列的样式属性的，否则就会对应不上了。下面看代码。

```java
try
{
    Aspose.Words.Document doc = new Aspose.Words.Document(templateFile);
    Aspose.Words.DocumentBuilder builder = new Aspose.Words.DocumentBuilder(doc);

    DataTable nameList = DataTableHelper.CreateTable("编号,姓名,时间");
    DataRow row = null;
    for (int i = 0; i < 50; i++)
    {
        row = nameList.NewRow();
        row["编号"] = i.ToString().PadLeft(4, '0');
        row["姓名"] = "伍华聪 " + i.ToString();
        row["时间"] = DateTime.Now.ToString();
        nameList.Rows.Add(row);
    }

    List<double> widthList = new List<double>();
    for (int i = 0; i < nameList.Columns.Count; i++)
    {
        builder.MoveToCell(0, 0, i, 0); //移动单元格
        double width = builder.CellFormat.Width;//获取单元格宽度
        widthList.Add(width);
    }                    

    builder.MoveToBookmark("table");        //开始添加值
    for (var i = 0; i < nameList.Rows.Count; i++)
    {
        for (var j = 0; j < nameList.Columns.Count; j++)
        {
            builder.InsertCell();// 添加一个单元格                    
            builder.CellFormat.Borders.LineStyle = LineStyle.Single;
            builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
            builder.CellFormat.Width = widthList[j];
            builder.CellFormat.VerticalMerge = Aspose.Words.Tables.CellMerge.None;
            builder.CellFormat.VerticalAlignment = CellVerticalAlignment.Center;//垂直居中对齐
            builder.ParagraphFormat.Alignment = ParagraphAlignment.Center;//水平居中对齐
            builder.Write(nameList.Rows[i][j].ToString());
        }
        builder.EndRow();
    }
    doc.Range.Bookmarks["table"].Text = "";    // 清掉标示  

    doc.Save(saveDocFile);
    if (MessageUtil.ShowYesNoAndTips("保存成功，是否打开文件？") == System.Windows.Forms.DialogResult.Yes)
    {
        System.Diagnostics.Process.Start(saveDocFile);
    }
}
catch (Exception ex)
{
    LogHelper.Error(ex);
    MessageUtil.ShowError(ex.Message);
    return;
}
```

以上代码的步骤就是

1）创建 Aspose.Words.Document 和 Aspose.Words.DocumentBuilder 对象，然后生成数据的二维表格内容。

2）遍历模板表格，或者每一列的宽度，以备后用。

3）移动到表格的书签位置，然后开始录入数据，Word 表格的每个 Cell 都要求制定样式和宽度，这样才能和表格头部吻合。

4）保存文件内容到新的文件里面即可。

输出的效果如下所示。



![img](https://pic002.cnblogs.com/images/2012/8867/2012082923541262.png)





### 2、单元格合并的操作



常见的 Word 文件或者 Excel 文件中，都经常看到合并单元格的内容，因此这个部分也是非常常见的操作，必须掌握。



我们先看一个例子代码及效果。





```java
try
{
    Aspose.Words.Document doc = new Aspose.Words.Document(templateFile);
    Aspose.Words.DocumentBuilder builder = new Aspose.Words.DocumentBuilder(doc);

    builder.InsertCell();
    builder.CellFormat.Borders.LineStyle = LineStyle.Single;
    builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
    builder.CellFormat.VerticalMerge = CellMerge.First;
    builder.Write("Text in merged cells.");

    builder.InsertCell();
    builder.CellFormat.Borders.LineStyle = LineStyle.Single;
    builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
    builder.CellFormat.VerticalMerge = CellMerge.None;
    builder.Write("Text in one cell");
    builder.EndRow();

    builder.InsertCell();
    builder.CellFormat.Borders.LineStyle = LineStyle.Single;
    builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
    // This cell is vertically merged to the cell above and should be empty.
    builder.CellFormat.VerticalMerge = CellMerge.Previous;

    builder.InsertCell();
    builder.CellFormat.Borders.LineStyle = LineStyle.Single;
    builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
    builder.CellFormat.VerticalMerge = CellMerge.None;
    builder.Write("Text in another cell");
    builder.EndRow();

    doc.Save(saveDocFile);
    if (MessageUtil.ShowYesNoAndTips("保存成功，是否打开文件？") == System.Windows.Forms.DialogResult.Yes)
    {
        System.Diagnostics.Process.Start(saveDocFile);
    }
}catch (Exception ex)
{
    LogHelper.Error(ex);
    MessageUtil.ShowError(ex.Message);
    return;
}
```





他的效果如下





![img](https://pic002.cnblogs.com/images/2012/8867/2012082923575470.png)





关于合并单元格的介绍，你还可以参考下这篇官方介绍：http://www.aspose.com/docs/display/wordsnet/Working+with+Merged+Cells



如果上面的例子还不够明白，OK，我在介绍一个实际的例子，来说明合并单元格的操作模式。



实际文档生成如下所示：





![img](https://pic002.cnblogs.com/images/2012/8867/2012083000015288.png)

文档的模板如下所示：

![img](https://pic002.cnblogs.com/images/2012/8867/2012083000103126.png)

其实这个里面的 “测试” 内容是使用代码写入的，其实就是一行业务数据，用两行来展示，其中有些合并的单元格，这是一个实际项目的表格形式。我们注意到，每行有 13 个单元格，其中第一、第二、第十三列是合并列。和并列有一个特点，就是它的两个索引都有效，不过只是能使用第一个索引来对它进行操作复制，利用第二个没有用处的。

如第一个列是和并列，它应该有 0、13 这样的索引，第二列也是和并列，它也有 1、14 的索引，其他的类推。

了解这样的逻辑关系后，我们看实际操作的代码如下所示。

```java
try
                {
                    Aspose.Words.Document doc = new Aspose.Words.Document(templateFile);
                    Aspose.Words.DocumentBuilder builder = new Aspose.Words.DocumentBuilder(doc);
                    
                    List<double> widthList = new List<double>();
                    for (int i = 0; i < 13; i++)
                    {
                        builder.MoveToCell(0, 2, i, 0); //移动单元格
                        double width = builder.CellFormat.Width;//获取单元格宽度
                        widthList.Add(width);
                    }

                    builder.MoveToBookmark("table");        //开始添加值

                    Table table = builder.StartTable();
                    builder.RowFormat.HeadingFormat = true;
                    builder.ParagraphFormat.Alignment = ParagraphAlignment.Center;

                    for (int j = 0; j < 26; j++)
                    {
                        builder.InsertCell();// 添加一个单元格                    
                        builder.CellFormat.Borders.LineStyle = LineStyle.Single;
                        builder.CellFormat.Borders.Color = System.Drawing.Color.Black;
                        int cellIndex = (j > 12) ? (j-13) : j; //位于第几个单元格
                        builder.CellFormat.Width = widthList[cellIndex];
                        builder.CellFormat.VerticalAlignment = CellVerticalAlignment.Center;//垂直居中对齐
                        builder.ParagraphFormat.Alignment = ParagraphAlignment.Center;//水平居中对齐

                        builder.CellFormat.VerticalMerge = Aspose.Words.Tables.CellMerge.None;
                        if (cellIndex == 0 || cellIndex == 1 || cellIndex == 12)
                        {
                            if (j > 12)
                            {
                                builder.CellFormat.VerticalMerge = CellMerge.Previous;
                            }
                            else
                            {
                                builder.CellFormat.VerticalMerge = CellMerge.First;
                            }
                        }
 
                        builder.Write("测试" + j.ToString());
                        if (cellIndex == 12 )
                        {
                            builder.EndRow();
                        }
                    }
                    builder.EndTable();

                    doc.Save(saveDocFile);
                    if (MessageUtil.ShowYesNoAndTips("保存成功，是否打开文件？") == System.Windows.Forms.DialogResult.Yes)
                    {
                        System.Diagnostics.Process.Start(saveDocFile);
                    }
                }
                catch (Exception ex)
                {
                    LogHelper.Error(ex);
                    MessageUtil.ShowError(ex.Message);
                    return;
                }
```

