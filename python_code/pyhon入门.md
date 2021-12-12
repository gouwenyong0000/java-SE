# pyhon入门

## 基本数据类型与变量

```python
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

```

## string字符串

```python
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
```

## 数字Number

```python
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
```

练习：建立基本的计算机

```python
# 建立计算机

# input(msg)  显示提示信息  接受控制台输入  返回字符串给变量
number1 = input("请输入第一个数字")
number2 = input("请输入第二个数字")

print(type(number1))# 返回number1类型
print( int(number1) + float(number2))  # int()  将字符串转换成整数 float() 将字符串转换成浮点数

'''
请输入第一个数字5
请输入第二个数字6.3
11.3
'''
```

## 列表List

```python
# 列表   
arr = [70,80,50,100]
frends = ["jack","tom"]

print(arr)#[70, 80, 50, 100]
print(arr[0])# 取出索引值的值   70

print(arr[-1])# 取出倒数第一位的值  100
print(arr[-2])# 取出倒数第二位的值  50

# 切片
print(arr[0:3])# 取出0 - 3 的值  [70, 80, 50]
print(arr[2:])# 取出2 到结束  [50, 100]
print(arr[:2])# 取出开始到 第三位  [70, 80]

# 可以扩展到字符串 [[n]:[m]]


# 修改值
arr[0] = -1
print (arr)#[-1, 80, 50, 100]

'''
函数：
'''
arr.extend(frends)# 将frends列表连接到arr后面   
print(arr)#[-1, 80, 50, 100, 'jack', 'tom']

arr.append(1)# 在列表后面追加一个值  
print(arr)#[-1, 80, 50, 100, 'jack', 'tom', 1]

arr.insert(1,"插入值")# 在索引为1 的位置插入 字符串  其余顺序后移
print(arr)#[-1, '插入值', 80, 50, 100, 'jack', 'tom', 1]


arr.remove('插入值')# 删除列表在90的值
print(arr)#[-1, 80, 50, 100, 'jack', 'tom', 1]

arr.pop()# 移除列表最后一个值
print(arr)#[-1, 80, 50, 100, 'jack', 'tom']

arr.clear()#清空列表
print(arr)#[]

arr = [1,6,0,4,6,9]
arr.sort()# 排序 正序
print(arr)#[0, 1, 4, 6, 6, 9]

arr.reverse()# 反转列表
print(arr)#[9, 6, 6, 4, 1, 0]

print(arr.index(6))# 返回0出现的位置  1
print(arr.count(6))# 统计 6 出现的次数  2

print(len(arr))# 返回列表的长度   6
```

## 元组tuple

```python
scores = (90,80,60,70,50)

print(scores[0]) #90
print(scores[0:2]) #(90, 80)

print(len(scores))# 返回元组的长度   5

'''
与列表的差异：不能进行新增、修改、删除

用途：用于不可修改的数据定义
'''
```

## 函数function

```python
# 定义函数
def hello(name,age):
    print("hello : " + name + "\t年龄" + str(age)) 

# 调用函数
hello("jack",10) #hello : jack    年龄10


# =====================================================
def sum(num1,num2):
    # print(str(num1) + " + " + str(num2) +" = " +str (num1 + num2))
    return num1 + num2

sumNum = sum( 1 ,10) #hello : jack    年龄10
print(sumNum)# 11

#如果函数没有返回值   解释器默认返回None
```

## if语句

```python
value = input("输入一个数")# 返回的字符串

num = int(value) # 转换成数字
if num >= 50:
    print("不小于50")
elif (num < 50 and num > 0):
    print("小于50")
else:
    print("数据不合格")


'''
运算符优先级-----------
幂运算      **
正负号      + - 
算数运算    + - * / // 
比较运算符  < <= > >= == !=
逻辑运算符  and or not
'''

'''
返回最大值
【结合函数  if语句】
'''
def max_num(num1,num2,num3):
    if num1> num2 and num1>num3 :
        return num1
    elif num2> num1 and num2>num3 :
        return num2
    else:
        return num3

print(max_num(5,10,100))
```

```python
# 进阶 建立计算机

num1 = float(input("请输入第一个数"))
op = input("请输入运算符号")
num2 = float(input("请输入第2个数"))

if(op == "+" ):
    print (num1 + num2)
elif op=="-":
    print(num1 - num2)
elif op=="*":
    print(num1 * num2)
elif op=="/":
    print(num1 / num2)
else:
    print("erro")
```

## 字典dictionary

```python
'''
key     value
键      值

'''

dic = {"苹果":"apple","香蕉":"banana","猫":"cat"}

print(dic)#{'苹果': 'apple', '香蕉': 'banana', '猫': 'cat'}
print(dic["苹果"])# 查字典  apple

del dic['苹果']  # 删除键是'苹果'的条目
dic.clear()      # 清空字典所有条目
del dic          # 删除字典
```

## while循环

```python
# while 循环
i = 1
while i <= 5:
    print( id(i))
    i=i+1

#--------------------------------------------------
# 猜数字  三次机会
#--------------------------------------------------
flag = 50
i = None
count = 0
while  not (i == flag) and count < 3 :
    i = int(input("输入一个数字")) 
    if  i>flag:
        print("小一点")
    else:
        print("大一点")
    count = count+1 # 更新统计次数

if(count<= 3 ):
    print("win")     
else:
    print("fail")
```

## for循环

```python
# for循环

'''
for 变量 in 字符串or列表:
    函数体
'''


for letter in "小白你好":
    print(letter)

'''输出
小
白
你
好
'''
for i in [1,2,3,4,5]:
    print(i)
'''输出
1
2
3
4
5
'''

sum = 0
for i in range(1,5):   # 等价于[1,2,3,4]
    sum += i
print(sum) #10   = 1+2+3+4

# 自定义幂函数 ----------------
def power(num,m):
    res = 1
    for i in range( m ): # 循环m次
        res  = res * num

    return res
# 调用    
print(power(2,5))
```

## 二维列表

```python
# 二维列表

# row 行 col列
nums = [
    [0,1,2],
    [3,4,5],
    [6,7,8],
]

# 类似于java的foreach
for row in nums:
    for col in row:
        print(col)
```

## 文件的读取写入

```python
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
```

## 模组module

```python
#引入 tool.py
import sys

print(sys.path)# 打印python引入模组的路径

import tool 

print(tool.age)# 调用值
print(tool.max_num(1,5,10))# 调用函数

# pip套件管理工具
# 命令行执行：pip install numpy
import numpy as np # 自定义别名
```

## 类class

```python

# 定义class  模板
class Phone:
    # self 表示本身this
    def __init__(self,os,number):
        self.os  = os
        self.number  = number

    def print(self):
        print(self.os + str(self.number))

    def is_ios(self):
        return self.os == "ios"


# 创建对象
phone1 = Phone("ios",123)
print(phone1.os)
print(phone1.number)# 调用类的属性
phone1.print()# 调用类的方法
print(phone1.is_ios())# 调用类的方法
'''结果
ios
123
ios123
True
'''
```

## 继承extends

person.py

```python
class Person:
    def __init__(self,name,age):
        self.name = name
        self.age = age

    def print_name(self):
        print(self.name)
        
    def print_age(self):
        print(self.age)
```

student.py

```python
from person import Person # 从person文件中引入Person类

# Student继承Person类
class Student(Person):

    def __init__(self, name, age,school):
        super().__init__(name, age)
        self.school = school
        
    def print_school(self):
        print(self.school)
```

_15_extends.py

```python
from student import Student

stu1 = Student("jack",18,"小学")
print(stu1.name)# 访问父类属性
print(stu1.school)#访问子类属性
stu1.print_name()# 访问父类方法
stu1.print_school()# 访问子类方法
```

## 练习：问答程序

question.py

```python
class Question:
    '''
        des:问题描述
        answer：问题答案
    '''
    def __init__(self,des,answer) :
        self.des = des
        self.answer = answer
```

pratice.py

```python
# 问答程序
from question  import Question  # 从question模块只引入Question类

test = [
    "1+3=?\n (a) 2 \n (b) 3 \n (c) 4 \n (d) 5 \n\n",
    "1公尺等於幾公分？\n（a）10\n（b）100\n（c）1000\n\n",
    "香蕉是什麼顏色？\n（a）黑色\n（b）黄色\n（c）白色\n\n"
]

# 问题和答案  列表
questions = [
    Question(test[0],"c"),
    Question(test[1],"b"),
    Question(test[2],"b")
]

# 定义测试函数 
def run_test(questions):
    score = 0
    for ques in questions:
        anwer =  input(ques.des)
        if(anwer == ques.answer):
            score+=1
    return score

score  = run_test(questions)
print("你得到{0} / {1}".format(score,len(questions)))
```

