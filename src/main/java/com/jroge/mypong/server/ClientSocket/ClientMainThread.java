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
    private boolean tryingConnect;
    private String information;
    private int connectionAttempts;
    private final int MAX_RECONNECTION_ATTEMPS = 5;
    private final int TIME_BETWEEN_RECONNECTIONS = 3;
    private final int TIME_BETWEEN_PINGS = 2;

    public ClientMainThread(String newHost, int newPort) {
        host = newHost;
        port = newPort;
        connected = tryingConnect = false;
    }

    @Override
    public void run() {
        tryToConnectToSocket();
        try {
            printWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (connected) {
                sendMessage();
                printWriterOUT.flush();
                manageResponse();
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

    private void manageResponse() {
        try {
            String serverResponse = bufferedReaderIN.readLine();
            //Linux Disconnected begin
            if (serverResponse == null) {
                internalLog("Connection lost.");
                connected = false;
                onClientConnectionLost();
            } //Linux Disconnected end
            else {
                if (!serverResponse.equals("Ping received")) {
                    internalLog("Message from server:" + serverResponse);
                    onClientNewResponse(serverResponse);
                }
            }
        } catch (Exception e) {
            //Windows Disconnected begin
            internalLog("ERROR on manageMessage:" + e.getMessage());
            internalLog("Connection lost.");
            connected = false;
            onClientConnectionLost();
            //Windows Disconnected end
        }
    }

    public void setInformation(String newInformation) {
        information = newInformation;
    }

    private void sendMessage() {
        if (connectionAttempts == 0) {
            printWriterOUT.println(information);
            if (!information.equals("ping from client")) {
                internalLog("Sending...:" + information);
            }
            information = "ping from client";
        }
    }

    public void disconnect() {
        connected = false;
        if (clientSocket != null) {
            try {
                information = "Disconnected";
                sendMessage();
                clientSocket.close();
                internalLog("Disconnected.");
            } catch (Exception e) {
                internalLog("ERROR on disconnect" + e.getMessage());
            }
        }
        onClientDisconnected();
    }

    private void tryToConnectToSocket() {
        connectionAttempts++;
        try {
            clientSocket = new Socket(host, port);
            information = "asign me name";
            connected = true;
            tryingConnect = false;
            connectionAttempts = 0;
            internalLog("Connected.");
            onClientConnected();
        } catch (Exception ex) {
            internalLog("Unable to connect.");
            connected = false;
            if (connectionAttempts <= MAX_RECONNECTION_ATTEMPS) {
                tryingConnect = true;
                onClientTryingConnect();
                internalLog("Reconnecting... " + connectionAttempts + "/" + MAX_RECONNECTION_ATTEMPS + " attempt(s)");
                try {
                    Thread.sleep(TIME_BETWEEN_RECONNECTIONS * 1000);
                } catch (Exception e) {
                    internalLog("ERROR on connectToSocket:" + e.getMessage());
                }
                tryToConnectToSocket();
            } else {
                tryingConnect = false;
                disconnect();
            }
        }

    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isTryingConnect() {
        return tryingConnect;
    }

    private void internalLog(String msg) {
        mainThreadLog("Client.thread:" + msg);
    }

    //Overridables
    public void onClientConnected() {
    }

    public void onClientNewResponse(String msg) {
    }

    public void onClientDisconnected() {
    }

    public void onClientTryingConnect() {
    }

    public void onClientConnectionLost() {
    }

    public void mainThreadLog(String msg) {
    }
}
