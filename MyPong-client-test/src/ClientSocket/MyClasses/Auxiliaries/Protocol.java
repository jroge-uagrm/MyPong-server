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
public class Protocol extends Object{
    
    public String action;
    public String content;

    public Protocol(String action, String content) {
        this.action = action;
        this.content = content;
    }
}
