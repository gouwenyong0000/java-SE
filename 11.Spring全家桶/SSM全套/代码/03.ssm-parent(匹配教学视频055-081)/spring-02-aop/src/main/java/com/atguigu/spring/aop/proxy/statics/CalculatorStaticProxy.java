package com.atguigu.spring.aop.proxy.statics;

import com.atguigu.spring.aop.calculator.MathCalculator;
import lombok.Data;
import org.springframework.stereotype.Component;


/**
 * 静态代理：   编码时期间就决定好了代理的关系
 *    定义：代理对象，是目标对象的接口的子类型，代理对象本身并不是目标对象，而是将目标对象作为自己的属性。
 *    优点：同一种类型的所有对象都能代理
 *    缺点：范围太小了，只能负责部分接口代理功能
 * 动态代理：   运行期间才决定好了代理关系（拦截器：拦截所有）
 *    定义：目标对象在执行期间会被动态拦截，插入指定逻辑
 *    优点：可以代理世间万物
 *    缺点：不好写
 *
 */
@Data
public class CalculatorStaticProxy implements MathCalculator {

    private MathCalculator target; //目标对象

    public CalculatorStaticProxy(MathCalculator mc){
        this.target = mc;
    }


    @Override
    public int add(int i, int j) {
        System.out.println("【日志】add 开始：参数："+i+","+j);
        int result = target.add(i, j);
        System.out.println("【日志】add 返回：结果："+result);
        return result;
    }

    @Override
    public int sub(int i, int j) {
        int result = target.sub(i,j);
        return result;
    }

    @Override
    public int mul(int i, int j) {
        int result = target.mul(i,j);
        return result;
    }

    @Override
    public int div(int i, int j) {
        int result = target.div(i,j);
        return result;
    }
}
