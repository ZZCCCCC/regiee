package com.zzc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzc.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 赵智超
 * @date: 2023/02/18/15:18
 * @Description:
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
