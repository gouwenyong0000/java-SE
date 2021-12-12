print("hello world")
print("hello \n world")

# "hello world"  输出引号  可以外层单引号 或者\" 转义
print('"hello world"')

# 连接字符串  + 
name = "hello"
age = 17
print(name + " world")#hello world

# 返回字符串索引位置的值  0开始
print(name[0])#h
print(name[3:])#lo
print(name[:2])#he
print(name[0:2])#he
# 返回h在目标字符串的第一次出现位置
print(name.index("h"))#0

# 将变量中l  替换成 L
print(name.replace("l","L"))#heLLo

'''
字符串常用函数function
    lower()   转换小写
    upper()   转换大写
    isupper()  判断字符串都是大写
    islower()  判断字符串都是小写
'''
print(name.lower())#hello
print(name.upper())#HELLO
print(name.isupper())#False


print("name:{0} age:{1}".format("jack",20))# 格式化数据