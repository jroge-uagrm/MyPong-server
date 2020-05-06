/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket;

import ClientSocket.MyClasses.Client;
import ClientSocket.MyClasses.ContainerObject;
import ClientSocket.Events.ClientMainThreadEvents;
import ClientSocket.MyClasses.User;
import com.google.gson.Gson;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author jroge
 */
public class ClientUI extends javax.swing.JFrame implements ClientMainThreadEvents {

    private final Client client;
    private final String host = "192.168.1.117";
    private final int port = 32000;
    private boolean connected;
    private User[] userList;
    private final Gson gson;
    private String key, name, action;

    /**
     * Creates new form ClientUI
     */
    public ClientUI() {
        initComponents();
        setLogAlwaysOnTheButtom();
        client = new Client(host, port, this);
        gson = new Gson();
        name = "Client";
        action = "";
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

        txfMessage1 = new javax.swing.JTextField();
        btnDisconnect = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        btnSendMessage = new javax.swing.JButton();
        txfMessage = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaLog = new javax.swing.JTextArea();
        lblName = new javax.swing.JLabel();
        txfUsername = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        txfPassword = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstUsers = new javax.swing.JList<>();

        txfMessage1.setText("Hola");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnDisconnect.setBackground(java.awt.Color.red);
        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        lblStatus.setText("Disconnected");

        btnSendMessage.setBackground(java.awt.Color.white);
        btnSendMessage.setText("Send message");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });

        txfMessage.setText("Hola");

        txaLog.setColumns(20);
        txaLog.setFont(new java.awt.Font("Fira Code", 0, 18)); // NOI18N
        txaLog.setRows(5);
        txaLog.setFocusable(false);
        jScrollPane2.setViewportView(txaLog);

        lblName.setFont(new java.awt.Font("Fira Code", 0, 24)); // NOI18N
        lblName.setText("Client");

        txfUsername.setText("username");

        btnLogin.setBackground(java.awt.Color.white);
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnRegister.setBackground(java.awt.Color.white);
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        txfPassword.setText("jPasswordField1");

        jScrollPane1.setViewportView(lstUsers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblStatus)
                                .addGap(141, 141, 141)
                                .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(lblName)
                        .addGap(336, 336, 336)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfMessage)
                    .addComponent(btnSendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(txfUsername)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(txfPassword)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(txfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegister)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        client.disconnect();
    }//GEN-LAST:event_btnDisconnectActionPerformed

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        int[] indices = lstUsers.getSelectedIndices();
        String[] destinations = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            destinations[i] = userList[indices[i]].key;
        }
        client.sendMessage(new ContainerObject(
                key,
                txfMessage.getText(),
                destinations
        ));
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        action = "login";
        client.connect();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        action = "register";
        client.connect();
    }//GEN-LAST:event_btnRegisterActionPerformed

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
            java.util.logging.Logger.getLogger(ClientUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JList<String> lstUsers;
    private javax.swing.JTextArea txaLog;
    private javax.swing.JTextField txfMessage;
    private javax.swing.JTextField txfMessage1;
    private javax.swing.JPasswordField txfPassword;
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onClientConnected() {
        refreshComponents();
    }

    @Override
    public void onClientNewResponse(String serverResponse) {
        ContainerObject object = gson.fromJson(serverResponse, ContainerObject.class);
        if (object.origin.equals("server")) {
            String data = (String) object.body;
            String[] elements = data.split("_");
            String action = elements[0];
            switch (action) {
                case "newKey":
                    key = elements[1];
                    client.sendMessage(new ContainerObject(key, "OK", new String[]{"server"}));
                    sendCredentials();
                    break;
                case "userList":
                    userList = gson.fromJson(elements[1], User[].class);
                    changeClientList();
                    client.sendMessage(new ContainerObject(key, "OK", new String[]{"server"}));
                    break;
                case "serverStopped":
//                    client.disconnect();
                    break;
                case "loginResponse":
                    String loginResponse = elements[1];
                    client.sendMessage(new ContainerObject(key, "OK", new String[]{"server"}));
                    if (loginResponse.equals("OK")) {
                        refreshComponents();
                    }
                    JOptionPane.showMessageDialog(this, loginResponse);
                    break;
                case "registerResponse":
                    String registerResponse = elements[1];
                    client.sendMessage(new ContainerObject(key, "OK", new String[]{"server"}));
                    if (registerResponse.equals("OK")) {
                        refreshComponents();
                    }
                    JOptionPane.showMessageDialog(this, registerResponse);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this, object.body);
        }
    }

    @Override
    public void onClientConnectionLost() {
        if (action.equals("")) {
            client.connect();
        }
        refreshComponents();
    }

    @Override
    public void onClientDisconnected() {
        name = "Client";
        refreshComponents();
    }

    @Override
    public void mainThreadLog(String string) {
        log(string);
    }

    private void sendCredentials() {
        if (action.equals("login")) {
            name = txfUsername.getText();
            client.sendMessage(new ContainerObject(
                    key,
                    "login_" + gson.toJson(new User(name, txfPassword.getText())),
                    new String[]{"server"}
            ));
        } else {
            name = txfUsername.getText();
            String password = txfPassword.getText();
            client.sendMessage(new ContainerObject(
                    key,
                    "register_" + gson.toJson(new User(name, password)),
                    new String[]{"server"}
            ));
        }
    }

    private void setLogAlwaysOnTheButtom() {
        DefaultCaret caret = (DefaultCaret) txaLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void refreshComponents() {
        lblName.setText(name);
        connected = client.isConnected();
//        btnConnect.setEnabled(!connected);
        btnDisconnect.setEnabled(connected);
        lblStatus.setText(connected ? "CONNECTED" : "DISCONNECTED");
        boolean logged = !lblName.getText().equals("Client");
//        lstUsers.setEnabled(logged);
        txfMessage.setEnabled(logged);
        btnSendMessage.setEnabled(logged);

        txfUsername.setEnabled(!logged);
        txfPassword.setEnabled(!logged);
        btnLogin.setEnabled(!logged);
        btnRegister.setEnabled(!logged);
    }

    private void changeClientList() {
        DefaultListModel modeloLista = new DefaultListModel();
        lstUsers.setModel(modeloLista);
        for (User user : userList) {
            modeloLista.addElement(user.username);
        }
    }

    private void log(String msg) {
        txaLog.append(msg + '\n');
    }
}
