package com.zzc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzc.reggie.common.BaseContext;
import com.zzc.reggie.common.R;
import com.zzc.reggie.entity.AddressBook;
import com.zzc.reggie.entity.User;
import com.zzc.reggie.service.AddressBookService;
import com.zzc.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 赵智超
 * @date: 2023/07/04/9:23
 * @Description:
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        addressBook.setUserId(user.getId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        wrapper.eq(AddressBook::getUserId, user.getId());
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }


    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        queryWrapper.eq(AddressBook::getUserId, user.getId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (null == addressBook) {
            R.error("未设置默认地址");
        }
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getPhone, BaseContext.getCurrentId());
        User user = userService.getOne(userLambdaQueryWrapper);
        addressBook.setUserId(user.getId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);

        return R.success(list);
    }

}
