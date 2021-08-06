package com.example.socketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements Serializable {
    private static final String TAG = "[SOCKET] Client MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Thread before");
                ClientThread thread = new ClientThread();
                thread.start();
                Log.d(TAG, "onClick: Thread after");
            }
        });
    }

    public class ClientThread extends Thread {

        private static final String TAG = "[SOCKET] Client Thread";

        Handler handler;
        int num;

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
                String send = (String) inputStream.readObject();
                Log.d(TAG, "run: received data: " + send);


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

}