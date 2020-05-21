package com.android.serverclient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.android.java_websocket.WebSocket;
import com.android.java_websocket.WebSocketImpl;
import com.android.java_websocket.server.WebSocketServer;
import com.android.minesweeper.interfaces.Listener;

public class ServerTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "Server";
    private WSServer mServer;
    private NsdHelper mNsdHelper;
    private Listener<Object> onStart;

    public ServerTask(Activity activity, WSServer server) {
        mNsdHelper = new NsdHelper(activity);
        mServer = server;
    }

    public void setOnStart(Listener<Object> onStart) {
        this.onStart = onStart;
    }

    public void start() {
        this.execute();
    }

    @Override
    protected String doInBackground(String... strings) {
        WebSocketImpl.DEBUG = true;
        mServer.start();
        Log.i(TAG, "Server started on port: " + mServer.getPort());
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        AdvertiseServer();
        onStart.on(null);
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
