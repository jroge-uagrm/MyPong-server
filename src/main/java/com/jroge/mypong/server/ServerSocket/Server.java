/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

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
    }

    public void start() {
        new Thread(serverMainThread).start();
    }

    public void stop() {
        serverMainThread.stop();
        LinkedList<String> aux = new LinkedList<>();
        connectedClients.forEach((String t, ServerClientThread serverClientThread) -> {
            //serverClientThread.sendResponse("Server stopped");
            aux.add(t);
        });
        for (String stringKey : aux) {
            connectedClients.get(stringKey).sendResponse("Server stopped");
            connectedClients.get(stringKey).disconnect();
        };
    }

    public void createServerClientThread(Socket clientSocket) {
        ServerClientThread newServerClientThread = new ServerClientThread(clientSocket, clientEvents);
        String key = generateKey(newServerClientThread);
        newServerClientThread.setKey(key);
        connectedClients.put(key, newServerClientThread);
        new Thread(newServerClientThread).start();
    }

    public void removeClient(ServerClientThread connectedClientSocket) {
        connectedClients.remove(connectedClientSocket.getKey());
    }

    public int getConnectedClientSocketAmount() {
        return connectedClients.size();
    }

    public boolean isRunning() {
        return serverMainThread.isRunning();
    }

    private String generateKey(ServerClientThread newServerClientThread) {
        Socket clientSocket = newServerClientThread.getSocket();
        Calendar calendar = Calendar.getInstance();
        String key = ""
                + clientSocket.getLocalAddress() + "*"
                //+ clientSocket.getLocalSocketAddress() + "*"
                //+ clientSocket.getLocalPort() + "*"
                + clientSocket.getPort() + "*"
                + calendar.getTime();
        System.out.println("NEW: " + key);
        return key;
    }

    public void send(ServerClientThread serverClientThread, String response) {
        serverClientThread.sendResponse(response);
    }

    public void sendToEveryone(String response) {
        connectedClients.forEach((String t, ServerClientThread u) -> {
            u.sendResponse(response);
        });
    }

    public void disconnect(ServerClientThread serverClientThread) {
        serverClientThread.disconnect();
    }

}
