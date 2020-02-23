package com.ci123.dbmodule.litepalmannager.DbService;


import com.ci123.dbmodule.litepalmannager.DbSupport;
import com.ci123.dbmodule.litepalmannager.listener.InsertListener;

import java.util.List;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public interface InsertService {

    /**
     * 保存数据（同步）
     * @param list model数据集合
     */
    <T extends DbSupport> void saveAll(List<T> list);

    /**
     * 保存数据（异步）
     * @param list model数据集合
     * @param listener 保存监听
     */
    <T extends DbSupport> void saveAllAsync(List<T> list, InsertListener listener);
}
