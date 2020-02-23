package com.ci123.dbmodule.litepalmannager.listener;

import java.util.List;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public interface QueryListener {

    <T> void Result(List<T> list);
}
