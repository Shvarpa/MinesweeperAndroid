package com.android.minesweeper.server;

import com.android.java_websocket.WebSocket;
import com.android.minesweeper.interfaces.Listener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WebSocketDns {

    private HashMap<WebSocket, Integer> dns = new HashMap<>();
    private List<Integer> left = new LinkedList<>();
    private int max = 0;


    public Listener<WebSocket> onConnect = new Listener<WebSocket>() {
        @Override
        public void on(WebSocket conn) {
            dns.put(conn, left.isEmpty() ? max++ : left.remove(0));
        }
    };

    public Listener<WebSocket> onLeave = new Listener<WebSocket>() {
        @Override
        public void on(WebSocket conn) {
            left.add(dns.remove(conn));
        }
    };

    public Integer find(WebSocket conn) {
        return dns.get(conn);
    }
}
