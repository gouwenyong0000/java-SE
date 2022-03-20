package com.atguigu.imperial.court.service.api;

import com.atguigu.imperial.court.entity.Emp;

public interface EmpService {
    Emp getEmpByLogin(String loginAccount, String loginPassword);
}
