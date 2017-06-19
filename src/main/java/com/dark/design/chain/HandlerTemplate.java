package com.dark.design.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/6/18
 * Time: 18:35
 * description:
 */
public  abstract class HandlerTemplate implements ProductHandler {



    @Override
    public final void handler(List<Product> products) {
        List<Product> needMove=new ArrayList<>();
        for (Product product:products){
            if (checkHandler(product)){
                System.out.println("处理类型是002的数据:"+product.getName());
                handler(product);
                needMove.add(product);
            }
        }

        products.removeAll(needMove);
        //回收内存
        needMove=null;
    }

   protected abstract boolean checkHandler(Product product);
   protected abstract void handler(Product product);



}
