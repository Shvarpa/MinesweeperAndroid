package com.android.serverclient;

import android.net.nsd.NsdServiceInfo;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ServiceListDiff extends DiffUtil.Callback {

    private List<NsdServiceInfo> oldList;
    private List<NsdServiceInfo> newList;

    public ServiceListDiff(List<NsdServiceInfo> oldList, List<NsdServiceInfo> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }
    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getServiceName().equals(newList.get(newItemPosition).getServiceName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
