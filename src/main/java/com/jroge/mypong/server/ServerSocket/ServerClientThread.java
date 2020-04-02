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

    private final Socket clientSocket;
    private PrintWriter printerWriterOUT;
    private BufferedReader bufferedReaderIN;
    private String name;
    private boolean connected;
    private String key;
    private final ServerClientThreadEvents events;

    public ServerClientThread(Socket socket, ServerClientThreadEvents newEvents) {
        clientSocket = socket;
        name = "UNKNOW";
        events = newEvents;
    }

    @Override
    public void run() {
        try {
            System.out.println("MO");
            connected = true;
            events.onClientConnected(this);
            printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String msg;
            while (connected) {
                msg = bufferedReaderIN.readLine();
                events.onClientNewMessage(this, msg);
            }
        } catch (Exception e) {
            if (connected) {
                internalLog("ERROR(2):" + e.getMessage());
                closeAll();
            }
        } finally {
            events.onClientDisconnected(this);
        }
    }

    public void disconnect() {
        connected = false;
        closeAll();
    }

    private void closeAll() {
        if (clientSocket != null) {
            try {
                if (printerWriterOUT != null) {
                    printerWriterOUT.close();
                }
                if (bufferedReaderIN != null) {
                    bufferedReaderIN.close();
                }
                clientSocket.close();
            } catch (Exception e) {
                internalLog("ERROR(3):" + e.getMessage());
            } finally {
                internalLog("Disconnected.");
            }
        } else {
            internalLog("Never connected.");
        }
    }

    public void sendResponse(String msg) {
        try {
            printerWriterOUT.println(msg);
        } catch (Exception e) {
            internalLog("ERROR(4):" + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setKey(String newKey) {
        key = newKey;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public void internalLog(String msg) {
        events.onClientLog(name + ".t:" + msg);
    }
}
