package com.atguigu.aclservice;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.PermissionService;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Tes {

    @Autowired
    PermissionService permissionService;

    @Test
    public void allMeau(){
        List<Permission> permissions = permissionService.queryAllMenuGuli();
        String s = JSON.toJSONString(permissions);

        System.out.println(s);
    }

}
