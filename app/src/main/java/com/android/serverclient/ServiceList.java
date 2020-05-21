package com.android.serverclient;

import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.minesweeper.interfaces.Listener;
import com.android.serverclient.interfaces.Container;

import java.util.ArrayList;
import java.util.List;

public class ServiceList extends RecyclerView.Adapter<ServiceViewHolder> implements Container<NsdServiceInfo> {

    Activity mActivity;
    RecyclerView mView;
    RecyclerView.LayoutManager mLayout;
    private NsdHelper mHelper;

    List<NsdServiceInfo> list = new ArrayList<>();
    Listener<NsdServiceInfo> onJoin;

    public ServiceList(Activity activity, RecyclerView view, @Nullable Listener<NsdServiceInfo> onJoin) {
        mActivity = activity;
        mHelper = new NsdHelper(mActivity);
        mView = view;
        mLayout = new LinearLayoutManager(mActivity);
        mView.setAdapter(this);
        mView.setLayoutManager(mLayout);
        this.onJoin = onJoin;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_service, parent, false), onJoin);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.setService(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void start(String name) {
        mHelper.startDiscovery(name, this);
    }

    public void stop() {
//        list.clear();
//        notifyDataSetChanged();
        mHelper.stopDiscovery();
    }

    @Override
    public void update(final List<NsdServiceInfo> updated) {
        Log.e("TAG", "update: " + updated);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ServiceListDiff(updated, ServiceList.this.list));
//        diffResult.dispatchUpdatesTo(ServiceList.this);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ServiceList.this.list = updated;
                notifyDataSetChanged();
            }
        });
    }
}
