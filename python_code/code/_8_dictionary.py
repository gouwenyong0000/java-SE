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