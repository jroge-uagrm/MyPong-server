/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.Events;

/**
 *
 * @author jorge
 */
public interface ServerMainThreadEvents {

    public void onServerStarted();

    public void onServerStopped();

    public void onServerLog(String msg);
}
