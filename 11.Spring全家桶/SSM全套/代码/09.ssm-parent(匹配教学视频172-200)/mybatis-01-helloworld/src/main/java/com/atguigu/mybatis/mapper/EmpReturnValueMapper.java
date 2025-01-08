package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Emp;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 返回值结果：
 *   返回对象，普通：resultType="全类名"；
 *   返回集合：     resultType="集合中元素全类名"；
 * 最佳实践：
 *   1、开启驼峰命名
 *   2、1搞不定的，用自定义映射（ResultMap）
 */
@Mapper
public interface EmpReturnValueMapper {


    Long countEmp();

    BigDecimal getEmpSalaryById(Integer id);

    List<Emp> getAll();


    @MapKey("id")
        // 实际保存的不是 Emp，是 HashMap
    Map<Integer, Emp> getAllMap();


    Emp getEmpById(Integer id);

}
