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

    private final ServerMainThread serverMainThread;

    public Server(int port) {
        serverMainThread = new ServerMainThread(port) {
            @Override
            public void onStarted() {
                onServerStarted();
            }

            @Override
            public void onStopped() {
                onServerStopped();
            }

            @Override
            public void onNewClientConnected(Socket newClientSocket) {
                createServerClientThread(newClientSocket);
            }

            @Override
            public void mainThreadLog(String msg) {
                onServerLog(msg);
            }
        };
    }

    public void start() {
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
            }

            @Override
            public void onNewMessage(String msg) {
                onNewMessageFromClient(this, msg);
            }

            @Override
            public void onDisconnected() {
                onClientDisconnected(this);
            }

            @Override
            public void clientThreadLog(String msg) {
                onClientsLog(msg);
            }
        };
        onClientConnected(newServerClientThread);
        new Thread(newServerClientThread).start();
    }

    public boolean isRunning() {
        return serverMainThread.isRunning();
    }

    //Overridables
    public void onServerStarted() {
    }

    public void onServerStopped() {
    }

    public void onClientConnected(ServerClientThread newClient) {
    }

    public void onNewMessageFromClient(ServerClientThread clientSender, String msg) {
    }

    public void onClientDisconnected(ServerClientThread disconnectedClient) {
    }

    public void onServerLog(String msg) {
    }
    
    public void onClientsLog(String msg) {
    }

}
