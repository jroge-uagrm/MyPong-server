/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Principals;

import ServerSocket.Events.ServerClientThreadEvents;
import ServerSocket.Events.ServerMainThreadEvents;
import ServerSocket.MyClasses.Auxiliaries.BallConverted;
import ServerSocket.MyClasses.Auxiliaries.ContainerObject;
import ServerSocket.MyClasses.Auxiliaries.Protocol;
import ServerSocket.MyClasses.Auxiliaries.Scoreboard;
import com.google.gson.Gson;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author jorge
 */
public class GameServer implements ServerMainThreadEvents, ServerClientThreadEvents {

    private final Server server;
    private final int port = 32000;
    private final LinkedList<User> userList;
    private final HashMap<String, Match> matchList;
    private final Gson gson;

    public GameServer() {
        server = new Server(port, this, this);
        gson = new Gson();
        userList = new LinkedList<>();
        matchList = new HashMap<>();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }

    public boolean isRunning() {
        return server.isRunning();
    }

    public int getConnectedClientSocketAmount() {
        return server.getConnectedClientSocketAmount();
    }

    @Override
    public void onServerStarted() {
        refresh();
    }

    @Override
    public void onServerStopped() {
        refresh();
        matchList.clear();
    }

    @Override
    public void onServerLog(String msg) {
        serverLog(msg);
    }

    @Override
    public void onClientConnected(String connectedKey) {
        server.send(connectedKey, new ContainerObject(
                "server",
                new Protocol("newKey", connectedKey),
                new String[]{connectedKey}
        ));
        refresh();
    }

    @Override
    public void onClientNewMessage(ContainerObject object) {
        for (String destination : object.destinations) {
            if (!destination.equals("server")) {
                server.send(destination, object);
            } else {
                clientsLog("Received:" + gson.toJson(object));
                String x = gson.toJson(object.body);
                Protocol protocol = gson.fromJson(x, Protocol.class);
                switch (protocol.action) {
                    case "login":
                        login(object.origin, gson.fromJson(protocol.content, User.class));
                        break;
                    case "register":
                        register(object.origin, gson.fromJson(protocol.content, User.class));
                        break;
                    case "createRoom":
                        createMatch(getUserByKey(object.origin));
                        break;
                    case "invite":
                        invite(getUserByKey(object.origin), getUserByKey(protocol.content));
                        break;
                    case "acceptInvitation":
                        acceptInvitation(getUserByKey(object.origin), getUserByKey(protocol.content));
                        break;
                    case "rejectInvitation":
                        rejectInvitation(getUserByKey(object.origin), getUserByKey(protocol.content));
                        break;
                    case "leaveRoom":
                        leaveMatch(getUserByKey(object.origin));
                        break;
                    case "ballMoving":
                        sendBall(getUserByKey(object.origin), gson.fromJson(protocol.content, BallConverted.class));
                        break;
                    case "ballFailed":
                        updateScoreboard(getUserByKey(object.origin));
                        break;
                }
            }
        }
    }

    @Override
    public void onClientDisconnected(String key) {
        server.removeClient(key);
        userListRemoveKey(key);
        if (server.isRunning()) {
            server.sendToEveryone(new Protocol("disconnectedUser", key));
        }
        refresh();
    }

    @Override
    public void onClientLog(String msg) {
        clientsLog(msg);
    }

    private void login(String originKey, User userLoggingIn) {
        User userSaved = getUserByUsername(userLoggingIn.username);
        String loginResponse = "";
        if (userSaved == null) {
            loginResponse = "Username does not exist";
        } else if (!userSaved.password.equals(protectPassword(userLoggingIn.password))) {
            loginResponse = "Incorrect password";
        } else if (!userSaved.key.equals("")) {
            loginResponse = "Session already open";
        } else {
            loginResponse = "OK";
            userSaved.key = originKey;
            sendUserList(userSaved);
        }
        server.send(originKey, new ContainerObject(
                "server",
                new Protocol("loginResponse", loginResponse),
                new String[]{originKey}
        ));
        if (!loginResponse.equals("OK")) {
            server.removeClient(originKey);
        }
    }

    private void register(String originKey, User registerUser) {
        String registerResponse;
        if (registerUser.username.equals("")) {
            registerResponse = "Empty username";
        } else if (getUserByUsername(registerUser.username) != null) {
            registerResponse = "Username already exist";
        } else {
            registerUser.key = originKey;
            registerUser.password = protectPassword(registerUser.password);
            registerUser.matchId = "";
            userList.add(registerUser);
            registerResponse = "OK";
            sendUserList(registerUser);
        }
        server.send(originKey, new ContainerObject(
                "server",
                new Protocol("registerResponse", registerResponse),
                new String[]{originKey}
        ));
        if (!registerResponse.equals("OK")) {
            server.removeClient(originKey);
        }
    }

    private void createMatch(User userOwner) {
        String matchId = protectPassword(userOwner.key);
        userOwner.matchId = matchId;
        Match match = new Match(matchId);
        match.addPlayerA(userOwner);
        matchList.put(matchId, match);
        for (User user : userList) {
            String content = gson.toJson(userOwner);
            if (user.key.equals(userOwner.key)) {
                content = gson.toJson(match.getPlayers());
            }
            server.send(user.key, new ContainerObject(
                    "server",
                    new Protocol("joinedRoom", content),
                    new String[]{user.key}
            ));
        }
    }

    private void invite(User userOwner, User userInvited) {
        if (!userInvited.matchId.equals("")) {
            server.send(userOwner.key, new ContainerObject(
                    "server",
                    new Protocol("errorRoom", "Player is not available"),
                    new String[]{userOwner.key}
            ));
        } else {
            server.send(userInvited.key, new ContainerObject(
                    "server",
                    new Protocol("newInvitation", gson.toJson(userOwner)),
                    new String[]{userInvited.key}
            ));
        }
    }

    private void acceptInvitation(User userAccepting, User userOwner) {
        Match matchToJoin = matchList.get(userOwner.matchId);
        if (matchToJoin == null) {
            server.send(userAccepting.key, new ContainerObject(
                    "server",
                    new Protocol("emptyRoom", "Room not exist"),
                    new String[]{userAccepting.key}
            ));
        } else {
            matchToJoin.addPlayerB(userAccepting);
            userAccepting.matchId = matchToJoin.id;
            for (User user : userList) {
                String content = gson.toJson(userAccepting);
                if (user.key.equals(userAccepting.key)) {
                    content = gson.toJson(matchToJoin.getPlayers());
                }
                server.send(user.key, new ContainerObject(
                        "server",
                        new Protocol("joinedRoom", content),
                        new String[]{user.key}
                ));
            }
            //Start match
            matchToJoin.startMatch();
        }
    }

    private void rejectInvitation(User userRejecting, User userOwner) {
        getUserByKey(userRejecting.key).matchId = "";
        server.send(userOwner.key, new ContainerObject(
                "server",
                new Protocol("rejectedInvitation", userRejecting.key),
                new String[]{userOwner.key}
        ));
    }

    private void leaveMatch(User userLeaving) {
        Match match = matchList.get(userLeaving.matchId);
        match.removePlayer(userLeaving);
        getUserByKey(userLeaving.key).matchId = "";
        for (User user : userList) {
            server.send(user.key, new ContainerObject(
                    "server",
                    new Protocol("leftRoom", gson.toJson(userLeaving)),
                    new String[]{user.key}
            ));
        }
        if (match.getPlayers().isEmpty()) {
            matchList.remove(match.id);
        } else if (match.getPlayers().size() == 1) {
            finalizeMatch(match);
        }
    }

    private void sendBall(User userSending, BallConverted ball) {
        Match userMatch = matchList.get(userSending.matchId);
        User userReceiving = userMatch.getOtherPlayer(userSending);
        if (userReceiving != null) {
            server.send(userReceiving.key, new ContainerObject(
                    "server",
                    new Protocol("ballMoving", gson.toJson(ball)),
                    new String[]{userReceiving.key}
            ));
        }
    }

    private void updateScoreboard(User userLostPoint) {
        Match match = matchList.get(userLostPoint.matchId);
        if (match != null && match.hasTwoPlayers()) {
            match.addPoint(userLostPoint);
            User userWonPoint = match.getOtherPlayer(userLostPoint);
            Scoreboard scoreboard = new Scoreboard(
                    match.setsWonPlayerA,
                    match.setsWonPlayerB,
                    match.pointsWonPlayerA,
                    match.pointsWonPlayerB
            );
            if (!match.playing) {
                User winner = match.winner;
                server.send(userLostPoint.key, new ContainerObject(
                        "server",
                        new Protocol("winner", gson.toJson(winner)),
                        new String[]{userLostPoint.key}
                ));
                server.send(userWonPoint.key, new ContainerObject(
                        "server",
                        new Protocol("winner", gson.toJson(winner)),
                        new String[]{userWonPoint.key}
                ));
            }
            server.send(userLostPoint.key, new ContainerObject(
                    "server",
                    new Protocol("scoreboard", gson.toJson(scoreboard)),
                    new String[]{userLostPoint.key}
            ));
            server.send(userWonPoint.key, new ContainerObject(
                    "server",
                    new Protocol("scoreboard", gson.toJson(scoreboard)),
                    new String[]{userWonPoint.key}
            ));
        }
    }

    private void finalizeMatch(Match match) {
        if (match.getPlayers().size() == 1) {
            User lastPlayer = match.getLastPlayer();
            server.send(lastPlayer.key, new ContainerObject(
                    "server",
                    new Protocol("winner", gson.toJson(lastPlayer)),
                    new String[]{lastPlayer.key}
            ));
        }
    }

    //Auxiliaries
    private void userListRemoveKey(String keyToRemove) {
        //server.removeClient(key);
        for (User user : userList) {
            if (user.key.equals(keyToRemove)) {
                user.key = "";
                user.matchId = "";
                break;
            }
        }
    }

    private void sendUserList(User userLoggedIn) {
        LinkedList<User> newUserList = new LinkedList<>();
        for (User user : userList) {
            if (user.key != null && !user.key.equals("")) {
                newUserList.add(user);
            }
        }
        String userListJson = gson.toJson(newUserList);
        server.send(userLoggedIn.key, new ContainerObject(
                "server",
                new Protocol("userList", userListJson),
                new String[]{userLoggedIn.key}
        ));
        server.sendToEveryone(new Protocol("connectedUser", gson.toJson(userLoggedIn)));
    }

    private String protectPassword(String plainText) {
        /*plainText
                += Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                + Calendar.getInstance().get(Calendar.MINUTE)
                + Calendar.getInstance().get(Calendar.SECOND)
                + Calendar.getInstance().get(Calendar.MILLISECOND);*/
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            byte[] encoded = Base64.getEncoder().encode(digest);
            return new String(encoded);
        } catch (NoSuchAlgorithmException ex) {
            //System.out.println(ex.getMessage());
            return "empty";
        }
    }

    private User getUserByUsername(String usernameToSearch) {
        for (User user : userList) {
            if (user.username.equals(usernameToSearch)) {
                return user;
            }
        }
        return null;
    }

    private User getUserByKey(String key) {
        for (User user : userList) {
            if (user.key.equals(key)) {
                return user;
            }
        }
        return null;
    }

    //Overridables
    public void refresh() {
    }

    public void clientsLog(String msg) {
        System.out.println(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE)
                + "-Client:" + msg
        );
    }

    public void serverLog(String msg) {
        System.out.println(
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "" + Calendar.getInstance().get(Calendar.MINUTE)
                + "-" + msg
        );
    }
}
