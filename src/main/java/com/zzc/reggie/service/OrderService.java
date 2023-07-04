package com.zzc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.reggie.entity.Orders;

/**
 * @author: 赵智超
 * @date: 2023/07/04/13:13
 * @Description:
 */
public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
