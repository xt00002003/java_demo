package com.dark.util.entity;

import java.util.Date;
import java.util.List;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/31
 * Time: 17:07
 * description:
 */
public class Commodity {


    private Long id;
    private String name;
    private String description;
    private Date date;

    private Product product;
    private List<Bom> bomList;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Bom> getBomList() {
        return bomList;
    }

    public void setBomList(List<Bom> bomList) {
        this.bomList = bomList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
