/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ClientSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author jroge
 */
public class ClientMainThread implements Runnable {

    private final String host;
    private final int port;
    private Socket clientSocket;
    private PrintWriter printerWriterOUT;
    private BufferedReader bufferedReaderIN;
    private boolean connected;
    private int connectionAttempts;
    private boolean tryingConnect;
    private final int MAX_RECONNECTION_ATTEMPS = 5;
    private final int TIME_BETWEEN_RECONNECTIONS = 3;
    private final int TIME_BETWEEN_PINGS = 2;
    private final ClientMainThreadEvents events;

    public ClientMainThread(String newHost, int newPort, ClientMainThreadEvents newEvents) {
        host = newHost;
        port = newPort;
        connected = false;
        tryingConnect = false;
        events = newEvents;
    }

    @Override
    public void run() {
        tryingConnect = true;
        tryToConnectToSocket();
        try {
            while (connected) {
                printerWriterOUT.flush();
                manageMessage();
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
                events.onClientConnectionLost();
            } else {
                internalLog("Server response:" + serverResponse);
                events.onClientNewResponse(serverResponse);
            }
        } catch (Exception e) {
            //Windows
            if (connected) {
                internalLog("ERROR on manageMessage:" + e.getMessage());
                internalLog("Connection lost.");
                connected = false;
                events.onClientConnectionLost();
            }
        }
    }

    public void sendMessage(String message) {
        if (connectionAttempts == 0) {
            printerWriterOUT.println(message);
            internalLog("Sending...:" + message);
        }
    }

    public void disconnect() {
        connected = false;
        tryingConnect = false;
        connectionAttempts = 0;
        closeAll();
    }

    private void closeAll() {
        if (clientSocket != null) {
            try {
                sendMessage("Disconnected");
                if (printerWriterOUT != null) {
                    printerWriterOUT.close();
                }
                if (bufferedReaderIN != null) {
                    bufferedReaderIN.close();
                }
                clientSocket.close();
                events.onClientDisconnected();
            } catch (Exception e) {
                internalLog("ERROR on disconnect" + e.getMessage());
            }
        }
        internalLog("Disconnected.");
        events.onClientDisconnected();
    }

    private void startAll() {
        connected = true;
        connectionAttempts = 0;
        try {
            printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            internalLog("ERROR(999):" + ex.getMessage());
        }
    }

    private void tryToConnectToSocket() {
        if (tryingConnect) {
            connectionAttempts++;
            try {
                clientSocket = new Socket(host, port);
                startAll();
                internalLog("Connected.");
                events.onClientConnected();
            } catch (Exception ex) {
                connected = false;
                internalLog("Unable to connect.");
                if (connectionAttempts <= MAX_RECONNECTION_ATTEMPS) {
                    events.onClientTryingReconnect();
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
        events.mainThreadLog("Client.thread:" + msg);
    }
}
