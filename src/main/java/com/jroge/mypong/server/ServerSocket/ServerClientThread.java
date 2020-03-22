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
    private boolean running;

    public ServerClientThread(Socket socket) {
        try {
            clientSocket = socket;
            running = true;
            onConnected();
            clientThreadLog("MainThread:Connected.");
        } catch (Exception e) {
            running = false;
            clientThreadLog("MainThread:ERROR on constructor:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        printerWriterOUT = null;
        bufferedReaderIN = null;
        try {
            while (running) {
                printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
                bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = bufferedReaderIN.readLine()) != null) {
                    clientThreadLog("ClientThread:New message:" + line);
                    manageMessage(line);
                }
            }
        } catch (Exception e) {
            if (running) {
                clientThreadLog("ClientThread:ERROR on run:" + e.getMessage());
                stop();
            } else {
                clientThreadLog("ClientThread:Stopped.ERROR");
            }
        }
    }

    public void manageMessage(String messageFromClient) {
        String response = "Ping received";
        if (messageFromClient.equals("Hola")) {
            response = "Hola! como estas?";
        } else if (messageFromClient.equals("Chau")) {
            response = "No te vayas!! :(";
        }else if(messageFromClient.equals("Disconnected")){
            onDisconnect();
        }
        sendMessage(response);
    }

    public void sendMessage(String msg) {
        try {
            printerWriterOUT.println(msg);
        } catch (Exception e) {
            clientThreadLog("ClientThread:ERROR on sendMessage:" + e.getMessage());
        }
    }

    public void stop() {
        if (clientSocket != null) {
            try {
                if (printerWriterOUT != null) {
                    printerWriterOUT.close();
                }
                if (bufferedReaderIN != null) {
                    bufferedReaderIN.close();
                }
                clientSocket.close();
                clientThreadLog("ClientThread:Stopped.");
            } catch (Exception e) {
                clientThreadLog("ClientThread:ERROR on stop:" + e.getMessage());
            }
        } else {
            clientThreadLog("ClientThread:clientSocket is null");
        }
        running = false;
    }

    public boolean getRunningState() {
        return running;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    //Overridables
    public void onConnected() {
    }

    public void onDisconnect() {
    }

    public void clientThreadLog(String msg) {
    }
}
