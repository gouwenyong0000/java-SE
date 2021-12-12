import os
cmd = 'ping 127.0.0.1 -t'
res = os.popen(cmd)
# output_str = res.read()   # 获得输出字符串

while True:
    output_str = res.readline()   # 获得输出字符串
    if(output_str ==""):
        break
    else:
        print(output_str)
