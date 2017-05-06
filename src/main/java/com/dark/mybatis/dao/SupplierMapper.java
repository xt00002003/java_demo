package com.dark.mybatis.dao;

import com.dark.mybatis.domain.Supplier;
import org.springframework.stereotype.Repository;

@Repository("supplierMapper")
public interface SupplierMapper {
    int deleteByPrimaryKey(Long sid);

    int insert(Supplier record);

    int insertSelective(Supplier record);

    Supplier selectByPrimaryKey(Long sid);

    int updateByPrimaryKeySelective(Supplier record);

    int updateByPrimaryKey(Supplier record);
}