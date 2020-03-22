/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ClientSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jroge
 */
public class ClientMainThread implements Runnable {

    private Socket clientSocket;
    private PrintWriter printWriterOUT;
    private BufferedReader bufferedReaderIN;
    private boolean connected;
    private String information;

    public ClientMainThread(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            information = "";
            connected = true;
            mainThreadLog("MainThread:Connected.");
        } catch (Exception e) {
            connected = false;
            mainThreadLog("MainThread:ERROR on constructor:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            printWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (connected) {
                sendMessage();
                printWriterOUT.flush();
                String serverResponse = bufferedReaderIN.readLine();
                mainThreadLog("MainThread:Message from server:" + serverResponse);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            if (connected) {
                mainThreadLog("MainThread:ERROR on ClientMainThread run:"
                        + e.getMessage());
                disconnect();
            } else {
                mainThreadLog("MainThread:Disconnected.ERROR");
            }
        }
    }

    public void setInformation(String newInformation) {
        information = newInformation;
    }

    private void sendMessage() {
        if (information.equals("")) {
            printWriterOUT.println("ping from client");
        } else {
            printWriterOUT.println(information);
            information = "";
        }
    }

    public void disconnect() {
        connected = false;
        if (clientSocket != null) {
            try {
                information = "Disconnected";
                sendMessage();
                clientSocket.close();
                mainThreadLog("MainThread:Disconnected.");
            } catch (Exception e) {
                mainThreadLog("MainThread:ERROR on disconnect" + e.getMessage());
            }
        } else {
            mainThreadLog("MainThread:clientSocket is null");
        }
    }

    public boolean getConnectedStatus() {
        return connected;
    }

    //Overridables
    public void mainThreadLog(String msg) {
    }
}
