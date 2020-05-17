package com.android.serverclient;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.java_websocket.WebSocket;
import com.android.java_websocket.framing.Framedata;
import com.android.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collection;

public class ChatServer extends WSServer {

    public static final String TAG = "ChatServer";

    private Activity activity;

    public ChatServer(int port, Activity activity) {
        super(new InetSocketAddress(port));
        this.activity = activity;
    }


    @Override
    public void onOpen(final WebSocket conn, ClientHandshake handshake) {
        //this.sendToAll("new connection: " + handshake.getResourceDescriptor());
        Log.i(TAG, conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to server");
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        this.sendToAll(conn + " has left the room!");
        Log.i(TAG, conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, final String message) {
        Log.i(TAG, conn + ": " + message);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFragment(WebSocket conn, Framedata fragment) {
        Log.i(TAG, "received fragment: " + fragment);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAll(String text) {
        Collection<WebSocket> con = this.connections();
        Log.i(TAG, "# of active connections : " + con.size());
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }

    @Override
    public String getServiceName() {
        return "ChatService";
    }
}
