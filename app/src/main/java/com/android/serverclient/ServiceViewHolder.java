package com.android.serverclient;

import com.android.R;
import com.android.minesweeper.interfaces.Listener;

import android.net.nsd.NsdServiceInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private Button btnJoin;
    private NsdServiceInfo service;

    public ServiceViewHolder(@NonNull View itemView) {
        super(itemView);
        assignView(itemView);
        btnJoin.setVisibility(Button.INVISIBLE);
    }

    public ServiceViewHolder(@NonNull View itemView, final Listener<NsdServiceInfo> onJoin) {
        super(itemView);
        assignView(itemView);
        if (onJoin != null) {
            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onJoin.on(getService());
                }
            });
        } else {
            btnJoin.setVisibility(Button.INVISIBLE);
        }
    }

    private void assignView(View itemView) {
        name = itemView.findViewById(R.id.service_name);
        btnJoin = itemView.findViewById(R.id.btnJoin);
    }

    public void setService(NsdServiceInfo service) {
        this.service = service;
        name.setText(service.getServiceName() + ", at " + service.getHost());
    }

    public NsdServiceInfo getService() {
        return this.service;
    }
}
