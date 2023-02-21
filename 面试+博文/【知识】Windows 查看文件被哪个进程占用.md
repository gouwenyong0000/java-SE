经常当我们删除文件时，有时会提示【操作无法完成，因为文件已在另一个程序中打开，请关闭该文件并重试】，到底是哪些程序呢？

有时候一个一个找真不是办法，已经被这个问题折磨很久了，今天下决心要把它解决，找到办法了。如果系统是 win7，可以这么做：

在开始菜单中的搜索框内输入 “**资源监视器**”，回车，打开 “资源监视器”。

看下图，在 “资源监视器” 界面中，点击第二个选项卡 “CPU”。在“**关联的句柄**” 右侧搜索框内输入文件名称，点击右侧下拉箭头，就可以查看该文件被那几个程序占用了。

![](https://s2.51cto.com/images/blog/202107/14/555b0589b5ab42f5d93cc7937dc9e234.jpeg?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_30,g_se,x_10,y_10,shadow_20,type_ZmFuZ3poZW5naGVpdGk=/format,webp/resize,m_fixed,w_1184)

选中程序，右击选择结束进程。

![](https://s2.51cto.com/images/blog/202107/14/fd37cca0d22a86c1217d40bb2a270515.jpeg?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_30,g_se,x_10,y_10,shadow_20,type_ZmFuZ3poZW5naGVpdGk=/format,webp/resize,m_fixed,w_1184)

现在就可以删除文件了。结束系统进程前最好查一下，看看能不能结束，免得出现问题，那就得不偿失了。

**知道进程 ID 如果杀不死可以通过 DOS 命令杀死进程，用 `taskkill -pid  4444 -f` 杀死进程。**

