package com.dejia.anju.api.base;

public interface CallBackListener <T>{

    void onSuccess(T t);

    void onError(T t);
}
