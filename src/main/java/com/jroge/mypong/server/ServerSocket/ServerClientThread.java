/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jroge
 */
public class ServerClientThread implements Runnable {

    private Socket clientSocket;
    private PrintWriter printerWriterOUT;
    private BufferedReader bufferedReaderIN;
    private String name;
    private boolean running;

    public ServerClientThread(Socket socket) {
        running = false;
        clientSocket = socket;
    }

    @Override
    public void run() {
        tryToConnectToSocket();
        printerWriterOUT = null;
        bufferedReaderIN = null;
        try {
            while (running) {
                printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
                bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = bufferedReaderIN.readLine()) != null) {
                    manageMessage(line);
                }
            }
        } catch (Exception e) {
            if (running) {
                internalLog("ERROR on run:" + e.getMessage());
            }
        }
    }

    private void tryToConnectToSocket() {
        try {
            running = true;
            internalLog("New client connected.");
            onConnected();
        } catch (Exception e) {
            running = false;
            internalLog("ERROR on constructor:" + e.getMessage());
        }
    }

    private void manageMessage(String messageFromClient) {
        String response = "Ping received";
        if (messageFromClient.equals("Disconnected")) {
            disconnect();
        } else if (!messageFromClient.equals("ping from client")) {
            internalLog("New message:" + messageFromClient);
            onNewMessage(messageFromClient);
        } else {
            internalLog("Ping done");
            sendResponse(response);
        }
    }

    public void sendResponse(String msg) {
        try {
            if (!msg.equals("Ping received")) {
                internalLog("Sending...:" + msg);
            }
            printerWriterOUT.println(msg);
        } catch (Exception e) {
            internalLog("ERROR on sendMessage:" + e.getMessage());
        }
    }

    public void disconnect() {
        running = false;
        if (clientSocket != null) {
            try {
                if (printerWriterOUT != null) {
                    printerWriterOUT.close();
                }
                if (bufferedReaderIN != null) {
                    bufferedReaderIN.close();
                }
                clientSocket.close();
                internalLog("Disconnected.");
                onDisconnected();
            } catch (Exception e) {
                internalLog("ERROR on disconnect:" + e.getMessage());
            }
        } else {
            internalLog("clientSocket is null");
        }
    }

    public boolean getRunningState() {
        return running;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    private void internalLog(String msg) {
        clientThreadLog((name == null ? "unkwon" : name) + ".thread:" + msg);
    }

    //Overridables
    public void onConnected() {
    }

    public void onNewMessage(String msg) {
    }

    public void onDisconnected() {
    }

    public void clientThreadLog(String msg) {
    }
}
