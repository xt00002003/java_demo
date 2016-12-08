package com.dark.collection;

import com.google.common.collect.ImmutableSet;


/**
 * Created by darkxue on 8/12/16.
 */
public class ImmutableSetDemo {
    public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
            "red",
            "orange",
            "yellow",
            "green",
            "blue",
            "purple");

    public static void  main(String[] args){
        System.out.println("---------------- old element -----------------");
        for (String entity:COLOR_NAMES){
            System.out.println(entity);
        }
        System.out.println("---------------- new  element -----------------");
        COLOR_NAMES.builder().add("new One");
        for (String entity:COLOR_NAMES){
            System.out.println(entity);
        }

    }
}
