/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jroge.mypong.server.ServerSocket;

import java.net.Socket;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author jroge
 */
public class ServerUI extends javax.swing.JFrame implements ServerMainThreadEvents, ServerClientThreadEvents {

    private final Server server;
    private final int port = 32000;
    private boolean serverRunnig;

    /**
     * Creates new form ServerUI
     */
    public ServerUI() {
        initComponents();
        setLogsAlwaysOnTheButtom();
        serverRunnig = false;
        server = new Server(port, this, this);
        refreshComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnStop = new javax.swing.JButton();
        btnStart = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        lblConnectedAmount = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaServerLog = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txaClientsLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnStop.setBackground(java.awt.Color.red);
        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnStart.setBackground(java.awt.Color.green);
        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        lblStatus.setText("STOPPED");

        lblConnectedAmount.setFont(new java.awt.Font("Cantarell", 0, 18)); // NOI18N
        lblConnectedAmount.setText("Connected amount:");

        txaServerLog.setColumns(20);
        txaServerLog.setFont(new java.awt.Font("Fira Code", 0, 18)); // NOI18N
        txaServerLog.setRows(5);
        jScrollPane2.setViewportView(txaServerLog);

        txaClientsLog.setColumns(20);
        txaClientsLog.setFont(new java.awt.Font("Fira Code", 0, 18)); // NOI18N
        txaClientsLog.setRows(5);
        jScrollPane3.setViewportView(txaClientsLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(lblStatus)
                        .addGap(76, 76, 76)
                        .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblConnectedAmount)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblConnectedAmount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        server.start();
        refreshComponents();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        //LinkedList<MyPongClient> aux = connectedClientSockets;
        server.stop();
    }//GEN-LAST:event_btnStopActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblConnectedAmount;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextArea txaClientsLog;
    private javax.swing.JTextArea txaServerLog;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onServerStarted() {
        refreshComponents();
    }

    @Override
    public void onServerStopped() {
        refreshComponents();
    }

    @Override
    public void onServerNewClientConnected(Socket newClientSocket) {
        server.createServerClientThread(newClientSocket);
        refreshComponents();
    }

    @Override
    public void onServerLog(String msg) {
        serverLog(msg);
    }

    @Override
    public void onClientConnected(ServerClientThread serverClientThread) {
        //Client connected but this is detected in server.createServerClientThread(Socket s)
    }

    @Override
    public void onClientNewMessage(ServerClientThread serverClientThread, String messageFromClient) {
        String response = "Ping";
        if (messageFromClient.equals("Disconnected")) {
            server.disconnect(serverClientThread);
        } else if (!messageFromClient.equals("pinging")) {
            serverClientThread.internalLog("New message:" + messageFromClient);
            if (messageFromClient.equals("asign me name")) {
                server.asignName(serverClientThread);
                response = "assigned name:" + serverClientThread.getName();
            } else if (messageFromClient.equals("Hola")) {
                response = "Hola! como estas?";
            } else if (messageFromClient.equals("Chau")) {
                response = "No te vayas!! :(";
            }
        } else {
            serverClientThread.internalLog("Ping");
        }
        server.send(serverClientThread, response);
    }

    @Override
    public void onClientDisconnected(ServerClientThread serverClientThread) {
        server.removeClient(serverClientThread);
        refreshComponents();
    }

    @Override
    public void onClientLog(String msg) {
        clientsLog(msg);
    }

    private void setLogsAlwaysOnTheButtom() {
        DefaultCaret caret = (DefaultCaret) txaServerLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        caret = (DefaultCaret) txaClientsLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void refreshComponents() {
        serverRunnig = server.isRunning();
        btnStart.setEnabled(!serverRunnig);
        btnStop.setEnabled(serverRunnig);
        lblStatus.setText(serverRunnig ? "RUNNING" : "STOPPED");
        lblConnectedAmount.setText("Connected amount:" + server.getConnectedClientSocketAmount());
    }

    private void serverLog(String msg) {
        txaServerLog.append(msg + '\n');
    }

    private void clientsLog(String msg) {
        txaClientsLog.append(msg + '\n');
    }
}
