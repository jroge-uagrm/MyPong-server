/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author jroge
 */
public class Client {

    private ClientMainThread clientMainThread;
    private String host;
    private int port;

    public Client(String newHost, int newPort) {
        host = newHost;
        port = newPort;
    }

    public void connect() {
        clientMainThread = new ClientMainThread(host, port) {
            @Override
            public void mainThreadLog(String msg) {
                clientLog(msg);
            }
        };
        new Thread(clientMainThread).start();
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean getConnectedState() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }

    //Overridables
    public void clientLog(String msg) {
    }
}
