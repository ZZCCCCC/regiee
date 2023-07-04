package com.zzc.reggie.common;

/**
 * @author: 赵智超
 * @date: 2023/04/12/16:07
 * @Description: 基于threadLocal用于保存登录的Id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置Id保存到ThreadLocal
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取Id从ThreadLocal
     * @return
     */
    public static  Long getCurrentId(){
        return threadLocal.get();
    }
}
