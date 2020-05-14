/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Principals;

import java.util.LinkedList;

/**
 *
 * @author jorge
 */
public class Room {

    public String id;
    public LinkedList<User> players;
    public LinkedList<Match> matches;

    public Room(String newId) {
        id = newId;
        players = new LinkedList<>();
    }

    public void addPlayer(User newPlayer) {
        players.add(newPlayer);
    }

    public void removePlayer(User playerToRemove) {
        players.remove(playerToRemove);
    }
}
