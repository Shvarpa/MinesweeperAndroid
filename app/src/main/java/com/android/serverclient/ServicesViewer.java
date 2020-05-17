//package com.android.serverclient;
//import com.android.R;
//
//import android.content.Context;
//import android.net.nsd.NsdServiceInfo;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.java_websocket.client.WebSocketClient;
//import com.android.java_websocket.drafts.Draft;
//import com.android.java_websocket.drafts.Draft_10;
//import com.android.java_websocket.framing.Framedata;
//import com.android.java_websocket.handshake.ServerHandshake;
//
//import java.net.InetAddress;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//
//
//
//public class ServicesViewer extends AppCompatActivity {
//
//    private static final String TAG = "Client";
//    private ListView mListView;
//    private NsdHelper mNsdHelper;
//    private ArrayList<NsdServiceInfo> servers = new ArrayList<NsdServiceInfo>();
//    private NsdServiceInfo server;
//    private Client mClient;
//
//    void setClient(Client client) {
//        this.mClient = client;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.view_services);
//        //mAddress = (EditText) findViewById(R.id.address);
//
////        mNsdHelper.initializeNsd();
//        mNsdHelper.discoverServices();
//
//
//        //List to show the services advertising as NSD servers - start
//        mListView = (ListView) findViewById(R.id.list);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                mServiceName = (String) mListView.getItemAtPosition(position);
//                if (mServiceName.equals("SamWSServer")) {
//                    mServiceAddress = mNsdServers.get(position).getHost();
//                    mPort = mNsdServers.get(position).getPort();
//                    StartClientAync asynctask = new StartClientAync();
//                    asynctask.execute();
//                }
//            }
//        });
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, mNsdServiceNameList);
//        mListView.setAdapter(adapter);
//        //List to show the services advertising as NSD servers - end
//
//
//        //Discover button which on clicked should list the nsd services in listview -start
//        mDiscoverButton = (Button) findViewById(R.id.discover_button);
//        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                servers = mNsdHelper.getServers();
//                for (NsdServiceInfo ns : mNsdServers) {
//                    Log.i(TAG, "..service name : " + ns.getServiceName());
//                    mNsdServiceNameList.add(ns.getServiceName());
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
//        //Discover button which on clicked should list the nsd services in listview -end
//
////        mConnectButton = (Button) findViewById(R.id.connect_button);
////        mConnectButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View arg0) {
////                //mNsdServers = mNsdHelper.getChosenServiceList();
////                /*Log.i(TAG, "..button connect button clicked....");
////                if (mAddress.getText() != null) {
////                    StartClientAync asynctask = new StartClientAync();
////                    asynctask.execute();
////                }
////                else {
////                    Toast.makeText(getApplicationContext(), "Enter server address", Toast.LENGTH_SHORT).show();
////                }*/
////
////                StartClientAync asynctask = new StartClientAync();
////                asynctask.execute();
////
////            }
////        });
//
//    }
//
//    private setServer(NsdServiceInfo server) {
//        this.server = server;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        try {
//            //mClient = new ExampleClient(new URI(/*"ws://192.168.20.52:8887"*/mAddress.getText().toString()), new Draft_10()); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
//            //mClient.connect();
//
//            //NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
//            //mNsdServers = mNsdHelper.getChosenServiceList();
//            //if (service != null) {
//            if (null != mServiceAddress && mPort >= 0) {
//                Log.i(TAG, "Connecting. host : " + "ws:/" + mServiceAddress + ":" + mPort);
//                mClient = creator.connect(new URI("ws:/" + server.getHost() + ":" + server.getPort()))
//                mClient.connect();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(Client.this, "Connected to " + mServiceName, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Log.i(TAG, "No service to connect to!");
//            }
//
//        } catch (URISyntaxException e) {
//
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//
//    }
//
//}
