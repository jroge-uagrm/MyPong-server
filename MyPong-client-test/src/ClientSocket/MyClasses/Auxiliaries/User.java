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
public class User {

    public String username;
    public String password;
    public String key;
    public String matchId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.key = "";
        matchId = "";
    }
}
