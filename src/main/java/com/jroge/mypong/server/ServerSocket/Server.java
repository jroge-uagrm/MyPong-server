/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;

/**
 *
 * @author jroge
 */
public class Server {

    private final ServerMainThread serverMainThread;
    private final HashMap<String, ServerClientThread> connectedMyPongClients;
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
        connectedMyPongClients = new HashMap<>();
    }

    public void start() {
        new Thread(serverMainThread).start();
    }

    public void stop() {
        connectedMyPongClients.forEach((String t, ServerClientThread serverClientThread) -> {
            serverClientThread.sendResponse("Server stopped");
        });
        serverMainThread.stop();
    }

    public void createServerClientThread(Socket clientSocket) {
        ServerClientThread newServerClientThread = new ServerClientThread(clientSocket, clientEvents);
        String key = generateKey(newServerClientThread);
        newServerClientThread.setKey(key);
        connectedMyPongClients.put(key, newServerClientThread);
        new Thread(newServerClientThread).start();
    }

    public void removeClient(ServerClientThread connectedClientSocket) {
        System.out.println(connectedClientSocket.getKey() + "LULU");
        System.out.println(connectedClientSocket);
        connectedMyPongClients.remove(connectedClientSocket.getKey());
    }

    public int getConnectedClientSocketAmount() {
        return connectedMyPongClients.size();
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
        connectedMyPongClients.forEach((String t, ServerClientThread u) -> {
            u.sendResponse(response);
        });
    }

    public void disconnect(ServerClientThread serverClientThread) {
        serverClientThread.disconnect();
    }

}
