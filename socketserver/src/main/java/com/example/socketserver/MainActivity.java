package com.example.socketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.socketservice.IServiceInterface;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[SOCKET] Server";

    IServiceInterface myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent("com.example.socketService.MyService.aidl");
        intent.setPackage("com.example.socketService");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: Bind Service");
        
        try {
            myService.serviceThreadStart();
            Log.d(TAG, "onCreate: Service Start");
        } catch (RemoteException e) {
            Log.d(TAG, "onCreate: Service Error");
            e.printStackTrace();
        }

    }


    final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IServiceInterface.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: ");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}