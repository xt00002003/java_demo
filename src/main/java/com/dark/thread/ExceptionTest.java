package com.dark.thread;

public class ExceptionTest {
    public static void main(String[] args){
        Thread t=new Thread(){
            public void run(){
                Integer.parseInt("ABC");
            }
        };
        t.setUncaughtExceptionHandler(new TestExceptionHandler());
        t.start();
    }
}
