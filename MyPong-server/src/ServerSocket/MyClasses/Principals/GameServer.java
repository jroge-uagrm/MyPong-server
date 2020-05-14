/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSocket.MyClasses.Principals;

import ServerSocket.Events.ServerClientThreadEvents;
import ServerSocket.Events.ServerMainThreadEvents;
import ServerSocket.MyClasses.Auxiliaries.ContainerObject;
import ServerSocket.MyClasses.Auxiliaries.Protocol;
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
    private final HashMap<String, Room> roomList;
    private final Gson gson;

    public GameServer() {
        server = new Server(port, this, this);
        gson = new Gson();
        userList = new LinkedList<>();
        roomList = new HashMap<>();
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
        roomList.clear();
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
                        User userLoggingIn = gson.fromJson(protocol.content, User.class);
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
                            userSaved.key = object.origin;
                            sendUserList(userSaved);
                        }
                        server.send(object.origin, new ContainerObject(
                                "server",
                                new Protocol("loginResponse", loginResponse),
                                new String[]{object.origin}
                        ));
                        if (!loginResponse.equals("OK")) {
                            server.removeClient(object.origin);
                        }
                        break;
                    case "register":
                        User registerUser = gson.fromJson(protocol.content, User.class);
                        String registerResponse;
                        if (registerUser.username.equals("")) {
                            registerResponse = "Empty username";
                        } else if (getUserByUsername(registerUser.username) != null) {
                            registerResponse = "Username already exist";
                        } else {
                            registerUser.key = object.origin;
                            registerUser.password = protectPassword(registerUser.password);
                            registerUser.roomId = "";
                            userList.add(registerUser);
                            registerResponse = "OK";
                            sendUserList(registerUser);
                        }
                        server.send(object.origin, new ContainerObject(
                                "server",
                                new Protocol("registerResponse", registerResponse),
                                new String[]{object.origin}
                        ));
                        if (!registerResponse.equals("OK")) {
                            server.removeClient(object.origin);
                        }
                        break;
                    case "createRoom":
                        User requesting = getUserByKey(object.origin);
                        String roomId = requesting.key;
                        requesting.roomId = roomId;
                        Room room = new Room(roomId);
                        room.addPlayer(requesting);
                        roomList.put(roomId, room);
                        for (User user : userList) {
                            String content = gson.toJson(requesting);
                            if (user.key.equals(requesting.key)) {
                                content = gson.toJson(room.players);
                            }
                            server.send(user.key, new ContainerObject(
                                    "server",
                                    new Protocol("joinedRoom", content),
                                    new String[]{user.key}
                            ));
                        }
                        break;
                    case "invite":
                        User userInvited = getUserByKey(protocol.content);
                        if (!userInvited.roomId.equals("")) {
                            server.send(object.origin, new ContainerObject(
                                    "server",
                                    new Protocol("errorRoom", "Player is not available"),
                                    new String[]{object.origin}
                            ));
                        } else {
                            User userOwner = getUserByKey(object.origin);
                            server.send(userInvited.key, new ContainerObject(
                                    "server",
                                    new Protocol("newInvitation", gson.toJson(userOwner)),
                                    new String[]{userInvited.key}
                            ));
                        }
                        break;
                    case "acceptInvitation":
                        User userOwner = getUserByKey(protocol.content);
                        Room roomToJoin = roomList.get(userOwner.roomId);
                        User newUserToJoin = getUserByKey(object.origin);
                        roomToJoin.addPlayer(newUserToJoin);
                        newUserToJoin.roomId = roomToJoin.id;
                        for (User user : userList) {
                            String content = newUserToJoin.key;
                            if (user.key.equals(newUserToJoin.key)) {
                                content = gson.toJson(roomToJoin.players);
                            }
                            server.send(user.key, new ContainerObject(
                                    "server",
                                    new Protocol("joinedRoom", content),
                                    new String[]{user.key}
                            ));
                        }
                        break;
                    case "leaveRoom":
                        User userLeavingRoom = getUserByKey(object.origin);
                        Room userRoom = roomList.get(userLeavingRoom.roomId);
                        for (User user : userList) {
                            server.send(user.key, new ContainerObject(
                                    "server",
                                    new Protocol("leftRoom", userLeavingRoom.key),
                                    new String[]{user.key}
                            ));
                        }
                        userRoom.removePlayer(userLeavingRoom);
                        userLeavingRoom.roomId = "";
                        if (userRoom.players.isEmpty()) {
                            roomList.remove(userRoom.id);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onClientDisconnected(String key) {
        //Al reves
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

    private void userListRemoveKey(String keyToRemove) {
        //server.removeClient(key);
        for (User user : userList) {
            if (user.key.equals(keyToRemove)) {
                user.key = "";
                user.roomId = "";
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
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            byte[] encoded = Base64.getEncoder().encode(digest);
            return new String(encoded);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
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
