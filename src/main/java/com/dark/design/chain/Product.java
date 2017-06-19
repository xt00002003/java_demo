package com.dark.design.chain;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/6/18
 * Time: 18:12
 * description:
 */
public class Product {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
