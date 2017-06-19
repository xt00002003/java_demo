package com.dark.design.chain;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/6/18
 * Time: 18:17
 * description:
 */
public class HandlerMain {
    public static void main(String[] args) throws Exception{
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"classpath:spring/spring-conllection.xml"});

        LinkedList<ProductHandler> productLinkedList=(LinkedList<ProductHandler>)context.getBean("chain");

        List<Product> productList=genProducts();
        for (ProductHandler productHandler:productLinkedList){
            productHandler.showName();
            productHandler.handler(productList);
        }
    }

    public static List<Product> genProducts(){
        List<Product> productList=new ArrayList<>();
        Product apple=new Product();
        apple.setCode("001");
        apple.setName("苹果");

        Product potato=new Product();
        potato.setCode("002");
        potato.setName("土豆");


        productList.add(apple);
        productList.add(potato);


        return productList;
    }
}
