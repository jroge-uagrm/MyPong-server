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

    public Client(String newHost, int newPort, ClientMainThreadEvents events) {
        host = newHost;
        port = newPort;
        clientMainThread = new ClientMainThread(host, port, events);
    }

    public void connect() {
        new Thread(clientMainThread).start();
    }

    public void sendMessage(String msg) {
        clientMainThread.sendMessage(msg);
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean isConnected() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }
}
