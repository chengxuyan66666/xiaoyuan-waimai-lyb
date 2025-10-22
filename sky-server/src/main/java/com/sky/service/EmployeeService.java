package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import io.swagger.models.auth.In;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     * @param employeeDTO
     */
    void insert(EmployeeDTO employeeDTO);

    /**
     * 查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    PageResult find(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工
     */
    void updateStatus(Integer status, Long id);

    /**
     * 回显员工信息
     * @param id
     * @return
     */
    Employee find(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void change(EmployeeDTO employeeDTO);
}
