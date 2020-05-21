package com.android.minesweeper.common;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class ClientMessage {
    @SerializedName("update")
    public GameState update;

    @SerializedName("message")
    public String message;

//    public static byte[] charArrayToByteArray(char[] c_array) {
//        byte[] b_array = new byte[c_array.length];
//        for (int i = 0; i < c_array.length; i++) {
//            b_array[i] = (byte) (0xFF & (int) c_array[i]);
//        }
//        return b_array;
//    }
//
//    public static char[] byteArrayToCharArray(byte[] b_array) {
//        char[] c_array = new char[b_array.length];
//        for (int i = 0; i < b_array.length; i++) {
//            c_array[i] = (char) (0xFF & (int) b_array[i]);
//        }
//        return c_array;
//    }

    public ClientMessage(GameState state) {
        update = state;
    }

    public ClientMessage(String message) {
        this.message = message;
    }
}
