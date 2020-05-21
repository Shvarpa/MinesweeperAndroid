package com.android.minesweeper.server;

import android.app.Activity;
import android.util.Log;

import com.android.java_websocket.WebSocket;
import com.android.java_websocket.handshake.ClientHandshake;
import com.android.minesweeper.common.ClientMessage;
import com.android.minesweeper.common.Game;
import com.android.minesweeper.common.GameState;
import com.android.minesweeper.common.InitRequest;
import com.android.minesweeper.common.Point;
import com.android.minesweeper.common.ServerMessage;
import com.android.serverclient.WSServer;
import com.google.gson.Gson;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

public class MinesweeperServer extends WSServer {

    public static final String TAG = "MinesweeperServer";
    public static final String serviceName = "Minesweeper";
    private Gson gson = new Gson();
    private Game game = new Game();

    @Override
    public String getServiceName() {
        return serviceName;
    }

    private Activity activity;

    public MinesweeperServer(int port, Activity activity) {
        super(new InetSocketAddress(port));
        this.activity = activity;
    }


    @Override
    public void onOpen(final WebSocket conn, ClientHandshake handshake) {
        //this.sendToAll("new connection: " + handshake.getResourceDescriptor());
//        Log.i(TAG, conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to server");
//        activity.runOnUiThread(new Runnable() {
//            public void run() {
//                Toast.makeText(activity, conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to server", Toast.LENGTH_SHORT).show();
//            }
//        });
        synchronized (conn) {
            conn.send(gson.toJson(new ClientMessage(game.getState())));
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        this.sendToAll(conn + " has left the room!");
//        Log.i(TAG, conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, final String message) {
//        Log.e(TAG, conn + ": " + message);
        if (message == null) return;
        ServerMessage msg = gson.fromJson(message, ServerMessage.class);
        if (msg.init != null) this.init(msg.init);
        if (msg.reveal != null) this.reveal(msg.reveal);
        if (msg.flag != null) this.flag(msg.flag);
        if (msg.neighbours != null) this.neighbours(msg.neighbours);
//        activity.runOnUiThread(new Runnable() {
//            public void run() {
//                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void init(InitRequest initRequest) {
        this.game.generateGame(initRequest.rows, initRequest.columns, initRequest.bombs);
        update();
    }

    private void reveal(Point p) {
        if(this.game.reveal(p)) update();
    }

    private void flag(Point p) {
        if(this.game.flag(p)) update();
    }

    private void neighbours(Point p) {
        if (this.game.revealNeighbours(p)) update();
    }

    private void update() {
        String message = gson.toJson(new ClientMessage(game.getState()));
//        byte[] message = new ClientMessage(game.getState())
//        ByteBuffer message = ByteBuffer.wrap(ClientMessage.charArrayToByteArray(game.getState().toBytes()));
//        Log.e(TAG, "update: " + message);
        Collection<WebSocket> con = this.connections();
        synchronized (con) {
            for (WebSocket c : con)
                c.send(message);
        }
    }

//    @Override
//    public void onFragment(WebSocket conn, Framedata fragment) {
//        Log.i(TAG, "received fragment: " + fragment);
//    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAll(String text) {
        Collection<WebSocket> con = this.connections();
//        Log.i(TAG, "# of active connections : " + con.size());
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }
}
