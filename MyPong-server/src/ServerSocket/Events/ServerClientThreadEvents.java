/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.Events;

import ServerSocket.MyClasses.Auxiliaries.ContainerObject;
import ServerSocket.Threads.ServerClientThread;

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
