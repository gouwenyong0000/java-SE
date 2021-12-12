# 问答程序
from question  import Question  # 从question模块只引入Question类

test = [
    "1+3=?\n (a) 2 \n (b) 3 \n (c) 4 \n (d) 5 \n\n",
    "1公尺等於幾公分？\n（a）10\n（b）100\n（c）1000\n\n",
    "香蕉是什麼顏色？\n（a）黑色\n（b）黄色\n（c）白色\n\n"
]

# 问题和答案  列表
questions = [
    Question(test[0],"c"),
    Question(test[1],"b"),
    Question(test[2],"b")
]

# 定义测试函数 
def run_test(questions):
    score = 0
    for ques in questions:
       anwer =  input(ques.des)
       if(anwer == ques.answer):
           score+=1
    return score

score  = run_test(questions)
print("你得到{0} / {1}".format(score,len(questions)))