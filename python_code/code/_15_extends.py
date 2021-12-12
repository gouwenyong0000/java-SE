from student import Student


stu1 = Student("jack",18,"小学")
print(stu1.name)# 访问父类属性
print(stu1.school)#访问子类属性
stu1.print_name()# 访问父类方法
stu1.print_school()# 访问子类方法
