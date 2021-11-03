package com.yuemei.dejia.net;

import android.os.Build;
import android.util.Log;

import com.yuemei.dejia.AppLog;

import java.io.IOException;
import java.nio.charset.Charset;

import androidx.annotation.RequiresApi;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoggingInterceptor implements Interceptor {
    public static final String TAG = "NetWork_Http";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        if (requestBody != null){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null){
                charset = contentType.charset(UTF_8);
            }
            String paramsStr = buffer.readString(charset);
            AppLog.i( "请求接口: " + request.url() + paramsStr+ "\n" + request.headers());
        }else {
            AppLog.i( "请求接口: " + request.url() + "请求体为空" + "\n" + request.headers());
        }
        return chain.proceed(request);
    }
}
