package com.atguigu.mybatis;

import com.atguigu.mybatis.bean.Emp;
import com.atguigu.mybatis.mapper.EmpMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@SpringBootTest
class Mybatis01HelloworldApplicationTests {

    @Autowired  //容器中是MyBatis为每个Mapper接口创建的代理对象
    EmpMapper empMapper;

    @Test
    void testValue(){
        //前端传来的参数最好做一个校验（SQL防注入校验）   or
        //SQL防注入工具类
        Emp tEmp = empMapper.getEmpById02(1, "t_emp");
        System.out.println("tEmp = " + tEmp);
    }

    @Test
    void testAll(){
        List<Emp> all = empMapper.getAll();
        for (Emp emp : all) {
            System.out.println(emp);
        }
    }

    @Test
    void testCRUD() {
        Emp emp = new Emp();
        emp.setEmpName("张三x");
        emp.setAge(1011);
        emp.setEmpSalary(11111.0D);

        // 添加是id自增；
        empMapper.addEmp(emp);

        Integer id = emp.getId();
        System.out.println("上次自增id = " + id);

//        empMapper.updateEmp(emp);
//        empMapper.deleteEmpById(5);
    }

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        System.out.println("empMapper = " + empMapper.getClass());

        // #{}取值： 预编译方式
        // Preparing: select id,emp_name empName,age,emp_salary empSalary from t_emp where id = ?
        // Parameters: 1(Integer)
        // Total: 1
        Emp empById = empMapper.getEmpById(1);

        // ${}取值：拼接方式
        // select id,emp_name empName,age,emp_salary empSalary from t_emp where id = 1

        System.out.println("empById = " + empById);


        Connection connection = dataSource.getConnection();
//
        String sql = "select * from user where username = ? and password = ? ";
        //#{}底层预编译方式：
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "admin");
        preparedStatement.setString(2, "123456");
//
//
//        //${}底层拼接方式： SQL注入问题
//        String sql2 = "select * from user where username = 'admin' and password = ' ' or 1=1 or 1='' ";
//        Statement statement = connection.createStatement();
//        statement.execute("select * from t_emp where id = " + 2);


    }

}
