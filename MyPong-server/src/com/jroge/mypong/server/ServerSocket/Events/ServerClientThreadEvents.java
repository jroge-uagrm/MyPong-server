/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket.Events;

import com.jroge.mypong.server.ServerSocket.MyClasses.ContainerObject;
import com.jroge.mypong.server.ServerSocket.Threads.ServerClientThread;

/**
 *
 * @author jorge
 */
public interface ServerClientThreadEvents {

    public void onClientConnected(String key);

    public void onClientNewMessage(ContainerObject object);

    public void onClientDisconnected(String key);

    public void onClientLog(String msg);
}
