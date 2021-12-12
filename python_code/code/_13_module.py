#引入 tool.py
import sys

print(sys.path)# 打印python引入模组的路径

import tool 

print(tool.age)# 调用值
print(tool.max_num(1,5,10))# 调用函数

# pip套件管理工具
# 命令行执行：pip install numpy
import numpy as np # 自定义别名

