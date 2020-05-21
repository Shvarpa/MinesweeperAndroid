package com.android.serverclient;

import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import com.android.java_websocket.client.WebSocketClient;
import com.android.minesweeper.interfaces.Listener;
import com.android.serverclient.interfaces.ClientCreator;

public class ClientTask extends AsyncTask<String, Void, String> {


    private static final String TAG = "Client";
    private WebSocketClient mClient;
    private ClientCreator creator;
    private URI url;

    public ClientTask(ClientCreator creator) {
        this.creator = creator;
    }

    public void connect(String url) {
        if (mClient != null) mClient.close();
        try {
            this.url = new URI(url);
        } catch (Exception e) {
            Log.e(TAG, "URI Parsing Failed, client cant join " + url);
        }
        execute();
    }


    private Listener<Object> onStart;

    public void setOnStartListener(Listener<Object> onStart) {
        this.onStart = onStart;
    }

    @Override
    protected String doInBackground(String... strings) {
        mClient = creator.connect(url);
        mClient.connect();
        if (onStart != null) onStart.on(null);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
