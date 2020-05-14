/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Principals;

import ServerSocket.MyClasses.Auxiliaries.ContainerObject;
import com.google.gson.Gson;
import ServerSocket.Threads.ServerClientThread;
import ServerSocket.Threads.ServerMainThread;
import ServerSocket.Events.ServerClientThreadEvents;
import ServerSocket.Events.ServerMainThreadEvents;
import ServerSocket.MyClasses.Auxiliaries.ConnectedSocketsVerifier;
import ServerSocket.MyClasses.Auxiliaries.Protocol;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author jroge
 */
public class Server {

    private final ServerMainThread serverMainThread;
    private final HashMap<String, ServerClientThread> connectedClients;
    private final ServerClientThreadEvents clientEvents;
    private final Gson gson;
    private final ConnectedSocketsVerifier connectedSocketsVerifier;

    public Server(
            int port,
            ServerMainThreadEvents events,
            ServerClientThreadEvents newClientEvents
    ) {
        serverMainThread = new ServerMainThread(port, events) {
            @Override
            public void onNewClientConnected(Socket clientSocket) {
                createServerClientThread(clientSocket);
            }
        };
        clientEvents = newClientEvents;
        connectedClients = new HashMap<>();
        gson = new Gson();
        connectedSocketsVerifier = new ConnectedSocketsVerifier() {
            @Override
            public void onNotReachable(String clientKey) {
                // removeClient(clientKey);
            }
        };
    }

    public void start() {
        new Thread(serverMainThread).start();
        new Thread(connectedSocketsVerifier).start();
    }

    public void stop() {
        serverMainThread.stop();
        connectedSocketsVerifier.stop();
        LinkedList<String> auxList = new LinkedList<>();
        connectedClients.forEach((String t, ServerClientThread serverClientThread) -> {
            auxList.add(t);
        });
        for (String stringKey : auxList) {
            connectedClients.get(stringKey).sendMessage(gson.toJson(
                    new ContainerObject(
                            "server",
                            new Protocol("serverStopped", ""),
                            new String[]{stringKey}
                    )
            ));
            connectedClients.get(stringKey).disconnect();
            connectedClients.remove(stringKey);
        };
        connectedClients.clear();
    }

    public void createServerClientThread(Socket clientSocket) {
        String key = generateKey(clientSocket);
        ServerClientThread newServerClientThread = new ServerClientThread(clientSocket, key, clientEvents);
        connectedClients.put(key, newServerClientThread);
        new Thread(newServerClientThread).start();
        connectedSocketsVerifier.addClientToVerify(newServerClientThread);
    }

    public void removeClient(String key) {
        connectedSocketsVerifier.removeClientToVerify(connectedClients.get(key));
        ServerClientThread client = connectedClients.get(key);
        if (client != null) {
            client.disconnect();
        }
        connectedClients.remove(key);
    }

    public int getConnectedClientSocketAmount() {
        return connectedClients.size();
    }

    public boolean isRunning() {
        return serverMainThread.isRunning();
    }

    private String generateKey(Socket clientSocket) {
        Calendar calendar = Calendar.getInstance();
        String key = ""
                + clientSocket.getLocalAddress() + "*"
                + clientSocket.getPort() + "*"
                + calendar.getTime();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            byte[] encoded = Base64.getEncoder().encode(digest);
            return new String(encoded);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
            return key;
        }
    }

    public void send(String destinationKey, ContainerObject object) {
        ServerClientThread client = connectedClients.get(destinationKey);
        if (client != null) {
            client.sendMessage(gson.toJson(object));
        }
    }

    public void sendToEveryone(Protocol body) {
        LinkedList<String> auxList = new LinkedList<>();
        connectedClients.forEach((String t, ServerClientThread serverClientThread) -> {
            auxList.add(t);
        });
        for (String clientKey : auxList) {
            connectedClients.get(clientKey).sendMessage(gson.toJson(
                    new ContainerObject(
                            "server",
                            body,
                            new String[]{clientKey}
                    )
            ));
        };
    }

    public LinkedList<String> getConnectedClients() {
        LinkedList<String> clientList = new LinkedList<>();
        connectedClients.forEach((String key, ServerClientThread serverClientThread) -> {
            clientList.add(key);
        });
        return clientList;
    }
}
