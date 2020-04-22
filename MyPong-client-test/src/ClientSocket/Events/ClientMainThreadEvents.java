/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.Events;

/**
 *
 * @author jorge
 */
public interface ClientMainThreadEvents {

    public void onClientConnectionLost();

    public void onClientNewResponse(String serverResponse);

    public void onClientDisconnected();

    public void onClientConnected();

    public void mainThreadLog(String string);
    
}
