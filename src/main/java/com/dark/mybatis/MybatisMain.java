package com.dark.mybatis;

import com.dark.mybatis.dao.SupplierMapper;
import com.dark.mybatis.domain.Supplier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * java_demo
 * User: dark xue
 * Date: 2017/5/6
 * Time: 13:20
 * description:
 */
public class MybatisMain {

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"classpath:spring/spring-mybatis.xml"});

        SupplierMapper supplierMapper=(SupplierMapper)context.getBean("supplierMapper");

        Supplier supplier= supplierMapper.selectByPrimaryKey(4590L);

        System.out.println("-------------- ok ----------------");


    }
}
