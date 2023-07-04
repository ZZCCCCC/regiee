package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzc.reggie.common.R;
import com.zzc.reggie.entity.Orders;
import com.zzc.reggie.service.OrderService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 赵智超
 * @date: 2023/07/04/13:14
 * @Description:
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);

        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){

        return null;
    }
}
