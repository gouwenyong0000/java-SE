package com.atguigu.practice.controller;
import java.math.BigDecimal;



import com.atguigu.practice.service.EmployeeService;
import com.atguigu.practice.bean.Employee;
import com.atguigu.practice.common.R;
import com.atguigu.practice.vo.req.EmployeeAddVo;
import com.atguigu.practice.vo.req.EmployeeUpdateVo;
import com.atguigu.practice.vo.resp.EmployRespVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * CORS policy：同源策略（限制ajax请求，图片，css，js）； 跨域问题
 * 跨源资源共享（CORS）（Cross-Origin Resource Sharing）
 *    浏览器为了安全，默认会遵循同源策略（请求要去的服务器和当前项目所在的服务器必须是同一个源[同一个服务器]），
 *    如果不是，请求就会被拦截
 *    复杂的跨域请求会发送2次：
 *    1、options 请求：预检请求。浏览器会先发送options请求，询问服务器是否允许当前域名进行跨域访问
 *    2、真正的请求：POST、DELETE、PUT等
 *
 *
 * 浏览器页面所在的：http://localhost   /employee/base
 * 页面上要发去的请求：http://localhost:8080   /api/v1/employees
 *  /以前的东西，必须完全一样，一个字母不一样都不行。浏览器才能把请求（ajax）发出去。
 *
 *  跨域问题：
 *    1、前端自己解决：
 *    2、后端解决：允许前端跨域即可
 *          原理：服务器给浏览器的响应头中添加字段：Access-Control-Allow-Origin = *
 *
 *
 *
 * 数据校验：
 * 1、导入校验包
 * 2、JavaBean 编写校验注解
 * 3、使用 @Valid 告诉 SpringMVC 进行校验
 * 效果1： 如果校验不通过，目标方法不执行
 * 4【以后不用】、在 @Valid 参数后面，紧跟一个 BindingResult 参数，封装校验结果
 * 效果2： 全局异常处理机制
 * 5【推荐】：编写一个全局异常处理器，处理 MethodArgumentNotValidException（校验出错的异常），统一返回校验失败的提示消息
 * 6：自定义校验 = 自定义校验注解 + 自定义校验器
 */

@Tag(name="员工管理") //描述controller类的作用
@CrossOrigin  //允许跨域
@RequestMapping("/api/v1")
@RestController
public class EmployeeRestController {


    @Autowired
    EmployeeService employeeService;



    @Parameters({
            @Parameter(name = "id", description = "员工id", in = ParameterIn.PATH,required = true),
    })
    @Operation(summary="按照id查询员工信息")
    @GetMapping("/employee/{id}")
    public R get(@PathVariable("id") Long id){
        System.out.println("查询用户。目标方法执行.....");
        Employee emp = employeeService.getEmp(id);

         EmployRespVo respVo = new EmployRespVo();
         BeanUtils.copyProperties(emp,respVo);
        //进行脱敏以后返回给前端
        return R.ok(respVo);
    }

    /**
     * 设计模式：单一职责；
     * JavaBean也要分层，各种xxO：
     * Pojo：普通java类
     * Dao：Database Access Object ： 专门用来访问数据库的对象
     * DTO：Data Transfer Object： 专门用来传输数据的对象；
     * TO：transfer Object： 专门用来传输数据的对象；
     * BO：Business Object： 业务对象（Service），专门用来封装业务逻辑的对象；
     * VO：View/Value Object： 值对象，视图对象（专门用来封装前端数据的对象）
     *
     *
     * 新增员工；
     * 要求：前端发送请求把员工的json放在请求体中
     * 要求：如果校验出错，返回给前端。
     *   {
     *       "code": 500,
     *       "msg": "校验失败",
     *       "data": {
     *           "name": "姓名不能为空",  //这些就是为了让前端知道是哪些输入框错了，怎么错误，给用户要显示提示。
     *           "age": "年龄不能超过150"
     *       }
     *   }
     * @param vo
     * @return
     */
    @Operation(summary="新增员工")
    @PostMapping("/employee")
    public R add(@RequestBody @Valid EmployeeAddVo vo){
        //把vo转为do；
        Employee employee = new Employee();
        //属性对拷
        BeanUtils.copyProperties(vo,employee);
        employeeService.saveEmp(employee);
        return R.ok();
//        if (!result.hasErrors()) {  //校验通过
//
//        }
//        // 说明校验错误； 拿到所有属性错误的信息
//        Map<String, String> errorsMap = new HashMap<>();
//        for (FieldError fieldError : result.getFieldErrors()) {
//            //1、获取到属性名
//            String field = fieldError.getField();
//            //2、获取到错误信息
//            String message = fieldError.getDefaultMessage();
//            errorsMap.put(field, message);
//        }
//        return R.error(500, "校验失败", errorsMap);

    }

    /**
     * 修改员工
     * 要求：前端发送请求把员工的json放在请求体中； 必须携带id
     * @param vo
     * @return
     */
    @Operation(summary="按照id修改员工信息")
    @PutMapping("/employee")
    public R update(@RequestBody @Valid EmployeeUpdateVo vo){

        Employee employee = new Employee();
        BeanUtils.copyProperties(vo,employee);

        employeeService.updateEmp(employee);
        return R.ok();
    }

    /**
     *   @XxxMapping("/employee")：Rest 映射注解
     * @param id
     * @return
     */
    @Operation(summary="按照id删除员工信息")
    @DeleteMapping("/employee/{id}")
    public R delete(@PathVariable("id") Long id){
        employeeService.deleteEmp(id);
        return R.ok();
    }

    //语义化
    @Operation(summary="获取所有员工信息")
    @GetMapping("/employees")
    public R all(){
       List<Employee> employees = employeeService.getList();

       //stream
//       List<EmployRespVo> respVos = new ArrayList<>();
//        for (Employee employee : employees) {
//            EmployRespVo vo = new EmployRespVo();
//            BeanUtils.copyProperties(employee,vo);
//            respVos.add(vo);
//        }


        // VO: 脱敏，分层
        List<EmployRespVo> collect = employees.stream()
                .map(employee -> {
                    EmployRespVo vo = new EmployRespVo();
                    BeanUtils.copyProperties(employee, vo);
                    return vo;
                }).collect(Collectors.toList());

        return R.ok(collect);
    }

}
