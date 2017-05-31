package com.dark.util.entity;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/31
 * Time: 17:03
 * description:
 */
public class Product {
    private Long id;
    private String name;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
