package com.atguigu.practice;


import com.atguigu.practice.bean.Employee;
import com.atguigu.practice.dao.EmployeeDao;
import com.atguigu.practice.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeDaoTest {


    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    EmployeeService employeeService;

    @Test
    void testEmployeeService() {
        Employee employee = new Employee();
        employee.setId(4L); //查询条件
        employee.setName("李四22");
//        employee.setAge(10);
//        employee.setEmail("aaa");
        employee.setGender("男");
//        employee.setAddress("下次对对对");
//        employee.setSalary(new BigDecimal("0.1"));

        employeeService.updateEmp(employee);
    }


    @Test
    void testEmployeeDao() {
//        Employee empById = employeeDao.getEmpById(4L);
//        System.out.println("empById = " + empById);

//
//        employeeDao.updateEmp(employee);


        employeeDao.deleteById(5L);




    }
}
