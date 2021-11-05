package com.yuemei.dejia.webSocket;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public abstract class WsStatusListener {

  public void onOpen(WebSocket webSocket, Response response) {
  }

  public void onMessage(WebSocket webSocket, String text) {
  }

  public void onMessage(ByteString bytes) {
  }

  public void onReconnect() {

  }

  public void onClosing(int code, String reason) {
  }


  public void onClosed(int code, String reason) {
  }

  public void onFailure(Throwable t, Response response) {
  }
}
