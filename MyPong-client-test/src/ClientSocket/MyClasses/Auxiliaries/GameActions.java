/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.MyClasses.Auxiliaries;

/**
 *
 * @author jorge
 */
public interface GameActions {

    public void refresh();

    public void showMessageDialog(String msg);
    
    public int showConfirmDialog(String msg);
}
