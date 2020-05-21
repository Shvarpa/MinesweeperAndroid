package com.android.minesweeper;

import com.android.R;
import com.android.minesweeper.interfaces.Listener;
import com.android.minesweeper.server.MinesweeperServer;
import com.android.serverclient.ServiceList;

import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.net.URISyntaxException;

public class JoinGameActivity extends AppCompatActivity {

    ServiceList services;
    Button btnRefresh;

    private static String nsdServerInfoToURI(NsdServiceInfo server) {
        return "ws:/" + server.getHost() + ":" + server.getPort();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                services.stop();
                services.start(MinesweeperServer.serviceName);
            }
        });

        services = new ServiceList(this, (RecyclerView) findViewById(R.id.service_list), new Listener<NsdServiceInfo>() {
            @Override
            public void on(NsdServiceInfo serviceInfo) {
                Intent i = new Intent(JoinGameActivity.this, GameActivity.class);
                i.putExtra("server", nsdServerInfoToURI(serviceInfo));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        services.start(MinesweeperServer.serviceName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        services.stop();
    }
}
