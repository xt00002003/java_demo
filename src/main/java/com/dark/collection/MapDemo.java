package com.dark.collection;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by darkxue on 8/12/16.
 * put方法的过程：具有相同的hashcode的key会存放到同一个链表中，
 * 最后放进去的值会存储在第一个。对于相同的hashcode并且key也相同的话，
 * 则会更新原来的value。
 */
public class MapDemo {
    public static void  main(String[] args){
        Map<String,String> map=new HashedMap();
        String a="a";
        int hashcode=a.hashCode();
        map.put("a","a");
        map.put("a","abc");
        map.put("a","abcd");
        map.put("b","abc");
        System.out.println(map.size());
        for (Map.Entry entry: map.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
