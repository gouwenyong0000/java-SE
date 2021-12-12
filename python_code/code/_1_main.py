# 单行注释

'''
三个单引号  多行注释
'''


# 字符串  单引号 或者双引号
"string"
'string'

# 数字 
12
12.0

# 布尔值
True
False

''' 
变量
    变量名构成：字母 数字 _
    不能是数字开头
'''
name = "jack"
age = 17
is_male = True

'''
错误：can only concatenate str (not "int") to str；
上网查过后发现是因为我没有做数据类型的转换，python并不能像java一样，在做拼接的时候自动把类型转换为string类型;
故而需要进行一个类型转换，譬如将print(1+"a")改为print(str(1)+"a")就可以了；
'''
print(name + str(age) + str(is_male))
