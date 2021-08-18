// IServiceInterface.aidl
package com.example.socketservice;

// Declare any non-default types here with import statements

interface IServiceInterface {

    void setMessage(String text);
    String getMessage();
    void serviceThreadStart();

}