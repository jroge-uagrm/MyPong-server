/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author jroge
 */
public class ServerMainThread implements Runnable {

    public ServerSocket serverSocket = null;
    protected boolean running;

    public ServerMainThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            running = true;
//            new Thread(this).start();
            internalLog("Running...");
        } catch (Exception e) {
            running = false;
            internalLog("ERROR on ServerMainThread constructor:"
                    + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                internalLog("Waiting a connection...");
                Socket client = serverSocket.accept();
                onNewClientConnected(client);
            }
        } catch (Exception e) {
            if (running) {
                internalLog("ERROR on run:"
                        + e.getMessage());
                stop();
            } else {
                internalLog("Stopped.");
            }
        }
    }

    public boolean getRunningState() {
        return running;
    }

    public void stop() {
        running = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                internalLog("ERROR on stop" + e.getMessage());
            }
        } else {
            internalLog("server is null");
        }
    }

    private void internalLog(String msg) {
        mainThreadLog("Server.thread:" + msg);
    }

    //Overridables
    public void onNewClientConnected(Socket newClient) {
    }

    public void mainThreadLog(String msg) {
    }
}
