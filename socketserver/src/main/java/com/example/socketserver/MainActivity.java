package com.example.socketserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.socketservice.IServiceInterface;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "[SOCKET] Server";

    IServiceInterface myService;
    boolean isBind =false;
    EditText eMessage;
    Button btn_color;
    String eventName;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });

    final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Service Connected?");
            myService = IServiceInterface.Stub.asInterface(service);
            isBind = true;
            Log.d(TAG, "onServiceConnected: Service Connected! Is Bind?: " + isBind);

            if (isBind == true){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mySocketService();
                        Looper.loop();
                    }
                }).start();
            } else{
                Log.d(TAG, "onServiceConnected: ");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eMessage = (EditText) findViewById(R.id.edit_message);
        Log.d(TAG, "onCreate: Which Thread: " + Looper.myLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                myBindService();
                Log.d(TAG, "on myBindService: Which Thread: " + Looper.myLooper());
                Looper.loop();
            }
        }).start();

        btn_color=findViewById(R.id.btn_Color);
        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    eventName = "button";
                    myService.handleSocketEvent(eventName);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.getLooper().quit();
        super.onDestroy();
    }

    public void myBindService(){
        Intent intent = new Intent("com.example.socketservice.MY_SERVICE");
        intent.setPackage("com.example.socketservice");
        //getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "myBindService: Bind Service");
    }

    public void mySocketService(){
        try {
            if (isBind == true){
                Log.d(TAG, "on mySocketService: Which Thread: " + Looper.myLooper());
                Log.d(TAG, "on mySocketService: Start Service from Server?");
                myService.setMessage(eMessage.getText().toString());
                myService.serviceThreadStart();
                Log.d(TAG, "on mySocketService: Service Start");
            } else{
                Log.d(TAG, "on mySocketService: Not Binded");
            }

        } catch (RemoteException e) {
            Log.d(TAG, "mySocketService: Service Error");
            e.printStackTrace();
        }
    }
}