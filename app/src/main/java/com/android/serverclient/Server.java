package com.android.serverclient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.android.java_websocket.WebSocket;
import com.android.java_websocket.WebSocketImpl;
import com.android.java_websocket.server.WebSocketServer;

public class Server extends AsyncTask<String, Void, String> {

    private static final String TAG = "Server";
    private WSServer mServer;
    private NsdHelper mNsdHelper;

    public Server(Context ctx, WSServer server) {
        mNsdHelper = new NsdHelper(ctx, server.getServiceName(), null);
        mServer = server;
    }

    public void start() {
        this.execute();
    }

    @Override
    protected String doInBackground(String... strings) {
        WebSocketImpl.DEBUG = true;
        int port = 8887; // 843 flash policy port
        mServer.start();
        Log.i(TAG, "ChatServer started on port: " + mServer.getPort());
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        AdvertiseServer();
    }


    //Discovery Related code - start
    public void AdvertiseServer() {
        Log.i(TAG, "...starting to Advertise Sever...");
        // Register server
        if (mServer.getPort() > -1) {
            Log.i(TAG, "..Server port being advertised : " + mServer.getPort());
            mNsdHelper.registerService(mServer.getPort(), mServer.getServiceName());
        } else {
            Log.i(TAG, "WebServerSocket isn't bound.");
        }
    }
    //Discovery Related code - end
}
