package com.example.socketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "[SOCKET] Client";

    TextView Changetext;
    Button ChangeButton;
    Button newButton;
    TextView newText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cSocket:
                Log.d(TAG, "onClick: Thread before");
                ClientThread thread = new ClientThread();
                thread.start();
                Log.d(TAG, "onClick: Thread after");
                break;
        }
    }

    public void newLayout(String data){
        LinearLayout linear = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linear.setLayoutParams(params);
        linear.setOrientation(LinearLayout.VERTICAL);

        newText = new TextView(this);
        newButton = new Button(this);

        newText.setText(data);
        newButton.setText("Press Button to Change Color");
        linear.addView(newText);
        linear.addView(newButton);

        setContentView(linear);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int red = random.nextInt(255);
                int green = random.nextInt(255);
                int blue = random.nextInt(255);
                newText.setTextColor(Color.rgb(red, green, blue));
            }
        });

    }

    public class ClientThread extends Thread {
        private static final String TAG = "[SOCKET] Client Thread";

        Handler handler = new Handler();
        int num;

        @Override
        public void run() {
            String target = "192.168.0.21";
            int port = 5672;

            try{
                Log.d(TAG, "run: make socket object");
                Socket socket = new Socket();
                Log.d(TAG, "run: make socket address");
                SocketAddress socketAddress = new InetSocketAddress(target, port);
                Log.d(TAG, "run: connect socket");
                socket.connect(socketAddress, port);
                Log.d(TAG, "run: make Socket");

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String message = (String) ois.readObject();

                Log.d(TAG, "run: message: " + message);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            newLayout(message);
                            Log.d(TAG, "run: new layout");

                        } catch (Exception e){
                            Log.d(TAG, "error: " + e);
                            e.printStackTrace();
                        }
                    }
                });

            } catch (UnknownHostException e){
                Log.d(TAG, "run: unknown host exception");
                e.printStackTrace();
            } catch (IOException e){
                Log.d(TAG, "run: IO exception");
                e.printStackTrace();
            } catch (Exception e){
                Log.d(TAG, "run: Other exception: " + e.getMessage());
            }
        }
    }

}