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
    private final int port;
    private LinkedList<Socket> connectedClientSockets;

    public Server(int newPort) {
        port = newPort;
        connectedClientSockets = new LinkedList<>();
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
        new Thread(serverMainThread).start();
    }

    public void stop() {
        serverMainThread.stop();
    }

    private void addClientSocket(Socket newClientSocket) {
        connectedClientSockets.add(newClientSocket);
        onNewClientConnected(newClientSocket);
    }

    public int getConnectedClientSocketAmount() {
        return connectedClientSockets.size();
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
