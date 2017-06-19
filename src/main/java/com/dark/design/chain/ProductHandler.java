package com.dark.design.chain;

import java.util.LinkedList;
import java.util.List;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/6/18
 * Time: 18:11
 * description:
 */
public interface ProductHandler {
    void  handler(List<Product> products);
    void showName();
}
