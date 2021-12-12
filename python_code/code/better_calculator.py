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