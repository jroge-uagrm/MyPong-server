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

    public ServerClientThread(Socket socket) {
        clientSocket = socket;
        name = "UNKNOW";
    }

    @Override
    public void run() {
        try {
            connected = true;
            onConnected();
            while (connected) {
                printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
                bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;
                while ((msg = bufferedReaderIN.readLine()) != null) {
                    onNewMessage(msg);
                }
            }
        } catch (Exception e) {
            if (connected) {
                internalLog("ERROR(2):" + e.getMessage());
                closeAll();
            }
        }
    }

    public boolean disconnect() {
        connected = false;
        closeAll();
        return true;
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
                onDisconnected();
            }
        } else {
            internalLog("Never connected.");
        }
    }

    public void sendMessage(String msg) {
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
        name = newName + ".t";
    }

    public void internalLog(String msg) {
        clientThreadLog(name + ":" + msg);
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
