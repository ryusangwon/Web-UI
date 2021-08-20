package com.example.socketservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyService extends Service {

    private static final String TAG = "[SOCKET] Service";
    private static String message;

    public Binder mBinder = new IServiceInterface.Stub() {

        @Override
        public void setMessage(String text) throws RemoteException {
            message = text;
            //Log.d(TAG, "setMessage: " + message);
        }

        @Override
        public String getMessage() throws RemoteException {
            Log.d(TAG, "getMessage: " + message);
            return message;
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
        @Override
        public void run() {
            Log.d(TAG, "run: accept?");
            int port = 5672;
            
            try{
                ServerSocket serverSocket = new ServerSocket(port);
                
                while (true){
                    Socket socket = serverSocket.accept();
                    Log.d(TAG, "run: accept!");

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    oos.flush();
                    Log.d(TAG, "run: send: " + message);
                    socket.close();                    
                }
                
            } catch (UnknownHostException e){
                Log.d(TAG, "run: unknown host exception");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "run: IO exception");
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}