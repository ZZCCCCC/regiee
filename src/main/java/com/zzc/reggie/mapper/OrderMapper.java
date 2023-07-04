package com.zzc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzc.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 赵智超
 * @date: 2023/07/04/13:13
 * @Description:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
