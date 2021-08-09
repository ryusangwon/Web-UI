package com.example.socketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "[SOCKET] Server MainActivity";
    Button start, stop, send;
    TextView sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        send = (Button) findViewById(R.id.send);
        sendText = (TextView) findViewById(R.id.sendText);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                ServerThread thread = new ServerThread();
                thread.start();
                Log.d(TAG, "onClick: ");
                break;

            case R.id.stop:
                break;
        }
    }

    public class ServerThread extends Thread {
        private static final String TAG = "[SOCKET] Server MainActivity Thread";

        Handler handler;
        int num;

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

                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject("sendText");
                    Log.d(TAG, "run: send Object");

                    oos.flush();

                    socket.close();
                }

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