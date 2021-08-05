package com.example.socketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
                Log.d(TAG, "onClick: Service Start Button");
                Intent intent = new Intent(this, socketService.class);
                startService(intent);
                Log.d(TAG, "onCreate: start service");
                break;

            case R.id.stop:
                Log.d(TAG, "onClick: Service Stop Button");
                stopService(new Intent(this, socketService.class));
                break;
        }
    }
}