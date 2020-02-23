package com.ci123.dbmodule.litepalmannager;

import com.ci123.dbmodule.litepalmannager.DbService.DeleteService;
import com.ci123.dbmodule.litepalmannager.listener.DeleteListener;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public class DbDelete implements DeleteService {

    private DbDelete() {
    }

    private static class Helper {

        private static final DbDelete INSTANCE = new DbDelete();

    }

    public static final DbDelete getInstance() {
        return Helper.INSTANCE;
    }

    @Override
    public int deleteAll(Class<?> modelClass) {
        return LitePal.deleteAll(modelClass);
    }

    @Override
    public void deleteAllAsync(Class<?> modelClass, final DeleteListener listener) {
        LitePal.deleteAllAsync(modelClass).listen(new UpdateOrDeleteCallback() {
            @Override
            public void onFinish(int rowsAffected) {
                if (listener!=null){
                    listener.result(rowsAffected);
                }
            }
        });
    }

    @Override
    public int deleteByConditions(Class<?> modelClass, String... conditions) {
        return LitePal.deleteAll(modelClass,conditions);
    }

    @Override
    public void deleteByConditionsAsync(Class<?> modelClass, final DeleteListener listener, String... conditions) {
        LitePal.deleteAllAsync(modelClass,conditions).listen(new UpdateOrDeleteCallback() {
            @Override
            public void onFinish(int rowsAffected) {
                if (listener!=null){
                    listener.result(rowsAffected);
                }
            }
        });

    }


}
