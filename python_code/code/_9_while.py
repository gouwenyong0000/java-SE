# while 循环
i = 1
while i <= 5:
    print( id(i))
    i=i+1

#--------------------------------------------------
# 猜数字  三次机会
#--------------------------------------------------
flag = 50
i = None
count = 0
while  not (i == flag) and count < 3 :
   i = int(input("输入一个数字")) 
   if  i>flag:
       print("小一点")
   else:
        print("大一点")
   count = count+1 # 更新统计次数

if(count<= 3 ):
    print("win")     
else:
    print("fail")
