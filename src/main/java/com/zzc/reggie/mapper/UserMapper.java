package com.zzc.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzc.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 赵智超
 * @date: 2023/07/03/20:43
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
