import cv2
import numpy as np

'''
常用函数
'''

# 读取图片
img = cv2.imread("../colorcolor.jpg")
# 等比缩放图片
img = cv2.resize(img, (0, 0), fx=0.5, fy=0.5)

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 灰阶图片

gauss = cv2.GaussianBlur(img, (7, 7), 0)  # 高斯模糊

canny = cv2.Canny(img, 150, 200)  # 轮廓 得分 在150-200

kernel = np.ones((3, 3), np.uint8)
dilate = cv2.dilate(canny, kernel, iterations=1)  # 线条变粗

kernel1 = np.ones((3, 3), np.uint8)
erode = cv2.erode(dilate, kernel1, iterations=1)  # 线条变细

# cv2.imshow("img", img)
# cv2.imshow("gray", gray)
# cv2.imshow("gauss", gauss)
cv2.imshow("canny", canny)
cv2.imshow("dilate", dilate)
cv2.imshow("erode", erode)

cv2.waitKey(0)
