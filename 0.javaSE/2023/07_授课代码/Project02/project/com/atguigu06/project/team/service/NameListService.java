package com.atguigu06.project.team.service;

import com.atguigu06.project.team.domain.*;


/**
 * ClassName: NameListService
 * Description:
 *      负责将Data中的数据封装到Employee[]数组中，同时提供相关操作Employee[]的方法。
 * @Author 尚硅谷-宋红康
 * @Create 14:37
 * @Version 1.0
 */
public class NameListService {
    private Employee[] employees;


    public NameListService(){
//        根据项目提供的Data类构建相应大小的employees数组
        employees = new Employee[Data.EMPLOYEES.length];

        for (int i = 0; i < employees.length; i++) {

            int type = Integer.parseInt(Data.EMPLOYEES[i][0]); //员工的类型

            //获取通用的属性
            int id = Integer.parseInt(Data.EMPLOYEES[i][1]);
            String name = Data.EMPLOYEES[i][2];
            int age = Integer.parseInt(Data.EMPLOYEES[i][3]);
            double salary = Double.parseDouble(Data.EMPLOYEES[i][4]);
            Equipment equipment;
            double bonus;
            int stock;
            // 再根据Data类中的数据构建不同的对象，包括Employee、Programmer、Designer和Architect对象，以及相关联的Equipment子类的对象
            //将对象存于数组中
            //Data类位于com.atguigu.team.service包中
            switch(type){
                case Data.EMPLOYEE:
                    employees[i] = new Employee(id,name,age,salary);
                    break;
                case Data.PROGRAMMER:
                    equipment = createEquipment(i);
                    employees[i] = new Programmer(id,name,age,salary,equipment);
                    break;
                case Data.DESIGNER:
                    equipment = createEquipment(i);
                    bonus = Double.parseDouble(Data.EMPLOYEES[i][5]);
                    employees[i] = new Designer(id,name,age,salary,equipment,bonus);
                    break;
                case Data.ARCHITECT:
                    equipment = createEquipment(i);
                    bonus = Double.parseDouble(Data.EMPLOYEES[i][5]);
                    stock = Integer.parseInt(Data.EMPLOYEES[i][6]);
                    employees[i] = new Architect(id,name,age,salary,equipment,bonus,stock);
                    break;
            }

        }

    }

    private Equipment createEquipment(int index){
        int equipmentType = Integer.parseInt(Data.EQUIPMENTS[index][0]);

        String modelOrName = Data.EQUIPMENTS[index][1];
        String priceOrDisplayOrType = Data.EQUIPMENTS[index][2];

        switch (equipmentType){
            case Data.PC:

                return new PC(modelOrName,priceOrDisplayOrType);
            case Data.NOTEBOOK:
                double price = Double.parseDouble(priceOrDisplayOrType);
                return new NoteBook(modelOrName,price);
            case Data.PRINTER:

                return new Printer(modelOrName,priceOrDisplayOrType);

        }
        return null;
    }

    public Employee[] getAllEmployees(){
        return employees;
    }

    /**
     * 获取指定id对应的员工
     * @param id
     * @return
     */
    public Employee getEmployee(int id) throws TeamException{
        for(int i = 0;i < employees.length;i++){
            if(id == employees[i].getId()){
                return employees[i];
            }
        }
        //如果执行到此位置，就意味着没有找到
        throw new TeamException("找不到指定的员工");

    }

}
