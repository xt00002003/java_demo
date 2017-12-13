package com.dark.thread;

public class TestExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("线程出现异常：");
        e.printStackTrace();
    }
}


