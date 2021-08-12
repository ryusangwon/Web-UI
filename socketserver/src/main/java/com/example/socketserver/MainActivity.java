package com.example.socketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "[SOCKET] Server MainActivity";
    Button start, stop, send;
    TextView sendText;
    EditText writeText;
    String data = "";

    ServerThread thread = new ServerThread();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        send = (Button) findViewById(R.id.set);
        sendText = (TextView) findViewById(R.id.sendText);
        writeText = (EditText) findViewById(R.id.inputText);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                thread.start();
                Log.d(TAG, "onClick: ");
                break;

            case R.id.stop:
                thread.interrupt();
                break;

            case R.id.set:
                String textTemp = writeText.getText().toString();
                sendText.setText(textTemp);
                break;

        }
    }

    public class ServerThread extends Thread {
        private static final String TAG = "[SOCKET] Server MainActivity Thread";

        String event;

        @Override
        public void run() {
            Log.d(TAG, "run: ");
            int port = 5672;
            try {

                Button change = (Button) findViewById(R.id.Change);
                change.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: set event");
                        event = "Button";

                    }
                });

                ServerSocket server = new ServerSocket(port);

                while(true){
                    Log.d(TAG, "run: server accept?");
                    Socket socket = server.accept();
                    Log.d(TAG, "run: server accept!");


                    data = "";
                    data += sendText.getText();

                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    oos.writeObject(data);
                    oos.writeObject(event);

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