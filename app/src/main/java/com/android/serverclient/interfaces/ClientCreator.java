package com.android.serverclient.interfaces;

import com.android.java_websocket.client.WebSocketClient;

import java.net.URI;

public interface ClientCreator {
    WebSocketClient connect(URI wsurl);
}
