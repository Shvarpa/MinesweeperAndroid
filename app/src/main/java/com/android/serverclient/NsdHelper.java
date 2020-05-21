/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.serverclient;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.minesweeper.interfaces.Listener;
import com.android.serverclient.interfaces.Container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NsdHelper {

    private NsdManager manager;
    private Context context;

    private Container<NsdServiceInfo> container;
    public static final String SERVICE_TYPE = "_http._tcp.";
    public static final String TAG = "NsdHelper";
    public String serviceName = "WSServer";

    private AtomicBoolean resolveListenerBusy = new AtomicBoolean(false);
    private ConcurrentLinkedQueue<NsdServiceInfo> pending = new ConcurrentLinkedQueue<>();
    private List<NsdServiceInfo> services = Collections.synchronizedList(new ArrayList<NsdServiceInfo>());

    private NsdManager.ResolveListener resolveListener;
    private NsdManager.RegistrationListener registrationListener;
    private NsdManager.DiscoveryListener discoveryListener;

    public NsdHelper(Context context) {
        this.context = context;
        manager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        initializeRegistrationListener();
        initializeResolveListener();
    }

    public void registerService(int port, @Nullable String name, @Nullable String type) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(name != null ? name : serviceName != null ? serviceName : "NO SERVICE NAME");
        serviceInfo.setServiceType(type != null ? type : SERVICE_TYPE);

        manager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    void registerService(int port, @Nullable String name) {
        registerService(port, name, null);
    }

    void registerService(int port) {
        registerService(port, null, null);
    }

    public void startDiscovery(@Nullable final String serviceName, @Nullable final Container<NsdServiceInfo> container) {
        this.serviceName = serviceName;
        this.container = container;
        initializeDiscoveryListener();
        manager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public void stopDiscovery() {
        pending.clear();
        services.clear();
        if (discoveryListener != null) {
            manager.stopServiceDiscovery(discoveryListener);
            discoveryListener = null;
        }
    }


    public List<NsdServiceInfo> getServices() {
        return services;
    }

    public void tearDown() {
        this.stopDiscovery();
        manager.unregisterService(registrationListener);
    }


    private void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
//            serviceName = serviceInfo.getServiceName();
                Log.e(TAG, "service " + serviceName + " registered!");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        };
    }

    private void initializeDiscoveryListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                manager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                manager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.e(TAG, "Service discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.e(TAG, "Service discovery stopped");
                resolveListenerBusy.set(false);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Service discovery success" + serviceInfo);
                if (!serviceInfo.getServiceType().equals(SERVICE_TYPE)) {
                    Log.e(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                }
                String name = serviceInfo.getServiceName();
                if (serviceName == null || name.contains(serviceName)) {
                    if (resolveListenerBusy.compareAndSet(false, true)) {
                        Log.e(TAG, "put to resolving");
                        manager.resolveService(serviceInfo, resolveListener);
                    } else {
                        Log.e(TAG, "put to pending");
                        pending.add(serviceInfo);
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Iterator<NsdServiceInfo> iterator;
                iterator = services.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getServiceName().equals(serviceInfo.getServiceName()))
                        iterator.remove();
                }
                iterator = pending.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getServiceName().equals(serviceInfo.getServiceName()))
                        iterator.remove();
                }
                update();
                Log.e(TAG, "service lost" + serviceInfo);
            }
        };
    }

    private void initializeResolveListener() {
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
                resolvePending();
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                add(serviceInfo);
                resolveListenerBusy.set(false);
                resolvePending();
            }
        };
    }

    private void resolvePending() {
        NsdServiceInfo next = pending.poll();
        Log.e(TAG, "resolvePending: " + next);
        if (next != null && resolveListenerBusy.compareAndSet(false, true))
            manager.resolveService(next, resolveListener);
    }

    private void add(NsdServiceInfo serviceInfo) {
        services.add(serviceInfo);
        update();
    }

    private void update() {
        container.update(services);
    }
}
