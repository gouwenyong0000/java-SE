import cv2
import numpy as np
import random

img = cv2.imread("../colorcolor.jpg")

# cv2.imshow("原图",img)
# cv2.waitKey(0)

# print(img)# 打印阵列        长      宽  RGB
print(img.shape) #图片的尺寸 (1015, 700, 3)
size = img.shape
# 图片反转
# for row in range(size[0]):
#     for col in range(size[1]):
#         img[row][col] = [255 - img[row][col][0],255 - img[row][col][1],255 - img[row][col][2]]
#
# cv2.imshow("反转后图片",img)
# cv2.waitKey(0)

# 切割图片   裁剪坐标为[y0:y1, x0:x1]  高  宽
newimg = img[100:200,500:700]
cv2.imshow("原图",img)
cv2.imshow("切割的图片",newimg)
cv2.waitKey(0)

# 通过阵列创建图片
# 创建长300 宽200 每一个点三种三色  每个点取值范围0-255
img = np.empty((300, 200, 3), np.uint8)

# 填充阵列
for row in range(300):
    for col in range(200):
        img[row][col] = [255, 0, 0]  # B G R

cv2.imshow("手动创建阵列的图片", img)
cv2.waitKey(0)
