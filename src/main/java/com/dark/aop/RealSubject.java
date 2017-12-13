package com.dark.aop;

/**
 * project_name: java_demo
 * package: com.dark.aop
 * describe: TODO
 * creat_user: dark.xue
 * creat_date: 2017/11/27
 * creat_time: 22:16
 **/
public class RealSubject implements Subject2,Subject {

    public RealSubject(){
        System.out.println( "构造方法执行" );
    }

    @Override
    public void doSomething(String... things) {

        System.out.println( "call doSomething()" );
        for (String thing: things){
            System.out.println( "传入的参数是:"+thing );
        }
        callMe();
    }

    public void  callMe(){
        System.out.println( "======================内部来调用我======================" );
    }
}
