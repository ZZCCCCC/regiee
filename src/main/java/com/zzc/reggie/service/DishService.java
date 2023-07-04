package com.zzc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.reggie.dto.DishDto;
import com.zzc.reggie.entity.Dish;

/**
 * @author: 赵智超
 * @date: 2023/04/12/18:54
 * @Description:
 */
public interface DishService extends IService<Dish> {

    void saveDish(DishDto dishDto);

    DishDto getDishWithFlavor(long id);

    void updateWithDish(DishDto dishDto);
}
