package com.shf.springsecurityweb.Controller;

import com.shf.springsecurityweb.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    //    @Secured({"ROLE_sale","ROLE_manager"}) // 判断是否具有角色，另外需要注意的是这里匹配的字符串需要添加前缀“ROLE_“。
//    @PreAuthorize("hasAnyAuthority('admins')") // 方法执行之前
    @PostAuthorize("hasAnyAuthority('admins')") // 方法执行之后
    @GetMapping("hello")
    public String hello() {
        return "hello security";
    }


    @GetMapping("getAll")
    @PostAuthorize("hasAnyAuthority('admins')")
    @PostFilter("filterObject.username=='admin1'")
    public List<Users> getAllUser() {
        List<Users> list = new ArrayList<>();
        list.add(new Users(1, "admin1", "6666"));
        list.add(new Users(2, "admin2", "88888"));
        return list;
    }

}
