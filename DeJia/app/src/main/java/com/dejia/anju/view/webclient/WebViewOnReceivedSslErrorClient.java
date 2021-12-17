package com.dejia.anju.view.webclient;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

public interface WebViewOnReceivedSslErrorClient {

    void onReceivedSslErrorClient(WebView view, SslErrorHandler handler, SslError error);
}
