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
    private LinkedList<ServerClientThread> connectedClientSockets;

    public Server(int newPort) {
        port = newPort;
        connectedClientSockets = new LinkedList<>();
    }

    public void start() {
        serverMainThread = new ServerMainThread(port) {
            @Override
            public void onNewClientConnected(ServerClientThread newClient) {
                addClientSocket(newClient);
            }

            @Override
            public void onClientDisconnected(ServerClientThread disconnectedClient) {
                removeClient(disconnectedClient);
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

    private void addClientSocket(ServerClientThread newClientSocket) {
        connectedClientSockets.add(newClientSocket);
        onNewClientConnected(newClientSocket);
    }

    private void removeClient(ServerClientThread disconnectedClient) {
        connectedClientSockets.remove(disconnectedClient);
        onClientDisconnected(disconnectedClient);
    }

    public int getConnectedClientSocketAmount() {
        return connectedClientSockets.size();
    }

    public boolean getRunningState() {
        return serverMainThread != null && serverMainThread.getRunningState();
    }

    //Overridables
    public void onNewClientConnected(ServerClientThread newClient) {
    }

    public void onClientDisconnected(ServerClientThread disconnectedClient) {
    }

    public void serverLog(String msg) {
    }

}
