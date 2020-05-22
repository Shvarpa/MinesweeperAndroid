package com.android.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.java_websocket.client.WebSocketClient;
import com.android.minesweeper.client.MinesweeperClient;
import com.android.minesweeper.common.Cell;
import com.android.minesweeper.common.ClientMessage;
import com.android.minesweeper.common.GameState;
import com.android.minesweeper.common.Point;
import com.android.minesweeper.common.RandomColors;
import com.android.minesweeper.interfaces.Listener;
import com.android.minesweeper.server.MinesweeperServer;
import com.android.serverclient.ClientTask;
import com.android.serverclient.FragmentWrapper;
import com.android.serverclient.ServerTask;
import com.android.serverclient.interfaces.ClientCreator;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {


    GameGrid grid;


    MinesweeperServer server;
    MinesweeperClient client;

    ServerTask serverTask;
    ClientTask clientTask;

    static private final String SERVER_TAG = "minesweeperserver";
    static private final String CLIENT_TAG = "minesweeperclient";

    final int port = 4444;

    TextView tvBombs, tvBombsRemaining, tvRemaining;

    private ClientCreator clientCreator = new ClientCreator() {
        @Override
        public WebSocketClient connect(URI wsurl) {
            GameActivity.this.client = new MinesweeperClient(wsurl);
            assignClientCallbacks();
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

        FragmentManager fm = getSupportFragmentManager();

        final Intent i = getIntent();
//        Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        getServerFragment(fm, i);
        getClientFragment(fm, i);
    }

    private void getServerFragment(FragmentManager fm, Intent i) {
        Boolean host = i.getBooleanExtra("host", false);
        if (!host || serverTask != null) return;
        FragmentWrapper serverFragment = ((FragmentWrapper) fm.findFragmentByTag(SERVER_TAG));
        if (serverFragment == null) {
            int rows = i.getIntExtra("rows", 10);
            int columns = i.getIntExtra("columns", 10);
            int bombs = i.getIntExtra("bombs", 10);
            createServer(fm, rows, columns, bombs);
        } else {
            serverTask = (ServerTask) serverFragment.getTask();
            server = (MinesweeperServer) serverTask.getServer();
            Log.e("TAG", "getServerFragment: task: " + (serverTask != null) + ", client: " + (server != null));
        }
    }

    private void getClientFragment(FragmentManager fm, Intent i) {
        if (clientTask != null) return;
        FragmentWrapper clientFragment = ((FragmentWrapper) fm.findFragmentByTag(CLIENT_TAG));
        if (clientFragment == null) {
            String url = i.getStringExtra("server");
            if (url != null) createClient(fm, url);
            else if (server != null) createClient(fm, "ws://localhost:" + server.getPort());
        } else {
            clientTask = (ClientTask) clientFragment.getTask();
            client = (MinesweeperClient) clientTask.getClient();
            assignClientCallbacks();
            draw(client.getState());
            Log.e("TAG", "getClientFragment: task: " + (clientTask != null) + ", client: " + (client != null));
        }
    }

    private void assignClientCallbacks() {
        client.setOnUpdate(new Listener<ClientMessage>() {
            @Override
            public void on(final ClientMessage data) {
                draw(data);
            }
        });
    }

    private void createClient(FragmentManager fm, String url) {
        FragmentWrapper clientFragment = new FragmentWrapper();
        clientTask = new ClientTask((clientCreator));
        clientTask.setUri(url);
        clientFragment.setTask(clientTask);
        fm.beginTransaction().add(clientFragment, CLIENT_TAG).commit();
    }

    private void createServer(final FragmentManager fm, final int rows, final int columns, final int bombs) {
        server = new MinesweeperServer(port);
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
                                clientTask.setOnStartListener(null);
                            }
                        });
                    }
                });
                clientTask.setUri("ws://localhost:" + server.getPort());
                FragmentWrapper clientFragment = new FragmentWrapper();
                clientFragment.setTask(clientTask);
                fm.beginTransaction().add(clientFragment, CLIENT_TAG).commit();
            }
        });
        FragmentWrapper serverFragment = new FragmentWrapper();
        serverFragment.setTask(serverTask);
        fm.beginTransaction().add(serverFragment, SERVER_TAG).commit();
    }

    private Map<Integer, List<Point>> highlights;

    void draw(final ClientMessage state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == null || state.game == null) return;
                tvBombs.setText(getText(R.string.bombs).toString() + state.game.getBombs());
                tvBombsRemaining.setText(getText(R.string.bombs_remaining).toString() + (state.game.getBombs() - state.game.getFlags()));
                tvRemaining.setText(getText(R.string.remaining).toString() + (state.game.getRemaining()));
                CollectionUtils.forEach(state.highlights, new CollectionUtils.Predicate<Map.Entry<Integer, List<Point>>>() {
                    @Override
                    public boolean apply(Map.Entry<Integer, List<Point>> entry) {
                        List<Point> points = entry.getValue();
                        List<Point> prev = highlights != null ? highlights.get(entry.getKey()) : null;
                        if (prev != null && prev.get(0).equals(points.get(0))) return false;
                        int color = getColor(entry.getKey());
                        for (Point p : points) {
                            state.game.get(p).color = color;
                        }
                        return true;
                    }
                });
                highlights = state.highlights;
                if (state.game.getLost()) {
                    Point losingPoint = state.game.getLosingPoint();
                    if (losingPoint != null) state.game.get(losingPoint).lost = true;
                    Toast.makeText(GameActivity.this, "Lost", Toast.LENGTH_SHORT).show();
                }
                if (state.game.getWon()) {
                    Long time = state.game.getWiningTime();
                    Toast.makeText(GameActivity.this, "Won" + (time != null ? " in " + (Math.floor(time / 1e7) / 1e2) + " seconds!!" : ""), Toast.LENGTH_SHORT).show();
                }
                grid.update(state.game);
            }
        });
    }

    private HashMap<Integer, Integer> colors = new HashMap<>();
    private RandomColors randomColors = new RandomColors();

    private Integer getColor(Integer target) {
        Integer c = colors.get(target);
        if (c != null) return c;
        c = randomColors.getColor();
        colors.put(target, c);
        return c;
    }

}
