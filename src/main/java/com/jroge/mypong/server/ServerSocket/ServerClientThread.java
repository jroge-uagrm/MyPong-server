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

    public ServerClientThread(Socket socket, String newName) {
        try {
            clientSocket = socket;
            name = newName;
            running = true;
//            new Thread(this).start();
            internalLog("New client connected:" + name);
            onConnected();
        } catch (Exception e) {
            running = false;
            internalLog("ERROR on constructor:" + e.getMessage());
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
                    manageMessage(line);
                }
            }
        } catch (Exception e) {
            if (running) {
                internalLog("ERROR on run:" + e.getMessage());
                stop();
            } else {
                internalLog("Disconnected.");
            }
        }
    }

    public void manageMessage(String messageFromClient) {
        String response = "Ping received";
        if (messageFromClient.equals("Disconnected")) {
            stop();
            onDisconnected();
        } else if (!messageFromClient.equals("ping from client")) {
            if (messageFromClient.equals("asign me name")) {
                response = "assigned name:" + name;
            } else if (messageFromClient.equals("Hola")) {
                response = "Hola! como estas?";
            } else if (messageFromClient.equals("Chau")) {
                response = "No te vayas!! :(";
            }
            internalLog("New message:" + messageFromClient);
        } else {
            internalLog("Ping done");
        }
        sendMessage(response);
    }

    public void sendMessage(String msg) {
        try {
            if (!msg.equals("Ping received")) {
                internalLog("Sending...:" + msg);
            }
            printerWriterOUT.println(msg);
        } catch (Exception e) {
            internalLog("ERROR on sendMessage:" + e.getMessage());
        }
    }

    public void stop() {
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
            } catch (Exception e) {
                internalLog("ERROR on stop:" + e.getMessage());
            }
        } else {
            internalLog("clientSocket is null");
        }
    }

    public boolean getRunningState() {
        return running;
    }

    private void internalLog(String msg) {
        clientThreadLog(name + ".thread:" + msg);
    }

    //Overridables
    public void onConnected() {
    }

    public void onDisconnected() {
    }

    public void clientThreadLog(String msg) {
    }
}
