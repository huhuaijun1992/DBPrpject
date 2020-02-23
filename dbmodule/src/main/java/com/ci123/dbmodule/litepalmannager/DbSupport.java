package com.ci123.dbmodule.litepalmannager;

import com.ci123.dbmodule.litepalmannager.listener.InsertListener;
import com.ci123.dbmodule.litepalmannager.listener.UpdateListener;

import org.litepal.crud.LitePalSupport;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public class DbSupport extends LitePalSupport {

    /**
     * 异步保存
     *
     * @param listener
     */
    public void saveAsync(final InsertListener listener) {
        super.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                listener.onFinsh(success);
            }
        });
    }

    /**
     * 异步修改数据
     *
     * @param listener
     * @param conditions 条件
     */
    public void updateAllAsync(final UpdateListener listener, String... conditions) {
        super.updateAllAsync(conditions).listen(new UpdateOrDeleteCallback() {
            @Override
            public void onFinish(int rowsAffected) {
                if (listener != null) {
                    listener.result(rowsAffected);
                }
            }
        });
    }

    /**
     * 异步保存或者更新数据
     */
    public void saveOrUpdateAsync(final InsertListener listener, String... conditions) {
        super.saveOrUpdateAsync(conditions).listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                listener.onFinsh(success);
            }
        });
    }
  }

