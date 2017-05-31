package com.dark.util.entity;

import org.dozer.Mapping;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/31
 * Time: 17:46
 * description:
 */
public class ProductDTO {
    @Mapping("id")
    private long productId;
    @Mapping("name")
    private String productName;
    @Mapping("description")
    private String desc;


}
