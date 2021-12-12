# 列表   
arr = [70,80,50,100]
frends = ["jack","tom"]

print(arr)#[70, 80, 50, 100]
print(arr[0])# 取出索引值的值   70

print(arr[-1])# 取出倒数第一位的值  100
print(arr[-2])# 取出倒数第二位的值  50

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