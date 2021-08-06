package com.example.socketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "[SOCKET] Server MainActivity";
    Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                //Log.d(TAG, "onClick: Service Start Button");
                //Intent intent = new Intent(this, socketService.class);
                //startService(intent);
                //Log.d(TAG, "onCreate: start service");

                ServerThread thread = new ServerThread();
                thread.start();
                Log.d(TAG, "onClick: ");
                break;

            case R.id.stop:
                //Log.d(TAG, "onClick: Service Stop Button");
                //stopService(new Intent(this, socketService.class));
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

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject("Hello");
                    Log.d(TAG, "run: send Object");

                    outputStream.flush();

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