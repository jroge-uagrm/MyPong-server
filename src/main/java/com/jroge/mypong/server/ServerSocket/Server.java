/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.net.Socket;

/**
 *
 * @author jroge
 */
public class Server {

    private ServerMainThread serverMainThread;
    private final int port;

    public Server(int newPort) {
        port = newPort;
    }

    public void start() {
        serverMainThread = new ServerMainThread(port) {
            @Override
            public void onServerStarted() {
                onStarted();
            }

            @Override
            public void onNewClientConnected(Socket newClient) {
                createServerClientThread(newClient);
            }

            @Override
            public void onServerStopped() {
                onStopped();
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

    private void createServerClientThread(Socket clientSocket) {
        ServerClientThread newServerClientThread
                = new ServerClientThread(clientSocket) {
            @Override
            public void onConnected() {
                onClientConnected(this);
            }

            @Override
            public void onDisconnected() {
                onClientDisconnected(this);
            }

            @Override
            public void onNewMessage(String msg) {
                onClientNewMessage(this, msg);
            }

            @Override
            public void clientThreadLog(String msg) {
                serverLog(msg);
            }
        };
        new Thread(newServerClientThread).start();
        onNewClientConnected(newServerClientThread);
    }

    public boolean getRunningState() {
        return serverMainThread != null && serverMainThread.getRunningState();
    }

    //Overridables
    public void onStarted() {
    }

    public void onStopped() {
    }

    public void onClientConnected(ServerClientThread connectedClient) {
    }

    public void onClientNewMessage(ServerClientThread clientSender, String msg) {
    }

    public void onClientDisconnected(ServerClientThread disconnectedClient) {
    }

    public void serverLog(String msg) {
    }

    public void onNewClientConnected(ServerClientThread newServerClientThread) {
    }
}
