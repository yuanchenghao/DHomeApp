package com.yuemei.dejia.net;

import com.lzy.okgo.model.HttpParams;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class YMHttpParams extends HttpParams {
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, List<String>> entry : urlParamsMap.entrySet()) {
            if (result.length() > 0) result.append("/");
            result.append(entry.getKey()).append("/").append(entry.getValue());
        }
        for (ConcurrentHashMap.Entry<String, List<FileWrapper>> entry : fileParamsMap.entrySet()) {
            if (result.length() > 0) result.append("/");
            result.append(entry.getKey()).append("/").append(entry.getValue());
        }
        return result.toString();
    }
}
