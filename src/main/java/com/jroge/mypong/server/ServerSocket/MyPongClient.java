/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

/**
 *
 * @author jorge
 */
public class MyPongClient {

    private ServerClientThread thread;
    private String name;

    public MyPongClient(ServerClientThread newThread) {
        thread = newThread;
    }

    public void setName(String newName) {
        thread.setName(newName);
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void disconnect() {
        sendMessage("Server stopped");
    }

    public void sendMessage(String msg) {
        thread.sendMessage(msg);
    }

    public int getHash() {
        return thread.hashCode();
    }
}
