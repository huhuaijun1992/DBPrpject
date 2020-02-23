package com.ci123.dbmodule.litepalmannager.DbService;


import com.ci123.dbmodule.litepalmannager.listener.QueryListener;


import java.util.List;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public interface QueryService {

    /**
     * 同步查询所有表
     * @param modelClass model类
     */
    <T> List<T> findAll(Class<T> modelClass);

    /**
     * 异步查询所有表数据
     * @param modelClass model类
     * @param listener 查询监听
     */
    <T> void findAllAsync(Class<T> modelClass, QueryListener listener);

    /**
     * 查找
     * @param s model类
     */
    <T> List<T> find(Class<T> s);

    /**
     * 异步查找
     * @param modelClass model类
     * @param listener 查询监听
     */
    <T> void findAsync(final Class<T> modelClass, QueryListener listener);

    /**
     * 排序
     * @param orderBy 表中排序字段
     */
    QueryService order(String orderBy);

    /**
     * 每次查询结果条数
     * @param limit 查询限制条数
     */
    QueryService limit(int limit);

    /**
     * 偏移量
     * @param offset 偏移量
     */
    QueryService offset(int offset);


    /**
     * 查询条件
     * @param conditions 示例（“bookName = ? and bid = ?”, "数学"，“1” ）
     * */
    QueryService where(String... conditions);


}
