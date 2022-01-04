import cv2
import numpy as np

'''
人脸识别
'''

# 轮廓形状检测
img = cv2.imread("../lenna.jpg")

gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 转换成灰度图

# 读取人脸识别模式数据
faceCascade = cv2.CascadeClassifier("../face_detect.xml")

# 返回识别到人脸的矩形框集合
faceRect = faceCascade.detectMultiScale(gray, 1.1, 3)

print(len(faceRect))  # 打印

for (x, y, w, h) in faceRect:
    cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2) # 框处人脸


cv2.imshow("result",img)
cv2.waitKey(1)
