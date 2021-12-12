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