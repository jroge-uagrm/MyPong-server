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

    private ClientMainThread clientMainThread;
    private String host;
    private int port;

    public Client(String newHost, int newPort) {
        host = newHost;
        port = newPort;
    }

    public void connect() {
        clientMainThread = new ClientMainThread(host, port) {
            @Override
            public void mainThreadLog(String msg) {
                clientLog(msg);
            }
        };
        new Thread(clientMainThread).start();
    }

    public void sendMessage(String msg) {
        clientMainThread.setInformation(msg);
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean getConnectedState() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }

    //Overridables
    public void clientLog(String msg) {
    }
}
