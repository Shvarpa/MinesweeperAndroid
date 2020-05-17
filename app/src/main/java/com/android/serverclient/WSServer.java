package com.android.serverclient;

import androidx.annotation.Nullable;

import com.android.java_websocket.WebSocket;
import com.android.java_websocket.drafts.Draft;
import com.android.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

interface Service {
    @Nullable String getServiceName();
}

public abstract class WSServer extends WebSocketServer implements Service {
    public WSServer() throws UnknownHostException {
    }

    public WSServer(int port) {
        super(new InetSocketAddress(port));
    }

    public WSServer(InetSocketAddress address) {
        super(address);
    }

    public WSServer(InetSocketAddress address, int decoders) {
        super(address, decoders);
    }

    public WSServer(InetSocketAddress address, List<Draft> drafts) {
        super(address, drafts);
    }

    public WSServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        super(address, decodercount, drafts);
    }

    public WSServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        super(address, decodercount, drafts, connectionscontainer);
    }
}
