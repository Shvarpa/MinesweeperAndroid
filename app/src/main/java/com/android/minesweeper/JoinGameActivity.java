package com.android.minesweeper;

import com.android.R;
import com.android.serverclient.NsdHelper;
import com.android.serverclient.ServiceList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class JoinGameActivity extends AppCompatActivity {

    NsdHelper helper;
    ServiceList services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        services = new ServiceList(this, (RecyclerView) findViewById(R.id.service_list), null);
        helper = new NsdHelper(this, "Minesweeper", services);
    }
}
