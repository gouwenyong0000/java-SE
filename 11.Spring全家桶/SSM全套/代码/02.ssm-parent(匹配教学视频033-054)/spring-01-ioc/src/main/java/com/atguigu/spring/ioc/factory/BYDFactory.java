package com.atguigu.spring.ioc.factory;

import com.atguigu.spring.ioc.bean.Car;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;



// 场景：如果制造某些对象比较复杂的时候，利用工厂方法进行创建。
@Component
public class BYDFactory implements FactoryBean<Car> {


    /**
     * 调用此方法给容器中制造对象
     * @return
     * @throws Exception
     */
    @Override
    public Car getObject() throws Exception {
        System.out.println("BYDFactory 正在制造Car对象...");
        Car car = new Car();
        return car;
    }


    /**
     * 说明造的东西的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }


    /**
     * 是单例？
     *    true：是单例的；
     *    false：不是单例的；
     * @return
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
