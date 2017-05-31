package com.dark.collection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
        List<String> params=new ArrayList<>();
        params.add("a");
        params.add("a");
        params.add("b");
        params.add("a");
        Set<String> result= Sets.newHashSet(params);
        System.out.println("---------------- the result is: -----------------"+result);

    }
}
