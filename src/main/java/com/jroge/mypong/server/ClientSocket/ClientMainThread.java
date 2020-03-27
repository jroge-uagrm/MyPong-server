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

    private String host;
    private int port;
    private Socket clientSocket;
    private PrintWriter printWriterOUT;
    private BufferedReader bufferedReaderIN;
    private boolean connected;
    private String information;
    private int connectionAttempts;
    private boolean tryingConnect;
    private final int MAX_RECONNECTION_ATTEMPS = 5;
    private final int TIME_BETWEEN_RECONNECTIONS = 3;
    private final int TIME_BETWEEN_PINGS = 2;

    public ClientMainThread(String newHost, int newPort) {
        host = newHost;
        port = newPort;
        connected = false;
    }

    @Override
    public void run() {
        tryingConnect = true;
        tryToConnectToSocket();
        try {
            printWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (connected) {
                sendMessage();
                printWriterOUT.flush();
                manageMessage();
                Thread.sleep(TIME_BETWEEN_PINGS * 1000);
            }
        } catch (Exception e) {
            if (connected) {
                internalLog("ERROR on ClientMainThread run:"
                        + e.getMessage());
                disconnect();
            }
        }
    }

    private void manageMessage() {
        try {
            String serverResponse = bufferedReaderIN.readLine();
            //Linux
            if (serverResponse == null) {
                internalLog("Connection lost.");
                connected = false;
                onClientConnectionLost();
            } else {
                internalLog("Message from server:" + serverResponse);
                onClientNewResponse(serverResponse);
            }
        } catch (Exception e) {
            //Windows
            internalLog("ERROR on manageMessage:" + e.getMessage());
            internalLog("Connection lost.");
            connected = false;//OK!!
            onClientConnectionLost();
        }
    }

    public void setInformation(String newInformation) {
        information = newInformation;
    }

    private void sendMessage() {
        if (connectionAttempts == 0) {
            printWriterOUT.println(information);
            internalLog("Sending...:" + information);
        }
    }

    public void disconnect() {
        connected = false;
        tryingConnect = false;
        connectionAttempts = 0;
        if (clientSocket != null) {
            try {
                information = "Disconnected";
                sendMessage();
                clientSocket.close();
            } catch (Exception e) {
                internalLog("ERROR on disconnect" + e.getMessage());
            }
        }
        internalLog("Disconnected.");
        onClientDisconnected();
    }

    private void tryToConnectToSocket() {
        if (tryingConnect) {
            connectionAttempts++;
            try {
                clientSocket = new Socket(host, port);
                connected = true;
                information = "asign me name";
                connectionAttempts = 0;
                internalLog("Connected.");
                onClientConnected();
            } catch (Exception ex) {
                connected = false;
                internalLog("Unable to connect.");
                if (connectionAttempts <= MAX_RECONNECTION_ATTEMPS) {
                    onClientTryingReconnect();
                    internalLog("Reconnecting... " + connectionAttempts + "/" + MAX_RECONNECTION_ATTEMPS + " attempt(s)");
                    try {
                        Thread.sleep(TIME_BETWEEN_RECONNECTIONS * 1000);
                    } catch (Exception e) {
                        internalLog("ERROR on connectToSocket:" + e.getMessage());
                    }
                    tryToConnectToSocket();
                } else {
                    disconnect();
                }
            }
        }

    }

    public boolean getConnectedStatus() {
        return connected || tryingConnect;
    }

    private void internalLog(String msg) {
        mainThreadLog("Client.thread:" + msg);
    }

    //Overridables
    public void onClientConnected() {
    }

    public void onClientNewResponse(String response) {
    }

    public void onClientTryingReconnect() {
    }

    public void onClientDisconnected() {
    }

    public void onClientConnectionLost() {
    }

    public void mainThreadLog(String msg) {
    }
}
