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

    private Socket socket;
    private boolean connected;

    public ClientMainThread(String host, int port) {
        try {
            socket = new Socket(host, port);
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
            PrintWriter printWriterOUT = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReaderIN = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
            while (connected) {
                printWriterOUT.println("ping from client");
                printWriterOUT.flush();
                mainThreadLog("Client:Server response:" + bufferedReaderIN.readLine());
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            if (connected) {
                mainThreadLog("MainThread:ERROR on ClientMainThread run:"
                        + e.getMessage());
                disconnect();
            } else {
                mainThreadLog("MainThread:Disconnected.");
            }
        }
    }

    public void disconnect() {
        connected = false;
        mainThreadLog("MainThread:Trying disconnect");
    }

    public boolean getConnectedStatus() {
        return connected;
    }

    //Overridables
    public void mainThreadLog(String msg) {
    }
}
