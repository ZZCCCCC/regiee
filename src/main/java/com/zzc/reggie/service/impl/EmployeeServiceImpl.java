package com.zzc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.reggie.entity.Employee;
import com.zzc.reggie.mapper.EmployeeMapper;
import com.zzc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: 赵智超
 * @date: 2023/02/18/15:19
 * @Description:
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
