import cv2
import numpy as np

'''
侦测颜色
'''


def empty(count):
    pass


# 读取并缩放图片
img = cv2.imread("../XiWinnie.jpg")
img = cv2.resize(img, (0, 0), fx=0.5, fy=0.5)

hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)  # 转换成HSV格式 色调 饱和度 亮度

# 创建空白视窗  取色器
cv2.namedWindow("TrackBar")
cv2.resizeWindow("TrackBar", 640, 320)
cv2.createTrackbar("Hue min", "TrackBar", 0, 179, empty)  # 创建一个控制条 0-179  empty为回调函数
cv2.createTrackbar("Hue max", "TrackBar", 179, 179, empty)
cv2.createTrackbar("Sat min", "TrackBar", 0, 255, empty)
cv2.createTrackbar("Sat max", "TrackBar", 255, 255, empty)
cv2.createTrackbar("Val min", "TrackBar", 0, 255, empty)
cv2.createTrackbar("Val max", "TrackBar", 255, 255, empty)

while True:
    # 读取HSV取色器的值
    h_min = cv2.getTrackbarPos("Hue min", "TrackBar")
    h_max = cv2.getTrackbarPos("Hue max", "TrackBar")
    s_min = cv2.getTrackbarPos("Sat min", "TrackBar")
    s_max = cv2.getTrackbarPos("Sat max", "TrackBar")
    v_min = cv2.getTrackbarPos("Val min", "TrackBar")
    v_max = cv2.getTrackbarPos("Val max", "TrackBar")
    print(h_min, h_max, s_min, s_max, v_min, v_max)

    # 最小/大值数组
    lower = np.array([h_min, s_min, v_min])
    upper = np.array([h_max, s_max, v_max])

    # 过滤不在范围内
    mask = cv2.inRange(hsv, lower, upper)

    result = cv2.bitwise_and(img, img, mask=mask) # 把 第一个参数图片与第二个参数图片与运算   最后套上mask遮罩
    # 显示
    cv2.imshow("img", img)
    cv2.imshow("hsv", hsv)
    cv2.imshow("mask", mask)
    cv2.imshow("result", result)

    cv2.waitKey(1)
