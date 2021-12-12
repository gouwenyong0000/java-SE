# 二维列表

# row 行 col列
nums = [
    [0,1,2],
    [3,4,5],
    [6,7,8],
]

# 类似于java的foreach
for row in nums:
    for col in row:
        print(col)
    print("\n")