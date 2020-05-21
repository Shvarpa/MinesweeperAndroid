package com.android.serverclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentWrapper extends Fragment {

    private AsyncTask<String, Void, String> task;

    public void setTask(AsyncTask<String, Void, String> serverTask) {
        this.task = serverTask;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (task != null) task.execute();
    }

    public AsyncTask<String, Void, String> getTask() {
        return this.task;
    }
}
