package com.android.serverclient;

import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import com.android.java_websocket.client.WebSocketClient;

interface ClientCreator {
    WebSocketClient connect(URI wsurl);
}

public class Client extends AsyncTask<String, Void, String> {


    private static final String TAG = "Client";
    private WebSocketClient mClient;
    private ClientCreator creator;
    private NsdServiceInfo server;

    public Client(ClientCreator creator) {
        this.creator = creator;
    }

    private static URI serverToUri(NsdServiceInfo server) throws URISyntaxException {
        return new URI("ws:/" + server.getHost() + ":" + server.getPort());
    }

    public void connect(NsdServiceInfo server) {
        if(mClient != null) mClient.close();
        this.server = server;
        execute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            mClient = creator.connect(Client.serverToUri(server));
            mClient.connect();
        } catch (URISyntaxException e) {
            Log.d(TAG, "doInBackground: " + "failed creating uri");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
