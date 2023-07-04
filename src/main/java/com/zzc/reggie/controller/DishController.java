package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzc.reggie.common.R;
import com.zzc.reggie.dto.DishDto;
import com.zzc.reggie.entity.Category;
import com.zzc.reggie.entity.Dish;
import com.zzc.reggie.entity.DishFlavor;
import com.zzc.reggie.service.CategoryService;
import com.zzc.reggie.service.DishFlavorService;
import com.zzc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 赵智超
 * @date: 2023/07/03/11:21
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("dishDto is {}", dishDto);
        dishService.saveDish(dishDto);

        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //最总返回的数据
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        //从dish表中查询数据 其中没有分类名称
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        //拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable long id) {

        DishDto dishDto = dishService.getDishWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("dishDto is {}", dishDto);
        dishService.updateWithDish(dishDto);

        return R.success("修改菜品成功");
    }

    @PostMapping("/status/{id}")
    public R<String> updateStatus(@PathVariable long id, String ids) {
        log.info("id is {}", id);
        log.info("ids is {}", ids);

        String[] arrays = ids.split(",");
        for (String array : arrays) {
            long i = Long.parseLong(array);
            Dish dish = new Dish();
            dish.setStatus((int) id);
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getId, i);
            dishService.update(dish, queryWrapper);
        }

        return R.success("修改成功");
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteByIds(String ids) {
        log.info("ids is {}", ids);


        String[] arrays = ids.split(",");
        for (String array : arrays) {
            Long id = Long.parseLong(array);
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
            dishService.removeById(id);
        }

        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort, Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = null;
        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 =new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }


}
