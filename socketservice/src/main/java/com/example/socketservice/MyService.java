package com.example.socketservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Service {

    private static final String TAG = "[SOCKET] Service";
    private static String textMessage;

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
            ServerThread serverThread = new ServerThread();
            serverThread.start();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
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
            while(loop){
                try{
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "run: accept!");

                    writeMessageThread writeMessageThread = new writeMessageThread(socket);
                    writeMessageThread.start();


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



    class writeMessageThread extends Thread{
        private Socket socket;

        public writeMessageThread(Socket socket){
            Log.d(TAG, "writeMessageThread: ");
            this.socket = socket;
        }

        @Override
        public void run() {
            //Message message = new Message(); this is for message we will use later
            ObjectOutputStream oos = null;

            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(textMessage);
                oos.flush();
                Log.d(TAG, "run: send: " + textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}