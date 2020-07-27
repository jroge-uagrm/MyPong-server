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
public class Match {

    public String id;
    public User playerA;
    public User playerB;
    public boolean playing;
    public int setsWonPlayerA, setsWonPlayerB, pointsWonPlayerA, pointsWonPlayerB;
    public User winner;

    public Match(String newId) {
        id = newId;
        playing = false;
    }

    public void addPlayerA(User newPlayer) {
        playerA = newPlayer;
    }

    public void addPlayerB(User newPlayer) {
        playerB = newPlayer;
    }

    public LinkedList<User> getPlayers() {
        LinkedList<User> players = new LinkedList<>();
        if (playerA != null) {
            players.add(playerA);
        }
        if (playerB != null) {
            players.add(playerB);
        }
        return players;
    }

    public void removePlayer(User playerToRemove) {
        try {
            if (playerA != null & playerA.key.equals(playerToRemove.key)) {
                playerA = null;
            } else if (playerB != null && playerB.key.equals(playerToRemove.key)) {
                playerB = null;
            }
        } catch (Exception e) {
            playerA = null;
            playerB = null;
        }
    }

    public User getLastPlayer() {
        return playerA != null ? playerA : playerB;
    }

    public boolean hasTwoPlayers() {
        return playerA != null && playerB != null;
    }

    public void startMatch() {
        playing = true;
        setsWonPlayerA = 0;
        setsWonPlayerB = 0;
        pointsWonPlayerA = 0;
        pointsWonPlayerB = 0;
    }

    public void addPoint(User userLostPoint) {
        if (playerA.key.equals(userLostPoint.key)) {
            pointsWonPlayerB++;
            if (pointsWonPlayerB == 11) {
                setsWonPlayerB++;
                pointsWonPlayerA = 0;
                pointsWonPlayerB = 0;
            }
            if (setsWonPlayerB == 3) {
                playing = false;
                winner = playerB;
            }
        } else {
            pointsWonPlayerA++;
            if (pointsWonPlayerA == 11) {
                setsWonPlayerA++;
                pointsWonPlayerA = 0;
                pointsWonPlayerB = 0;
            }
            if (setsWonPlayerA == 3) {
                playing = false;
                winner = playerA;
            }
        }

    }

    public User getOtherPlayer(User userX) {
        return playerA.key.equals(userX.key) ? playerB : playerA;
    }
}
