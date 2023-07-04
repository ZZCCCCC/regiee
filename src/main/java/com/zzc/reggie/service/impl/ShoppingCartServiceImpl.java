package com.zzc.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.reggie.entity.ShoppingCart;
import com.zzc.reggie.mapper.ShoppingCartMapper;
import com.zzc.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author: 赵智超
 * @date: 2023/07/04/11:09
 * @Description:
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
