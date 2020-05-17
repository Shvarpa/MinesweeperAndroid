package com.android.serverclient;

import android.util.Log;
import android.widget.Toast;

import com.android.java_websocket.client.WebSocketClient;
import com.android.java_websocket.drafts.Draft;
import com.android.java_websocket.framing.Framedata;
import com.android.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatClient extends WebSocketClient {

    public static final String TAG = "ChatClient";

    public ChatClient(URI serverURI) {
        super( serverURI );
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        Log.i(TAG, "opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage( final String message ) {
        Log.i(TAG, "received: " + message);
//        Client.this.runOnUiThread(new Runnable() {
//            public void run() {
//                Toast.makeText(Client.this, message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onFragment( Framedata fragment ) {
        Log.i(TAG, "received fragment: " + new String( fragment.getPayloadData().array() ) );
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        Log.i(TAG, "Connection closed by " + ( remote ? "remote peer" : "us" ) );
        Log.i(TAG, "Reason : " + reason + "    Code : " + code);
    }

    @Override
    public void onError( Exception ex ) {
        Log.i(TAG, "onError is called");
        ex.printStackTrace();
    }

}
