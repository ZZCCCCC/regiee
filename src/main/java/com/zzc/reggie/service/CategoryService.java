package com.zzc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzc.reggie.entity.Category;

/**
 * @author: 赵智超
 * @date: 2023/04/12/17:27
 * @Description:
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
