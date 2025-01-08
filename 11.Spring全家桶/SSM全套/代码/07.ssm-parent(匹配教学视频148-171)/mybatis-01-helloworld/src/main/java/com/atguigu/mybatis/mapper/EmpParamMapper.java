package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


//单个参数：
//  1、#{参数名} 就可以取值。
//  2、Map和JavaBean，#{key/属性名} 都可以取值。
//多个参数：
//  用@Param指定参数名， #{参数名} 就可以取值。
@Mapper  //告诉Spring，这是MyBatis操作数据库用的接口; Mapper接口
public interface EmpParamMapper {

    Emp getEmploy(Long id);

    //    // 获取数组中第二个元素指定的用户
    Emp getEmploy02(List<Long> ids);
//

    // 对象属性取值，直接获取
    void addEmploy(Emp e);


    // map中的属性也是直接取值
    void addEmploy2(Map<String, Object> m);

    //==========以上是单个参数测试==============

    //以后多个参数，用@Param指定参数名， #{参数名} 就可以取值。
    Emp getEmployByIdAndName(@Param("id") Long id, @Param("empName") String name);


    // select * from emp where id = #{id} and emp_name = #{从map中取到的name} and age = #{ids的第三个参数值} and salary = #{e中的salary}
    Emp getEmployHaha(@Param("id") Long id,
                      @Param("m") Map<String,Object> m,
                      @Param("ids") List<Long> ids,
                      @Param("e") Emp e);


}
