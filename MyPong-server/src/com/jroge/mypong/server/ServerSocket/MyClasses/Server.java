/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket.MyClasses;

import com.google.gson.Gson;
import com.jroge.mypong.server.ServerSocket.Threads.ServerClientThread;
import com.jroge.mypong.server.ServerSocket.Threads.ServerMainThread;
import com.jroge.mypong.server.ServerSocket.Events.ServerClientThreadEvents;
import com.jroge.mypong.server.ServerSocket.Events.ServerMainThreadEvents;
import java.net.Socket;
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
    }

    public void start() {
        new Thread(serverMainThread).start();
    }

    public void stop() {
        serverMainThread.stop();
        LinkedList<String> aux = new LinkedList<>();
        connectedClients.forEach((String t, ServerClientThread serverClientThread) -> {
            aux.add(t);
        });
        for (String stringKey : aux) {
            connectedClients.get(stringKey).sendMessage("Server stopped");
            connectedClients.get(stringKey).disconnect();
        };
    }

    public void createServerClientThread(Socket clientSocket) {
        String key = generateKey(clientSocket);
        ServerClientThread newServerClientThread = new ServerClientThread(clientSocket, key, clientEvents);
        connectedClients.put(key, newServerClientThread);
        new Thread(newServerClientThread).start();
    }

    public void removeClient(String key) {
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
        return key;
    }

    public void send(String destinationKey, String message) {
        connectedClients.get(destinationKey).sendMessage(message);
    }

    public void sendToEveryone(String body) {
        connectedClients.forEach((String clientKey, ServerClientThread u) -> {
            u.sendMessage(gson.toJson(
                    new ContainerObject(
                            "server",
                            body,
                            clientKey
                    )
            ));
        });
    }

    public void disconnect(ServerClientThread serverClientThread) {
        serverClientThread.disconnect();
    }

    public LinkedList<String> getConnectedClients() {
        LinkedList<String> clientList = new LinkedList<>();
        connectedClients.forEach((String key, ServerClientThread serverClientThread) -> {
            clientList.add(key);
        });
        return clientList;
    }
}
