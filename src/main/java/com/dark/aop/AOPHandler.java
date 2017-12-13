package com.dark.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * project_name: java_demo
 * package: com.dark.aop
 * describe: 动态代理类
 * creat_user: dark.xue
 * creat_date: 2017/11/27
 * creat_time: 22:17
 **/
public class AOPHandler implements InvocationHandler {

    private Object target;

    public AOPHandler(Object target){
        this.target=target;
    }

    public void println(String str, Object... args){
        System.out.println(str);
        if (args==null){
            System.out.println("\t 未传入任何值....");
        }else {
            for (Object obj: args){
                System.out.println(obj);
            }
        }
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("\n\n =====> 调用方法名："+ method.getName());

        Class<?>[] variabale=method.getParameterTypes();

        System.out.println("\n\n =====> 参数类型列表：");
        for (Class<?> typevariable: variabale){
            System.out.println("\t\t\t "+ typevariable.getName());

        }

        System.out.println("\n\n\t =====> 传入参数的值为：");

        for (Object arg: args){
            System.out.println("\t\t\t "+ arg);
        }

        Object result=method.invoke(target,args);
        println("方法声明的类：",method.getDeclaringClass().getName());
        println("返回的参数为：",result);
        println("返回值的类型是：",method.getReturnType());
        return result;
    }
}
