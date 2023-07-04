package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzc.reggie.common.R;
import com.zzc.reggie.entity.Employee;
import com.zzc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: 赵智超
 * @date: 2023/02/18/15:21
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /***
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        //查出来的一条数据
        Employee one = employeeService.getOne(queryWrapper);
        //3.如果没有查到则返回失败结果
        if (one == null) {
            return R.error("用户名错误");
        }
        //4.进行密码比对不一致，返回失败结果
        if (!one.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //5.查看状态是否禁用
        if (one.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //6.登录成功
        HttpSession session = request.getSession();
        //System.out.println(one.getId());
        session.setAttribute("employee", one.getId());
        return R.success(one);
    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加条件
        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //初始密码123456进行MD5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //补全信息
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //Long empID = (Long) (request.getSession().getAttribute("employee"));
        //employee.setCreateUser(empID);
        //employee.setUpdateUser(empID);
        employeeService.save(employee);
        //log.info("{}", employee);
        return R.success("新增员工成功");
    }

    /**
     * 根据ID修改状态信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        //Long employeeId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(employeeId);
        employeeService.updateById(employee);
        return  R.success("员工状态信息修改成功");
    }

    /**
     * 根据ID查询员工信息
     * @param Id
     * @return
     */
    @GetMapping("/{Id}")
    public R<Employee> getById(@PathVariable Long Id){
        log.info("根据ID查询员工信息");
        Employee employee = employeeService.getById(Id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("无对应的员工信息");
    }
}
