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
    private boolean running;

    public ServerClientThread(Socket socket) {
        try {
            clientSocket = socket;
            running = true;
            clientThreadLog("MainThread:Connected.");
        } catch (Exception e) {
            running = false;
            clientThreadLog("MainThread:ERROR on constructor:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        PrintWriter printerWriterOUT = null;
        BufferedReader bufferedReaderIN = null;
        try {
            while (running) {
                printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
                bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                while ((line = bufferedReaderIN.readLine()) != null) {
                    clientThreadLog("ClientThread:New message:" + line);
                    String msg = "Ping received";
                    if (line.equals("Hola")) {
                        msg = "Hola! como estas?";
                    } else if (line.equals("Chau")) {
                        msg = "No te vayas!! :(";
                    }
                    printerWriterOUT.println("ClientThread:" + msg);
                }
            }
        } catch (Exception e) {
            clientThreadLog(e.getMessage());
        } finally {
            try {
                if (printerWriterOUT != null) {
                    printerWriterOUT.close();
                }
                if (bufferedReaderIN != null) {
                    bufferedReaderIN.close();
                }
                stop();
            } catch (Exception e) {
                if (running) {
                    clientThreadLog("ClientThread:ERROR on run:" + e.getMessage());
                } else {
                    clientThreadLog("ClientThread:Stopped");
                }
            }
        }
    }

    public void stop() {
        running = false;
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (Exception e) {
                clientThreadLog(e.getMessage());
            }
        } else {
            clientThreadLog("ClientThread:clientSocket is null");
        }
    }

    //Overridables
    public void clientThreadLog(String msg) {
    }
}
