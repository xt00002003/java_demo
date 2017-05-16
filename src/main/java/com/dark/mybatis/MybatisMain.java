package com.dark.mybatis;

import com.dark.mybatis.dao.SupplierMapper;
import com.dark.mybatis.domain.Supplier;
import com.dark.mybatis.plugin.PageParams;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

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

//        Supplier supplier= supplierMapper.selectByPrimaryKey(4590L);

        PageParams pageParams=new PageParams();
        pageParams.setPage(1);
        pageParams.setPageSize(10);
        pageParams.setUseFlag(true);
        List<Supplier>  supplierList=supplierMapper.selectPagedSupplierList(pageParams);
        System.out.println("-------------- ok ----------------");


    }
}
