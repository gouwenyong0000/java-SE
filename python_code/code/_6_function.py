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

