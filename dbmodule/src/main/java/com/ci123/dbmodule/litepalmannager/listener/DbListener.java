package com.ci123.dbmodule.litepalmannager.listener;

/**
 * @author: 11304
 * @date: 2020/2/12
 * @desc:
 */
public interface DbListener {
     void onCreate();
     void onUpgrade(int oldVersion, int newVersion);

}
