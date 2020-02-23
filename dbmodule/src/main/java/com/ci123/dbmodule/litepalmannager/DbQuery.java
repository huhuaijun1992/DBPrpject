package com.ci123.dbmodule.litepalmannager;


import com.ci123.dbmodule.litepalmannager.DbService.QueryService;
import com.ci123.dbmodule.litepalmannager.listener.QueryListener;

import org.litepal.FluentQuery;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

/**
 * @author: 11304
 * @date: 2020/2/6
 * @desc:
 */
public class DbQuery implements QueryService {
    private FluentQuery fluentQuery;


    @Override
    public <T> List<T> findAll(Class<T> modelClass) {
        return LitePal.findAll(modelClass);
    }

    @Override
    public <T> void findAllAsync(Class<T> modelClass, final QueryListener listener) {
        LitePal.findAllAsync(modelClass).listen(new FindMultiCallback<T>() {
            @Override
            public void onFinish(List<T> list) {
                if (listener!=null){
                    listener.Result(list);
                }
            }
        });
    }


    @Override
    public <T> List<T> find(Class<T> modelClass) {
        if (fluentQuery != null) {
            return fluentQuery.find(modelClass);
        }
        return null;
    }

    @Override
    public <T> void findAsync(Class<T> modelClass, final QueryListener listener) {
        if (fluentQuery != null){
            fluentQuery.findAsync(modelClass).listen(new FindMultiCallback<T>() {
                @Override
                public void onFinish(List<T> list) {
                    if (listener!=null){
                        listener.Result(list);
                    }
                }
            });
        }
    }

    @Override
    public QueryService order(String orderBy) {
        if (fluentQuery!=null){
            this.fluentQuery = fluentQuery.order(orderBy);
        }
        return this;
    }

    @Override
    public QueryService limit(int limit) {
        if (fluentQuery!=null){
            this.fluentQuery = fluentQuery.limit(limit);
        }
        return this;
    }

    @Override
    public QueryService offset(int offset) {
        if (fluentQuery !=null){
            this.fluentQuery = fluentQuery.limit(offset);
        }
        return this;
    }

    @Override
    public QueryService where(String... conditions) {
        this.fluentQuery =LitePal.where(conditions);
        return this;
    }
}
