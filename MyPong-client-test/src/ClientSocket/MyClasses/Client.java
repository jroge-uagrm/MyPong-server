/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.MyClasses;

import ClientSocket.Threads.ClientMainThread;
import ClientSocket.Events.ClientMainThreadEvents;

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

    public void sendMessage(ContainerObject object) {
        clientMainThread.sendMessage(object);
    }

    public void disconnect() {
        clientMainThread.disconnect();
    }

    public boolean isConnected() {
        return clientMainThread != null && clientMainThread.getConnectedStatus();
    }
}
