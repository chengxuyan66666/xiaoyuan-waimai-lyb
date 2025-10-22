package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工登录相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 添加员工
     * @param employeeDTO
     * @return
     */
    @ApiOperation("添加员工")
    @PostMapping()
    public Result insert(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工{}",employeeDTO);
        employeeService.insert(employeeDTO);
        return Result.success();
    }

    /**
     * 查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("分页查询员工信息")
    @GetMapping("/page")
    public Result find(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询员工信息: {}",employeePageQueryDTO);
        PageResult pageResult=employeeService.find(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用禁用员工")
    @PostMapping("/status/{status}")
    public  Result updateStatus(@PathVariable Integer status,Long id)
    {
        log.info("启用禁用员工,状态{}，id{}",status,id);
        if (BaseContext.getCurrentId()!=1){
            log.info("非管理员操作，权限不够");
            return Result.error("非管理员操作，权限不够");
        }
        employeeService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 回显员工信息
     * @param id
     * @return
     */
    @ApiOperation("回显员工信息")
    @GetMapping("{id}")
    public Result find(@PathVariable Long id)
    {
       log.info("回显员工信息，id:{}",id);
       Employee employee=employeeService.find(id);
       return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @ApiOperation("修改员工信息")
    @PutMapping()
    public Result change(@RequestBody EmployeeDTO employeeDTO)
    {
        log.info("修改员工信息{}",employeeDTO);
        if (BaseContext.getCurrentId()!=1){
            log.info("非管理员操作，权限不够");
            return Result.error("非管理员操作，权限不够");
        }
        employeeService.change(employeeDTO);
        return Result.success();
    }
}
