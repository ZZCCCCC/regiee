package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzc.reggie.common.BaseContext;
import com.zzc.reggie.common.R;
import com.zzc.reggie.entity.ShoppingCart;
import com.zzc.reggie.entity.User;
import com.zzc.reggie.service.ShoppingCartService;
import com.zzc.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: 赵智超
 * @date: 2023/07/04/11:10
 * @Description:
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        shoppingCart.setUserId(user.getId());

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, user.getId());

        if (dishId != null) {
            //    添加的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //    套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (one != null) {

            Integer number = one.getNumber();
            shoppingCart.setNumber(number+1);
            shoppingCartService.update(shoppingCart,shoppingCartLambdaQueryWrapper);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);

            one = shoppingCart;
        }
        return R.success(one);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,user.getId());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,user.getId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("删除成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,user.getId());
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        ShoppingCart cartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (cartServiceOne.getNumber()>1){
            cartServiceOne.setNumber(cartServiceOne.getNumber()-1);
            shoppingCartService.update(cartServiceOne,shoppingCartLambdaQueryWrapper);
        }else{
            shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        }

        return R.success(cartServiceOne);
    }


}
