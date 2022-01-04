import cv2
import numpy as np

'''
在全黑的图片上画图形
'''

# 创建600 * 600的阵列
img = np.zeros((600, 400,3), np.uint8)
#                   起点          结束点             颜色          粗细
cv2.line(img,       (0, 0),     (400, 600),     (255, 0, 0),    1)  # 划线
cv2.rectangle(img,  (0, 0),     (400, 300),     (0, 255, 0),  3)  # 矩形
#                   圆心              半径      颜色              线条粗细
cv2.circle(img,     (300, 300),     50,     (0, 0, 255),      cv2.FILLED)  # 圆形

cv2.putText(img, "hello" , (100, 100) , cv2.FONT_HERSHEY_SIMPLEX, 5, (255, 255, 255), 1) # 写字 不支持中文

cv2.imshow("img", img)
cv2.waitKey(0)
