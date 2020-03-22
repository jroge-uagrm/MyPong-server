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

    public ServerSocket server = null;
    protected boolean running;

    public ServerMainThread(int port) {
        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);
            running = true;
            mainThreadLog("MainThread:Running...");
        } catch (Exception e) {
            mainThreadLog("MainThread:ERROR on ServerMainThread constructor:"
                    + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                mainThreadLog("MainThread:Waiting a connection...");
                Socket client = server.accept();
                asignAThreadToClient(client);
            }
        } catch (Exception e) {
            if (running) {
                mainThreadLog("MainThread:ERROR on ServerMainThread run:"
                        + e.getMessage());
                stop();
            } else {
                mainThreadLog("MainThread:Stopped.");
            }
        }
    }

    public void asignAThreadToClient(Socket clientSocket) {
        mainThreadLog("MainThread:New client connected:" + clientSocket.getInetAddress().getHostAddress());
        ServerClientThread clientSock = new ServerClientThread(clientSocket);
        new Thread(clientSock).start();
        onNewClientConnected(clientSocket);
    }

    public boolean getRunningState() {
        return running;
    }

    public void stop() {
        running = false;
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {
                mainThreadLog("MainThread:ERROR on stop" + e.getMessage());
            }
        } else {
            System.out.println("MainThread:server is null");
        }
    }

    //Overridables
    public void onNewClientConnected(Socket clientSocket) {
    }

    public void mainThreadLog(String msg) {
    }
}
