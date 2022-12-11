package com.atguigu.distributedlock.mapper;

import com.atguigu.distributedlock.pojo.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface StockMapper extends BaseMapper<Stock> {

    @Update("UPDATE tb_stock SET `count` = `count` - #{count} WHERE product_code = #{product_code} AND `count` >= #{count}")
    public Integer updateCount(@Param("product_code") String productCode, @Param("count") Integer count);


    @Select("SELECT  *  FROM tb_stock WHERE product_code = #{product_code} FOR UPDATE")
    Stock queryStockForUpdate(@Param("product_code") String s);
}
