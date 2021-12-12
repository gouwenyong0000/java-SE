
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
