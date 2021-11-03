package com.yuemei.dejia.utils.http;

import java.io.IOException;

public interface Interceptor {

    Response intercept(Chain chain) throws IOException;
}
