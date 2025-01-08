package com.atguigu.practice.service;

import com.atguigu.practice.bean.Employee;

import java.util.List;

public interface EmployeeService {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Employee getEmp(Long id);

    /**
     * 更新用户
     * @param employee
     */
    void updateEmp(Employee employee);

    /**
     * 新增用户
     * @param employee
     */
    void saveEmp(Employee employee);


    /**
     * 根据id删除用户
     * @param id
     */
    void deleteEmp(Long id);

    /**
     * 查询所有用户
     * @return
     */
    List<Employee> getList();

}
