package com.example.downupload;

/**

测试动态绑定
*/
public class Test {
    public static void main(String[] args) {
        Father son =  new Son();
    }
}

class Father {

    Father() {
        super();//2、调用父类Object的构造器
        this.toString();//3、调用成员方法，运行时动态绑定，去找new Son（）这个对象的成员方法，发现找到了，则执行。找不到，根据class类对象的文件元数据信息，则向上找超类的方法
    }

    @Override
    public String toString() {
        return "Father";
    }
}

class Son extends Father {
    String name;

    Son() {
        super();//1、调用父类构造器
        name = "Son";
    }

    @Override
    public String toString() {
        return name.toUpperCase();//4、调用name.toUpperCase()的方法，但是name此时还是默认初始化，值为null，所以报空指针异常
    }
}
