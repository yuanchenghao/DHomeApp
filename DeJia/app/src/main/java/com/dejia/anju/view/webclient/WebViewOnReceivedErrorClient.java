package com.dejia.anju.view.webclient;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

public interface WebViewOnReceivedErrorClient {
    void onReceivedErrorClient(WebView view, WebResourceRequest request, WebResourceError error);
}
