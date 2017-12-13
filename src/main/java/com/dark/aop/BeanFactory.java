package com.dark.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * project_name: java_demo
 * package: com.dark.aop
 * describe: TODO
 * creat_user: dark.xue
 * creat_date: 2017/11/27
 * creat_time: 22:34
 **/
public class BeanFactory {

    public static Object getBean(String className) throws Exception{
        Object obj=Class.forName(className).newInstance();
        InvocationHandler handler=new AOPHandler(obj);
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),handler);
    }

    public static <T> T getBaen(String className, Class<T> clazz)throws Exception{
        return (T) getBean(className);
    }

    public static void main(String[] args) throws Exception{
        Subject subject=BeanFactory.getBaen("com.dark.aop.RealSubject",Subject.class);
        subject.doSomething("a","b","c");
    }
}
