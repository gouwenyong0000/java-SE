package com.atguigu.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

public class WebBeanUhtils {

    public static <T> void copyParamToBean(HttpServletRequest httpServletRequest,T bean){

        try {
            System.out.println("注入之前：" + bean.toString());
            BeanUtils.populate(bean,httpServletRequest.getParameterMap());
            System.out.println("注入之后：" + bean.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
