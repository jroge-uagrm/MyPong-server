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
            mainThreadLog("MainThread:Running...");
        } catch (Exception e) {
            running = false;
            mainThreadLog("MainThread:ERROR on ServerMainThread constructor:"
                    + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                mainThreadLog("MainThread:Waiting a connection...");
                Socket client = serverSocket.accept();
                createServerClientThread(client);
            }
        } catch (Exception e) {
            if (running) {
                mainThreadLog("MainThread:ERROR on run:"
                        + e.getMessage());
                stop();
            } else {
                mainThreadLog("MainThread:Stopped.");
            }
        }
    }

    public void createServerClientThread(Socket clientSocket) {
        mainThreadLog("MainThread:New client connected:" + clientSocket.getInetAddress().getHostAddress());
        new ServerClientThread(clientSocket) {
            @Override
            public void onConnected() {
                asignAThread(this);
                onNewClientConnected(this);
            }

            @Override
            public void onDisconnect() {
                onClientDisconnected(this);
            }

            @Override
            public void clientThreadLog(String msg) {
                mainThreadLog(msg);
            }
        };
    }

    public void asignAThread(ServerClientThread newServerClientThread) {
        new Thread(newServerClientThread).start();
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
                mainThreadLog("MainThread:ERROR on stop" + e.getMessage());
            }
        } else {
            mainThreadLog("MainThread:server is null");
        }
    }

    //Overridables
    public void onNewClientConnected(ServerClientThread newClient) {
    }

    public void onClientDisconnected(ServerClientThread disconnectedClient) {
    }

    public void mainThreadLog(String msg) {
    }
}
