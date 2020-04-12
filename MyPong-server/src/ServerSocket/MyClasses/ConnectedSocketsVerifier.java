/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses;

import ServerSocket.Threads.ServerClientThread;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author jorge
 */
class ConnectedSocketsVerifier implements Runnable {

    private final LinkedList<ServerClientThread> connectedClients;
    private boolean verify;

    public ConnectedSocketsVerifier() {
        connectedClients = new LinkedList<>();
    }

    public void addClientToVerify(ServerClientThread client) {
        connectedClients.add(client);
    }

    public void removeClientToVerify(ServerClientThread client) {
        connectedClients.remove(client);
    }

    @Override
    public void run() {
        verify = true;
        ServerClientThread clientInVerification;
        Socket socketInVerification;
        int i = 0;
        System.out.println("Verify start");
        while (verify) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                //Thread.sleep ERROR
            }
            if (connectedClients.isEmpty()) {
                continue;
            }
            i = i >= connectedClients.size() ? 0 : i;
            clientInVerification = connectedClients.get(i);
            socketInVerification = clientInVerification.getSocket();
            try {
                socketInVerification.getInetAddress().isReachable(1000);
                i++;
                System.out.println("Ok:" + socketInVerification.getPort());
            } catch (IOException ex) {
                System.out.println("Bad:" + socketInVerification.getPort());
                onNotReachable(clientInVerification.getKey());
            }
        }
        System.out.println("Verify finish");
    }

    public void stop() {
        verify = false;
    }

    public void onNotReachable(String clientKey) {
    }
}