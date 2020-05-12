/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.Threads;

import ServerSocket.Events.ServerClientThreadEvents;
import com.google.gson.Gson;
import ServerSocket.MyClasses.Auxiliaries.ContainerObject;
import java.io.BufferedReader;
import java.io.IOException;
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
    private boolean connected;
    private final String key;
    private final ServerClientThreadEvents events;

    public ServerClientThread(Socket socket, String newKey, ServerClientThreadEvents newEvents) {
        clientSocket = socket;
        key = newKey;
        events = newEvents;
    }

    @Override
    public void run() {
        try {
            connected = true;
            printerWriterOUT = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedReaderIN = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Gson gson = new Gson();
            String content = "";
            events.onClientConnected(key);
            while (connected) {
                content = bufferedReaderIN.readLine();
                ContainerObject containerObject = gson.fromJson(content, ContainerObject.class);
                if (containerObject == null) {
                    throw new IOException();
                } else {
                    events.onClientNewMessage(containerObject);
                }
            }
        } catch (IOException e) {
            if (connected) {
                closeAll();
            }
        } finally {
            events.onClientDisconnected(key);
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
                internalLog("Disconnected:" + key);
            }
        } else {
            internalLog("Never connected.");
        }
    }

    public void sendMessage(String msg) {
        try {
            internalLog("Sent:" + msg);
            printerWriterOUT.println(msg);
        } catch (Exception e) {
            internalLog("ERROR(4):" + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public String getKey() {
        return key;
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public void internalLog(String msg) {
        events.onClientLog("cl.th:" + msg);
    }
}
