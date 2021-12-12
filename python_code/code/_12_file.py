'''
文件的读取与写入
open("路径",mode="开启模式")
绝对路径 ex：C:/Users/hibyby/Desktop/python/123.txt
相对路径 以程序的位置做延伸ex：123.txt
    mode="r"    读取
    mode="w"    覆写
    mode="a"    追加
'''
from os import close, name


file = open("123.txt",mode = "r", encoding='gbk') # 指定文件编码
print(file.readlines())
'''
    read() 读取全部内容   
    readline()读取一行 
    readlines() 按行返回列表
'''
file.close()# 关闭文件


fileW = open("2.txt",mode="w",encoding="utf-8")
fileW.write("hello  write")
fileW.close()

# 自动关闭文件
with  open("2.txt",mode="w",encoding="utf-8") as file:
    file.write("\nhello")