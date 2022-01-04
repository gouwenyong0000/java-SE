import cv2

# pip install opencv-python

# 读取文件
img = cv2.imread("../colorcolor.jpg")

# 修改图片大小
# img = cv2.resize(img,(300,300))
img = cv2.resize(img, (0, 0), fx=0.5, fy=0.5)  # 等比缩放 fx长度方向 fy宽度方向

# 窗口显示图片

cv2.imshow("img", img)  # 标题  图片

# 等待键盘输入
key = cv2.waitKey(100)  # 超时时间  0 不超时


