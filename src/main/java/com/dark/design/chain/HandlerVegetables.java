package com.dark.design.chain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/6/18
 * Time: 18:13
 * description:
 */
public class HandlerVegetables implements ProductHandler{
    @Override
    public void handler(List<Product> products) {
        List<Product> needMove=new ArrayList<>();
        for (Product product:products){
            if (product.getCode().equals("002")){
                System.out.println("处理类型是002的数据:"+product.getName());
                needMove.add(product);
            }
        }

        products.removeAll(needMove);
    }

    @Override
    public void showName() {
        System.out.println("002处理类");
    }
}
