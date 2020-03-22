/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author jroge
 */
public class Server {

    private ServerMainThread serverMainThread;
    private int port;
    private LinkedList<Socket> connectedClientSockets;

    public Server(int newPort) {
        port = newPort;
    }

    private void addClientSocket(Socket newClientSocket) {
        connectedClientSockets.add(newClientSocket);
        onNewClientConnected(newClientSocket);
    }

    public void start() {
        serverMainThread = new ServerMainThread(port) {
            @Override
            public void onNewClientConnected(Socket newClientSocket) {
                addClientSocket(newClientSocket);
            }

            @Override
            public void mainThreadLog(String msg) {
                serverLog(msg);
            }
        };
        connectedClientSockets = new LinkedList<>();
        new Thread(serverMainThread).start();
    }

    public void stop() {
        serverMainThread.stop();
    }

    public boolean getRunningState() {
        return serverMainThread != null && serverMainThread.getRunningState();
    }

    //Overridables
    public void onNewClientConnected(Socket newClientSocket) {
    }

    public void serverLog(String msg) {
    }

}
