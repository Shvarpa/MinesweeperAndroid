package com.android.serverclient;

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.R;
import com.android.minesweeper.Listener;

import java.util.List;

interface Container<DataType> {
    void update(List<DataType> updated);
}

public class ServiceList extends RecyclerView.Adapter<ServiceViewHolder> implements Container<NsdServiceInfo> {

    Context mContext;
    RecyclerView mView;
    RecyclerView.LayoutManager mLayout;
    private NsdHelper mHelper;

    List<NsdServiceInfo> list;
    Listener<NsdServiceInfo> onJoin;

    public ServiceList(Context context, RecyclerView view, @Nullable Listener<NsdServiceInfo> onJoin) {
        mContext = context;
        mHelper = new NsdHelper(mContext, null, null);
        mView = view;
        mLayout = new LinearLayoutManager(mContext);
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


    @Override
    public void update(List<NsdServiceInfo> updated) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ServiceListDiff(updated, this.list));
        this.list = updated;
        diffResult.dispatchUpdatesTo(this);
    }
}
