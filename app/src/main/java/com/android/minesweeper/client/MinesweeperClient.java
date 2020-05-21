package com.android.minesweeper.client;

import android.util.Log;

import com.android.java_websocket.client.WebSocketClient;
import com.android.java_websocket.handshake.ServerHandshake;
import com.android.minesweeper.common.ClientMessage;
import com.android.minesweeper.common.GameActionValidator;
import com.android.minesweeper.common.GameState;
import com.android.minesweeper.common.InitRequest;
import com.android.minesweeper.common.Point;
import com.android.minesweeper.common.ServerMessage;
import com.android.minesweeper.interfaces.Listener;
import com.google.gson.Gson;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class MinesweeperClient extends WebSocketClient {

    public static final String TAG = "MinesweeperClient";
    private Gson gson = new Gson();

    private ClientMessage state;
    private boolean open;

    public MinesweeperClient(URI serverURI) {
        super(serverURI);
    }

    private Listener<Object> onStart;

    public void setOnStart(Listener<Object> onStart) {
        this.onStart = onStart;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(TAG, "opened connection");
        open = true;
        if (onStart != null) onStart.on(null);
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(final String message) {
//        Log.e(TAG, "received string: " + message);
        if (message == null) return;
        ClientMessage msg = gson.fromJson(message, ClientMessage.class);
        if (msg != null) update(msg);
    }

//    @Override
//    public void onMessage(ByteBuffer bytes) {
//        super.onMessage(bytes);
//        Log.e(TAG, "received buffer: " + bytes);
//        this.update(bytes.array());
//    }

    public boolean reveal(Point p) {
        boolean success = GameActionValidator.reveal(state.game, p);
        if (success && open) send(gson.toJson(new ServerMessage(null, p, null, null)));
//        Log.e(TAG, "reveal: " + p + ", " + (success && open));
        return success && open;
    }

    public boolean flag(Point p) {
        boolean success = GameActionValidator.flag(state.game, p);
        if (success && open) send(gson.toJson(new ServerMessage(null, null, p, null)));
//        Log.e(TAG, "flag: " + p + ", " + (success && open));
        return success && open;
    }

    public boolean neighbours(Point p) {
        boolean success = GameActionValidator.neighbours(state.game, p);
        if (success && open) send(gson.toJson(new ServerMessage(null, null, null, p)));
//        Log.e(TAG, "reveal: " + p + ", " + (success && open));
        return success && open;
    }

    public boolean init(int rows, int columns, int bombs) {
        String msg = gson.toJson(new ServerMessage(new InitRequest(rows, columns, bombs), null, null, null));
//        Log.e(TAG, "init: " + msg + ", " + open);
        if (open) send(msg);
        return open;
    }

    private Listener<ClientMessage> onUpdate;

    public void setOnUpdate(Listener<ClientMessage> onUpdate) {
        this.onUpdate = onUpdate;
    }

    private void update(ClientMessage state) {
        try {
//            this.state = GameState.fromBytes(ClientMessage.byteArrayToCharArray(state));
            this.state = state;
            if (onUpdate != null) onUpdate.on(this.state);
        } catch (Exception e) {
//            Log.e(TAG, "update failed: " + e);
        }
    }

    private Listener<String> onMessage;

    public void setOnMessage(Listener<String> onMessage) {
        this.onMessage = onMessage;
    }

    private void message(String message) {
        if (onMessage != null) onMessage.on(message);
    }

//    @Override
//    public void onFragment( Framedata fragment ) {
//        Log.i(TAG, "received fragment: " + new String( fragment.getPayloadData().array() ) );
//    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
//        Log.i(TAG, "Connection closed by " + (remote ? "remote peer" : "us"));
//        Log.i(TAG, "Reason : " + reason + "    Code : " + code);
        open = false;
    }

    @Override
    public void onError(Exception ex) {
//        Log.i(TAG, "onError is called");
        ex.printStackTrace();
    }

    public ClientMessage getState() {
        return this.state;
    }
}
