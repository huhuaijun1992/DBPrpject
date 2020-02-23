package com.ci123.dbmodule.litepalmannager;

import android.content.Context;

import com.ci123.dbmodule.litepalmannager.listener.DbListener;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.tablemanager.callback.DatabaseListener;


/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public class DbManager {
    private DbManager() {

    }

    private static final class Helper {
        private static final DbManager INTANCE = new DbManager();
    }

    public static final DbManager getInstance() {
        return Helper.INTANCE;
    }

    /**
     * 初始化
     * @param context 应用的context
     * */
    public void init(Context context) {
        LitePal.initialize(context);
    }

    public DbQuery getDbQuery() {
        DbQuery dbQuery = new DbQuery();
        return dbQuery;
    }

    public DbDelete getDelete() {
        return DbDelete.getInstance();
    }

    public DbInsert getInsert() {
        return DbInsert.getInstance();
    }

    /**
     * 创建数据库并自定义表
     * @param dbName 数据库名
     * @param version 数据库版本
     * @param modelClassNames model类名
     */
    public void useNewDb(String dbName, int version, String... modelClassNames) {
        LitePalDB litePalDB = new LitePalDB(dbName, version);
        for (String s : modelClassNames) {
            litePalDB.addClassName(s);
        }
        LitePal.use(litePalDB);
    }

    /**
     * 创建私用litePal.xml一样配置的数据库,并使用
     *  如果该名数据库已存在则不重新创建，如果表结构有改变则升级，否则就创建并使用此库
     * @param dbName 数据库名
     */
    public void useNewXmlDb(String dbName) {
        LitePalDB litePalDB = LitePalDB.fromDefault(dbName);
        LitePal.use(litePalDB);
    }

    /**
     * 使用默认数据库（litepal.xml定义的数据库也就是默认数据库）
     */
    public void useDefaultDb() {
        LitePal.useDefault();
    }


    /**
     * 删除数据库
     * @param dbName 数据库名
     */
    public void deleteDb(String dbName) {
        LitePal.deleteDatabase(dbName);
    }

    /**
     * 数据库创建更新检测方法
     * @param listener
     */
    public void registDbListener(final DbListener listener) {
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                if (listener != null) {
                    listener.onCreate();
                }
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {
                if (listener != null) {
                    listener.onUpgrade(oldVersion, newVersion);
                }
            }
        });
    }


}
