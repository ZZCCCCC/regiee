package com.zzc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzc.reggie.common.CustomException;
import com.zzc.reggie.entity.Category;
import com.zzc.reggie.entity.Dish;
import com.zzc.reggie.entity.Setmeal;
import com.zzc.reggie.mapper.CategoryMapper;
import com.zzc.reggie.service.CategoryService;
import com.zzc.reggie.service.DishService;
import com.zzc.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 赵智超
 * @date: 2023/04/12/17:28
 * @Description:
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品，如已关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish :: getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1>0){
            //已经关联菜品，抛出业务异常
           throw  new CustomException("已关联菜品，不能删除");
        }
        //查询当前分类是否关联套餐，如已关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            //已经关联套餐，抛出业务异常
            throw  new CustomException("已关联套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
