package com.dark.mybatis.dao;

import com.dark.mybatis.domain.Supplier;
import com.dark.mybatis.plugin.PageParams;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("supplierMapper")
public interface SupplierMapper {
    int deleteByPrimaryKey(Long sid);

    int insert(Supplier record);

    int insertSelective(Supplier record);

    Supplier selectByPrimaryKey(Long sid);

    List<Supplier>  selectPagedSupplierList(PageParams pageParams);

    int updateByPrimaryKeySelective(Supplier record);

    int updateByPrimaryKey(Supplier record);
}