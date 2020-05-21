package com.android.minesweeper.common;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClientMessage {
    @SerializedName("game")
    public GameState game;

    @SerializedName("highlights")
    public Map<Integer,List<Point>> highlights;

    public ClientMessage(GameState game, Map<Integer,List<Point>> highlights) {
        this.game = game;
        this.highlights = highlights;
    }

}
