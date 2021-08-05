package com.example.socketserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class socketService extends Service {

    private static final String TAG = "[SOCKET] Server Thread";

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
                    Log.d(TAG, "run: server accept!");

                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Object input = inputStream.readObject();
                    Log.d(TAG,"input : "+input);

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(input + "from server");
                    outputStream.flush();
                    Log.d(TAG,"output 보냄");

                    socket.close();
                }
            } catch (Exception e){
                Log.d(TAG, "run: error");
                e.printStackTrace();
            }

        }
    }
}
