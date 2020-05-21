package com.android.minesweeper;

import com.android.R;
import com.android.minesweeper.server.MinesweeperServer;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartGameActivity extends AppCompatActivity {

    Button btnStart;
    Spinner sDifficulty;
    TextView tvStatus;
    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel channel;
    WifiP2pManager manager;
    MinesweeperServer server;
//    ServerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        btnStart = findViewById(R.id.btnStart);
        tvStatus = findViewById(R.id.status);
        sDifficulty = findViewById(R.id.difficulty_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDifficulty.setAdapter(adapter);
        sDifficulty.setSelection(0);

//        server = new MinesweeperServer(4444, this);
//        task = new ServerTask(this, server);
//                task.start();
//                tvStatus.setText("Running on port: " + server.getPort());
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartGameActivity.this, GameActivity.class);
                int difficulty = sDifficulty.getSelectedItemPosition() + 3;

                i.putExtra("host", true);
                i.putExtra("rows", (int) (difficulty * 7));
                i.putExtra("columns", (int) (difficulty * 3));
                i.putExtra("bombs", (int) (difficulty * difficulty * 4));

//                i.putExtra("host", true);
//                i.putExtra("rows", 3);
//                i.putExtra("columns", 3);
//                i.putExtra("bombs", 1);

                startActivity(i);
            }
        });

    }
}
