### Lua

Lua 是由巴西里约热内卢天主教大学（Pontifical Catholic University of Rio de Janeiro）里的一个研究小组于1993年开发的一种轻量、小巧的脚本语言，用标准 C 语言编写，其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。

官网：http://www.lua.org/

### IDE

#### EmmyLua插件

https://github.com/EmmyLua/IntelliJ-EmmyLua

https://emmylua.github.io/zh_CN/

#### LDT 基于eclipse

https://www.eclipse.org/ldt/

### Lua基础语法

#### hello world

```lua
print("hello world!")
```



#### 保留关键字

`and`       `break`     `do   ` `else`      `elseif`      `end`       `false`    ` for`       `function  if`      `in`        `local`     `nil`      ` not `      `or`      `repeat`    `return`    `then`     ` true`      `until`    ` while`

#### 注释

```lua
-- 两个减号是行注释

--[[

 这是块注释

 这是块注释.

 --]]
```



#### 变量

##### 数字类型

Lua的数字只有double型，64bits

你可以以如下的方式表示数字

 ```lua
num = 1024

num = 3.0

num = 3.1416

num = 314.16e-2

num = 0.31416E1

num = 0xff

num = 0x56
 ```

##### 字符串

可以用单引号，也可以用双引号

也可以使用转义字符‘\n’ （换行）， ‘\r’ （回车）， ‘\t’ （横向制表）， ‘\v’ （纵向制表）， ‘\\’ （反斜杠）， ‘\”‘ （双引号）， 以及 ‘\” （单引号)等等

下面的四种方式定义了完全相同的字符串（其中的两个中括号可以用于定义有换行的字符串）

```lua
a = 'alo\n123"'

a = "alo\n123\""

a = '\97lo\10\04923"'

a = [[alo

123"]]

local age = 10
print("Your age is "..age)  -- ..表示字符串和变量拼接
```





##### 空值

C语言中的NULL在Lua中是nil，比如你访问一个没有声明过的变量，就是nil

##### 布尔类型

只有nil和false是 false

数字0，‘’空字符串（’\0’）都是true

##### 作用域

lua中的变量如果没有特殊说明，默认全是全局变量，那怕是语句块或是函数里。

变量前加local关键字的是局部变量。

#### 控制语句

##### while循环

```lua
local i = 0
local max = 10

while i <= max do
    print(i)
    i = i +1
end
```





##### if-else

```lua
local function main()
local age = 35

local sex = 'Male'
 
if age == 40 and sex =="Male" then
    print(" 男人四十一枝花 ")

  elseif age > 60 and sex ~="Female" then
    print("old man!!")

  elseif age < 20 then
    io.write("too young, too simple!\n")
  
  else
    print("Your age is "..age)  -- ..表示字符串和变量拼接

  end

end

main()-- 调用
print("jack".."\t\t -->")

--[[
Your age is 35
jack		 -->
]]
```



##### for循环

```lua
sum = 0  --未标记local 表示全局变量

for i = 100, 1, -2 do -- in(start end)  step
    print(i)
	sum = sum + i

end
print(sum)

--[[
100
98
...
8
6
4
2
2550
]]
```





##### 函数

1.

```lua
function myPower(x,y)

  return      y+x

end

power2 = myPower(2,3)

 print(power2)
```





2.

```lua
function newCounter()

   local i = 0
   return function()     -- anonymous function

        i = i + 1

        return i

    end
end

 

c1 = newCounter()

print(c1())  --> 1

print(c1())  --> 2

print(c1())	--> 3
```





#### 返回值

赋值

```lua
name, age,bGay = "yiming", 37, false, "yimingl@hotmail.com"

print(name,age,bGay) -- yiming	37	false
```





 函数返回多个值

 ```lua
function isMyGirl(name)
  return name == 'xiao6' , name --返回两个值
end

local bol,name = isMyGirl('xiao6')

print(name,bol)--xiao6	true
 ```





#### Table

key，value的键值对 类似 map

```lua
local function main()
    dog = {name='111',age=18,height=165.5}

    dog.age=35

    print(dog.name,dog.age,dog.height)

    print(dog)
end
main()
--打印：
--111	35	165.5
--table: 0x63e6d0
```





#### 数组

```lua
local function main()
arr = {"string", 100, "dog",function() print("wangwang!") return 1 end}

print(arr[4]())
end
main()

```





#### 遍历

```lua
arr = {"string", 100, "dog",function() print("wangwang!") return 1 end}

for k, v in pairs(arr) do

   print(k, v)
end

--[[
1	string
2	100
3	dog
4	function: 0x63e6e0
]]
```



 

#### 成员函数

```lua
local function main()

	person = {name='旺财',age = 18}

     function  person.eat(food)

        print(person.name .." eating "..food)
     end
	person.eat("骨头")
end
main()

```

## 