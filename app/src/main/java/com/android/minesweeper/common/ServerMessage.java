package com.android.minesweeper.common;

import com.android.minesweeper.common.InitRequest;
import com.android.minesweeper.common.Point;
import com.google.gson.annotations.SerializedName;

public class ServerMessage {
    @SerializedName("init")
    public InitRequest init;

    @SerializedName("reveal")
    public Point reveal;

    @SerializedName("flag")
    public Point flag;

    @SerializedName("neighbours")
    public Point neighbours;

    public ServerMessage(InitRequest init, Point reveal, Point flag, Point neighbours) {
        this.init = init;
        this.reveal = reveal;
        this.flag = flag;
        this.neighbours = neighbours;
    }
}
