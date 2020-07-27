/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.MyClasses.Principals;

import ClientSocket.MyClasses.Auxiliaries.ContainerObject;
import ClientSocket.Threads.ClientMainThread;
import ClientSocket.Events.ClientMainThreadEvents;

/**
 *
 * @author jroge
 */
public class Client {

    private final ClientMainThread clientMainThread;

    public Client(String host, int port, ClientMainThreadEvents events) {
        clientMainThread = new ClientMainThread(host, port, events);
    }

    public void connect() {
        new Thread(clientMainThread).start();
    }

    public void send(ContainerObject object) {
        clientMainThread.sendMessage(object);
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean isConnected() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }
}
