import cv2

# 读取视频
capture = cv2.VideoCapture("../thumb.mp4")
# capture = cv2.VideoCapture(0) # 获取摄像头画面  0表示摄像头编号


# 如果存在下一帧  则进行显示   可以用while循环 进行快速播放
while True:
    # 读取下一帧    ret 是否存在下一帧  frame下一帧图片
    ret, frame = capture.read()

    if ret:
        frame = cv2.resize(frame, (0, 0), fx=0.4, fy=0.4) # 等比缩放图片

        findCor(frame)
        cv2.imshow("video", frame)
    else:
        break
    if cv2.waitKey(10) == ord('q'):  # 如果键盘输入q 则退出
        break


