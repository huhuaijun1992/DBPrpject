package com.ci123.dbmodule.litepalmannager;


import com.ci123.dbmodule.litepalmannager.DbService.InsertService;
import com.ci123.dbmodule.litepalmannager.listener.InsertListener;

import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.List;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public class DbInsert implements InsertService {


    private DbInsert() {
    }

    public <T extends DbSupport> void saveAll(List<T> list) {
        LitePal.saveAll(list);
    }

    @Override
    public <T extends DbSupport> void saveAllAsync(List<T> list, final InsertListener listener) {
        LitePal.saveAllAsync(list).listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                listener.onFinsh(success);
            }
        });
    }

    private static class Helper{
        public static final DbInsert INSTANCE= new DbInsert();
    }

    public static final DbInsert getInstance(){
        return Helper.INSTANCE;

    }

}
