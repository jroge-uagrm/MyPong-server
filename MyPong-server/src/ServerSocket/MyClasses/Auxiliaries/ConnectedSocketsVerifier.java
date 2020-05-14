/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Auxiliaries;

import ServerSocket.Threads.ServerClientThread;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author jorge
 */
public class ConnectedSocketsVerifier implements Runnable {

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
        Iterator<ServerClientThread> iterator = connectedClients.iterator();
        System.out.println("Verify start");
        while (verify) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                //Thread.sleep ERROR
            }
            if (!connectedClients.isEmpty()) {
                iterator = connectedClients.iterator();
            } else {
                continue;
            }
            clientInVerification = iterator.next();
            socketInVerification = clientInVerification.getSocket();
//            Calendar time = ;
            try {
                socketInVerification.getInetAddress().isReachable(1000);
                log(
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE)
                        + ":Ok:" + socketInVerification.getPort());
            } catch (IOException ex) {
                log(
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE)
                        + ":Bad:" + socketInVerification.getPort());
                onNotReachable(clientInVerification.getKey());
            } finally {
                if (!iterator.hasNext()) {
                    iterator = connectedClients.iterator();
                }
            }
        }
        System.out.println("Verify finish");
    }

    public void stop() {
        verify = false;
    }
    
    private void log(String msg){
//        System.out.println(msg);
    }

    public void onNotReachable(String clientKey) {
    }
}
