package com.zzc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.reggie.entity.OrderDetail;
import com.zzc.reggie.mapper.OrderDetailMapper;
import com.zzc.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author: 赵智超
 * @date: 2023/07/04/13:15
 * @Description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
