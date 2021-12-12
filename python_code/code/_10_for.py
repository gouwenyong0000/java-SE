# for循环

'''
for 变量 in 字符串or列表:
    函数体
'''


for letter in "小白你好":
    print(letter)

'''
小
白
你
好
'''
for i in [1,2,3,4,5]:
    print(i)
'''
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