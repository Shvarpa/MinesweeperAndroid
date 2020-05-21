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
    private URI uri;

    public ClientTask(ClientCreator creator, String url) {
        this.creator = creator;
        if (url != null) setUri(url);
    }

    public ClientTask(ClientCreator creator) {
        this.creator = creator;
    }

    public void connect(String url) {
        if (mClient != null) mClient.close();
        setUri(url);
        execute();
    }

    public void setUri(String url) {
        try {
            this.uri = new URI(url);
        } catch (Exception e) {
            Log.e(TAG, "URI Parsing Failed, client cant join " + url);
        }
    }

    private Listener<Object> onStart;

    public void setOnStartListener(Listener<Object> onStart) {
        this.onStart = onStart;
    }

    public WebSocketClient getClient() {
        return mClient;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (uri == null) return null;
        mClient = creator.connect(uri);
        mClient.connect();
        if (onStart != null) onStart.on(null);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
