from person import Person # 从person文件中引入Person类

# Student继承Person类
class Student(Person):

    def __init__(self, name, age,school):
        super().__init__(name, age)
        self.school = school
        
    def print_school(self):
        print(self.school)