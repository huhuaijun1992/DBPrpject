package com.ci123.dbmodule.litepalmannager.DbService;


import com.ci123.dbmodule.litepalmannager.listener.DeleteListener;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public interface DeleteService {


    /**
     * 删除表中所有数据（同步）
     *
     * @param modelClass model类
     */
    int deleteAll(Class<?> modelClass);

    /**
     * 删除表中所有数据（异步）
     *
     * @param modelClass model类
     * @param listener   状态监听类
     */
    void deleteAllAsync(Class<?> modelClass, DeleteListener listener);

    /**
     * 条件删除（同步）
     *
     * @param modelClass model类
     * @param conditions sql语句条件（“bookName  =? and bid = ?”, "1", "数学"）
     */
    int deleteByConditions(Class<?> modelClass, String... conditions);

    /**
     * 条件删除（异步）
     *
     * @param modelClass model类
     * @param conditions sql语句条件（“bookName  =? and bid = ?”, "1", "数学"）
     * @param listener 删除监听类
     */
    void deleteByConditionsAsync(Class<?> modelClass, DeleteListener listener, String... conditions);

}
