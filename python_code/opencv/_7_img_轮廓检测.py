import cv2
import numpy as np

'''
轮廓检测+ 图形识别
'''

# 轮廓形状检测
img = cv2.imread("../shape.jpg")

imgBak = img.copy()  # 复制一份
img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 转换成灰度图

canny = cv2.Canny(img, 150, 200)  # 计算边缘

contours, hierarchy = cv2.findContours(canny, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)  # 寻找轮廓

for cnt in contours:  # 遍历轮廓集合
    cv2.drawContours(imgBak, cnt, -1, (255, 0, 0), 4)  # 将轮廓图形画在备份的img上

    area = cv2.contourArea(cnt)  # 轮廓的面积
    s_len = cv2.arcLength(cnt, True)  # 轮廓边长

    vertices = cv2.approxPolyDP(cnt, s_len * 0.02, True)  # 返回轮廓顶点
    print(vertices)
    corners = len(vertices)  # 顶点数量

    x, y, w, h = cv2.boundingRect(vertices)  # 用方形将轮廓顶点框住   并返回方形的 顶点 + 长宽
    cv2.rectangle(imgBak, (x, y), (x + w, y + h), (0, 255, 0), 4)  # 将上面方形画出来

    # 显示识别结果
    if corners == 3:
        cv2.putText(imgBak, 'triangle', (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
    elif corners == 4:
        cv2.putText(imgBak, 'rectangle', (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
    elif corners == 5:
        cv2.putText(imgBak, 'pentagon', (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
    elif corners >= 6:
        cv2.putText(imgBak, 'circle', (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

cv2.imshow("img", img)
cv2.imshow("canny", canny)
cv2.imshow("imgBak", imgBak)
cv2.waitKey(1)
