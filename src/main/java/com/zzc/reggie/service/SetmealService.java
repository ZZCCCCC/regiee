package com.zzc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.reggie.dto.SetmealDto;
import com.zzc.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author: 赵智超
 * @date: 2023/04/12/18:55
 * @Description:
 */
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void delWithDish(List<Long> ids);
}

