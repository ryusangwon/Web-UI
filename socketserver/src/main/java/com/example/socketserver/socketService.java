package com.example.socketserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class socketService extends Service implements View.OnClickListener {

    private static final String TAG = "[SOCKET] Server Thread";
    public TextView send;

    @Override
    public void onCreate() {
        
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        ServerThread thread = new ServerThread();
        Log.d(TAG, "onCreate: make server thread");
        thread.start();
        Log.d(TAG, "onCreate: after thread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Change:
                Log.d(TAG, "onClick: Change");

                break;

            case R.id.send:
                Log.d(TAG, "onClick: Send");

                break;
        }

    }

    class ServerThread extends Thread{
        @Override
        public void run() {
            Log.d(TAG, "run: ");
            int port = 5672;
            try {
                ServerSocket server = new ServerSocket(port);

                while(true){
                    Log.d(TAG, "run: server accept?");
                    Socket socket = server.accept();
                    Log.d(TAG, "run: server accept, send Object");

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject("Connect with ObjectStream");
                    outputStream.flush();

                    socket.close();
                }
            } catch (Exception e){
                Log.d(TAG, "run: error");
                e.printStackTrace();
            }

        }
    }

}
