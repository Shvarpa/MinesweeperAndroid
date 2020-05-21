package com.android.minesweeper;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.java_websocket.client.WebSocketClient;
import com.android.minesweeper.client.MinesweeperClient;
import com.android.minesweeper.common.Cell;
import com.android.minesweeper.common.GameState;
import com.android.minesweeper.common.Point;
import com.android.minesweeper.interfaces.Listener;
import com.android.minesweeper.server.MinesweeperServer;
import com.android.serverclient.ClientTask;
import com.android.serverclient.ServerTask;
import com.android.serverclient.interfaces.ClientCreator;

import java.net.URI;

public class GameActivity extends AppCompatActivity {

    MinesweeperServer server;
    ServerTask serverTask;

    GameGrid grid;
    MinesweeperClient client;
    ClientTask clientTask;
    final int port = 4444;

    TextView tvBombs, tvBombsRemaining, tvRemaining;

    private ClientCreator clientCreator = new ClientCreator() {
        @Override
        public WebSocketClient connect(URI wsurl) {
            GameActivity.this.client = new MinesweeperClient(wsurl);
            client.setOnUpdate(new Listener<GameState>() {
                @Override
                public void on(final GameState data) {
                    draw(data);
                }
            });
            Log.e("TAG", "connect: created client");
            return GameActivity.this.client;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvBombs = findViewById(R.id.bombs);
        tvBombsRemaining = findViewById(R.id.bombsRemaining);
        tvRemaining = findViewById(R.id.remaining);

        grid = new GameGrid(this, (RecyclerView) findViewById(R.id.grid),
                new Listener<Cell>() {
                    @Override
                    public void on(Cell data) {
                        Log.e("TAG", "onclick: " + data.x + "," + data.y);
                        boolean vibrate = false;
                        GameState state = grid.getState();
                        if (data.isRevealed() && !state.getWon() && !state.getLost())
                            vibrate = client.neighbours(data.asPoint());
                        else vibrate = client.reveal(data.asPoint());
                        if (vibrate)
                            grid.mView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                    }
                }
                , new Listener<Cell>() {
            @Override
            public void on(Cell data) {
                boolean vibrate = false;
                if (!data.isRevealed()) vibrate = client.flag(data.asPoint());
                else vibrate = client.neighbours(data.asPoint());
                if (vibrate)
                    grid.mView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
        }
        );

        final Intent i = getIntent();
//        Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        Boolean host = i.getBooleanExtra("host", false);
        if (host) {
            if (serverTask == null) {
                int rows = i.getIntExtra("rows", 10);
                int columns = i.getIntExtra("columns", 10);
                int bombs = i.getIntExtra("bombs", 10);
                createServer(rows, columns, bombs);
            } else if (clientTask == null && server != null) {
                clientTask = new ClientTask(clientCreator);
                clientTask.connect("ws://localhost:" + server.getPort());
            }
        } else {
            String url = i.getStringExtra("server");
            clientTask = new ClientTask(clientCreator);
            clientTask.connect(url);
        }
    }

    private void createClient(String url) {
        clientTask = new ClientTask((clientCreator));
        clientTask.connect(url);
    }

    private void createServer(final int rows, final int columns, final int bombs) {
        server = new MinesweeperServer(4444, this);
        serverTask = new ServerTask(this, server);
        serverTask.setOnStart(new Listener<Object>() {
            @Override
            public void on(Object data) {
                clientTask = new ClientTask(clientCreator);
                clientTask.setOnStartListener(new Listener<Object>() {
                    @Override
                    public void on(Object nothing) {
                        client.setOnStart(new Listener<Object>() {
                            @Override
                            public void on(Object nothing) {
                                client.init(rows, columns, bombs);
                            }
                        });
                    }
                });
                clientTask.connect("ws://localhost:" + server.getPort());
            }
        });
        serverTask.start();
    }

    void draw(final GameState state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == null) return;
                tvBombs.setText(getText(R.string.bombs).toString() + state.getBombs());
                tvBombsRemaining.setText(getText(R.string.bombs_remaining).toString() + (state.getBombs() - state.getFlags()));
                tvRemaining.setText(getText(R.string.remaining).toString() + (state.getRemaining()));
                grid.update(state);
                if (state.getLost())
                    Toast.makeText(GameActivity.this, "Lost", Toast.LENGTH_SHORT).show();
                if (state.getWon())
                    Toast.makeText(GameActivity.this, "Won", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
