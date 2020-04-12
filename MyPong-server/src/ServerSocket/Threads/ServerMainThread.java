/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.Threads;

import ServerSocket.Events.ServerMainThreadEvents;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author jroge
 */
public class ServerMainThread implements Runnable {

    private ServerSocket serverSocket;
    private final int port;
    private boolean running;
    private final ServerMainThreadEvents events;

    public ServerMainThread(int newPort, ServerMainThreadEvents newEvents) {
        port = newPort;
        events = newEvents;
    }

    @Override
    public void run() {
        try {
            tryToConnectSocket();
            while (running) {
                internalLog("Waiting a connection...");
                Socket client = serverSocket.accept();
                internalLog("New sockted connected.");
                onNewClientConnected(client);
            }
        } catch (Exception e) {
            if (running) {
                internalLog("ERROR(2):" + e.getMessage());
                closeAll();
            }
        }
    }

    private void tryToConnectSocket() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            running = true;
            internalLog("Running...");
            events.onServerStarted();
        } catch (Exception e) {
            running = false;
            internalLog("ERROR(1):"
                    + e.getMessage());
        }
    }

    private void closeAll() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                internalLog("ERROR(3):" + e.getMessage());
            } finally {
                internalLog("Stopped.");
                events.onServerStopped();
            }
        } else {
            internalLog("Never connected.");
        }
    }

    public void stop() {
        running = false;
        closeAll();
    }

    public boolean isRunning() {
        return running;
    }

    private void internalLog(String msg) {
        events.onServerLog("server.t:" + msg);
    }

    //Overridables
    public void onNewClientConnected(Socket clientSocket) {
    }
}
