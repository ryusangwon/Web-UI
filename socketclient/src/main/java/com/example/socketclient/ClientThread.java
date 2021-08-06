package com.example.socketclient;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import static android.os.Build.VERSION.SDK_INT;

public class ClientThread extends  Thread {

    private static final String TAG = "[SOCKET] Client Thread";

    @Override
    public void run() {
        String target = "192.168.0.22";
        int port = 5672;

        try{
            Log.d(TAG, "run: make socket object");
            Socket socket = new Socket();
            Log.d(TAG, "run: make socket address");
            SocketAddress socketAddress = new InetSocketAddress(target, port);
            Log.d(TAG, "run: connect socket");
            socket.connect(socketAddress, port);
            Log.d(TAG, "run: make Socket");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            final String input = (String) inputStream.readObject();
            Log.d(TAG, "run: received data: " + input);


        } catch (UnknownHostException e){
            Log.d(TAG, "run: unknown host exception");
            e.printStackTrace();
        } catch (IOException e){
            Log.d(TAG, "run: IO exception");
            e.printStackTrace();
        } catch (Exception e){
            Log.d(TAG, "run: Other exception");
            e.printStackTrace();
        }
    }
}
