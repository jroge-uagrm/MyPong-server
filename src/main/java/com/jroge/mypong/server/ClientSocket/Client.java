/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ClientSocket;

/**
 *
 * @author jroge
 */
public class Client {

    private final ClientMainThread clientMainThread;
    private final String host;
    private final int port;

    public Client(String newHost, int newPort) {
        host = newHost;
        port = newPort;
        clientMainThread = new ClientMainThread(host, port) {
            @Override
            public void onClientConnected() {
                onConnected();
            }

            @Override
            public void onClientNewResponse(String response) {
                onNewResponse(response);
            }

            @Override
            public void onClientTryingReconnect() {
                onTryingReconnect();
            }

            @Override
            public void onClientDisconnected() {
                onDisconnected();
            }

            @Override
            public void onClientConnectionLost() {
                connect();
            }

            @Override
            public void mainThreadLog(String msg) {
                clientLog(msg);
            }
        };
    }

    public void connect() {
        new Thread(clientMainThread).start();
    }

    public void sendMessage(String msg) {
        clientMainThread.setInformation(msg);
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean isConnected() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }

    //Overridables
    public void onConnected() {
    }

    public void onNewResponse(String response) {
    }

    public void onTryingReconnect() {
    }

    public void onDisconnected() {
    }

    public void clientLog(String msg) {
    }
}
