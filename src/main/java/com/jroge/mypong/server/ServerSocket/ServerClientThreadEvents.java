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
public interface ServerClientThreadEvents {

    public void onClientConnected(ServerClientThread serverClientThread);

    public void onClientNewMessage(ServerClientThread serverClientThread, String messageFromClient);

    public void onClientDisconnected(ServerClientThread serverClientThread);

    public void onClientLog(String msg);
}
