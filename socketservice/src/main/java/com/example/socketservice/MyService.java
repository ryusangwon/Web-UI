package com.example.socketservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class MyService extends Service {

    private static final String TAG = "[SOCKET] Service";
    private static String textMessage;
    private static String eventNameMessage;

    ServerThread serverThread = new ServerThread();

    /*
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            eventNameMessage = String.valueOf(msg.what);
            return true;
        }
    });
     */

    Handler handler = new Handler();

    Boolean eventTurn=false;

    public Binder mBinder = new IServiceInterface.Stub() {

        @Override
        public void setMessage(String text) throws RemoteException {
            textMessage = text;
            //Log.d(TAG, "setMessage: " + message);
        }

        @Override
        public String getMessage() throws RemoteException {
            Log.d(TAG, "getMessage: " + textMessage);
            return textMessage;
        }

        @Override
        public void serviceThreadStart() throws RemoteException {
            Log.d(TAG, "serviceThreadStart: ");
            serverThread.start();
        }

        @Override
        public void handleSocketEvent(String eventName) throws RemoteException {
            eventNameMessage = eventName;
            Log.d(TAG, "handleSocketEvent: eventName: " + eventNameMessage);
            //handleEventThread handleEventThread = new handleEventThread(eventName);
            //handleEventThread.start();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        /*
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String event = msg.obj.toString();
            }
        };
         */
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

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    class ServerThread extends Thread{
        private Boolean loop;
        ServerSocket serverSocket;

        public ServerThread() {
            Log.d(TAG, "ServerThread: accept?");
            int port = 5672;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "ServerThread: accept!");
            loop = true;
        }

        @Override
        public void run() {
            Looper.prepare();

            if (eventTurn != true){
                try{
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "run: accept!");

                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(textMessage);
                        oos.flush();
                        Log.d(TAG, "run: send: " + textMessage);
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "run: loop: " + loop);

            while(loop){
                try{
                    Log.d(TAG, "run: socket connect please");
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "run: Write Message Thread " + Looper.myLooper());

                    /*
                    writeMessageThread writeMessageThread = new writeMessageThread(socket);
                    writeMessageThread.start();
                    */
                    writeMessageMethod(socket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try{
                if (serverSocket != null){
                    Log.d(TAG, "run: Server Socket Closed");
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Looper.loop();
        }
    }
    public void writeMessageMethod(Socket socket){
        try {
            eventTurn=false;
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(eventNameMessage);
            oos.flush();
            Log.d(TAG, "writeMessageMethod: " + eventNameMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    class writeMessageThread extends Thread{
        private Socket socket;

        public writeMessageThread(Socket socket){
            Log.d(TAG, "writeMessageThread: ");
            this.socket = socket;
        }

        @Override
        public void run() {
            Looper.prepare();
            //Message message = new Message(); this is for message we will use later

            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(eventNameMessage);
                oos.flush();
                Log.d(TAG, "writeMessageThread: " + eventNameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Looper.loop();
        }
    }

     */


    /*
    class handleEventThread extends Thread{
        public handleEventThread(String eventName){
            eventNameMessage = eventName;
        }

        @Override
        public void run() {
            Looper.prepare();
            //Message message = new Message(); this is for message we will use later

            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(eventNameMessage);
                oos.flush();
                Log.d(TAG, "writeMessageThread: " + eventNameMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Looper.loop();
        }
    }

     */



    /*
    class handleEventThread extends Thread{

        public handleEventThread(String eventName){
            eventNameMessage = eventName;
        }

        @Override
        public void run() {
            Looper.prepare();

            Message message = new Message();

            switch (eventNameMessage){
                case "Button":
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = Message.obtain(handler, new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }).start();
            }

            handler.sendMessage(message);

            Looper.loop();
        }
    }

     */

}