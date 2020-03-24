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

    private ServerSocket serverSocket = null;
    private int port;
    protected boolean running;

    public ServerMainThread(int newPort) {
        port = newPort;
    }

    @Override
    public void run() {
        tryToConnectToSocket();
        try {
            while (running) {
                Socket client = serverSocket.accept();
                onNewClientConnected(client);
            }
        } catch (Exception e) {
            if (running) {
                internalLog("ERROR on run:"
                        + e.getMessage());
            }
        }
    }

    private void tryToConnectToSocket() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            running = true;
            internalLog("Running...");
            onServerStarted();
        } catch (Exception e) {
            running = false;
            internalLog("ERROR on ServerMainThread constructor:"
                    + e.getMessage());
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
                internalLog("Stopped.");
            } catch (Exception e) {
                internalLog("ERROR on stop" + e.getMessage());
            }
        } else {
            internalLog("server is null");
        }
        onServerStopped();
    }

    private void internalLog(String msg) {
        mainThreadLog("Server.thread:" + msg);
    }

    //Overridables
    public void onServerStarted() {
    }

    public void onNewClientConnected(Socket newClient) {
    }

    public void onServerStopped() {
    }

    public void mainThreadLog(String msg) {
    }
}
