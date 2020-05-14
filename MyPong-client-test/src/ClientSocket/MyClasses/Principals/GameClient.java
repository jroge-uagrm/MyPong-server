/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSocket.MyClasses.Principals;

import ClientSocket.Events.ClientMainThreadEvents;
import ClientSocket.MyClasses.Auxiliaries.*;
import com.google.gson.Gson;
import java.util.LinkedList;

/**
 *
 * @author jorge
 */
public class GameClient implements ClientMainThreadEvents {

    private final Client client;
    private final String host = "192.168.1.117";
    private final int port = 32000;
    private LinkedList<User> playerList;
    private LinkedList<User> partnerList;
    private final Gson gson;
    private String myKey, username, password, action;
    private String myRoomId;
    private final GameActions gameActions;

    public GameClient(GameActions gameActions) {
        this.gameActions = gameActions;
        client = new Client(host, port, this);
        gson = new Gson();
        username = "Client";
        action = "";
        myRoomId = "";
        playerList = new LinkedList<>();
        partnerList = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public boolean isInRoom() {
        return !myRoomId.equals("");
    }

    public LinkedList<User> getUserList() {
        return playerList;
    }

    public LinkedList<User> getPartnerList() {
        return partnerList;
    }

    public String getKey() {
        return myKey;
    }

    public String getRoomId() {
        return myRoomId;
    }

    public boolean sending() {
        return !action.equals("");
    }

    public void connectByLogin(String username, String password) {
        this.username = username;
        this.password = password;
        action = "login";
        gameActions.refresh();
        client.connect();
    }

    public void connectByRegister(String username, String password) {
        this.username = username;
        this.password = password;
        action = "register";
        gameActions.refresh();
        client.connect();
    }

    public void disconnect() {
        client.disconnect();
    }

    public void createRoom() {
        myRoomId = "waiting...";
        send(new Protocol("createRoom", ""));
    }

    public void leaveRoom() {
        send(new Protocol("leaveRoom", ""));
    }

    public void sendInvitation(int playerIndex) {
        send(new Protocol("invite", playerList.get(playerIndex).key));
    }

    @Override
    public void onClientConnected() {
        gameActions.refresh();
    }

    @Override
    public void onClientNewResponse(String serverResponse) {
        ContainerObject object = gson.fromJson(serverResponse, ContainerObject.class);
        if (object.origin.equals("server")) {
            System.out.println("Client:Received:" + serverResponse);
            String x = gson.toJson(object.body);
            Protocol protocol = gson.fromJson(x, Protocol.class);
            switch (protocol.action) {
                case "newKey":
                    myKey = protocol.content;
                    sendCredentials();
                    break;
                case "userList":
                    setUserList(protocol.content);
                    break;
                case "serverStopped":
                    gameActions.showMessageDialog("Server stopped");
                    break;
                case "loginResponse":
                    manageLoginResponse(protocol.content);
                    break;
                case "registerResponse":
                    manageRegisterResponse(protocol.content);
                    break;
                case "connectedUser":
                    addUser(protocol.content);
                    break;
                case "disconnectedUser":
                    removeUser(protocol.content);
                    break;
                case "newInvitation":
                    catchInvitation(protocol.content);
                    break;
                case "rejectedInvitation":
                    gameActions.showMessageDialog(getUserByKey(protocol.content).username + " has rejected the invitation");
                    break;
                case "joinedRoom":
                    verifyPartnerList(protocol.content);
                    break;
                case "leftRoom":
                    changePlayerInRoom(protocol.content);
                    break;
                case "errorRoom":
                    gameActions.showMessageDialog(protocol.content);
                    break;
                case "emptyRoom":
                    myRoomId = "";
                    gameActions.showMessageDialog(protocol.content);
                    break;
                case "deleteRoom":
                    myRoomId = "";
                    gameActions.refresh();
                    break;
            }
        } else {
            gameActions.showMessageDialog(object.body.toString());
        }
        gameActions.refresh();
    }

    @Override
    public void onClientConnectionLost() {
        if (action.equals("")) {
            client.connect();
        }
        gameActions.refresh();
    }

    @Override
    public void onClientDisconnected() {
        username = "Client";
        myRoomId = "";
        action = "";
        gameActions.refresh();
    }

    @Override
    public void mainThreadLog(String string) {
    }

    private void sendCredentials() {
        if (action.equals("login")) {
            send(new Protocol(
                    "login",
                    gson.toJson(new User(username, password))
            ));
        } else {
            send(new Protocol(
                    "register",
                    gson.toJson(new User(username, password))
            ));
        }
    }

    private void setUserList(String listString) {
        playerList = new LinkedList<>();
        User[] newUserList = gson.fromJson(listString, User[].class);
        for (int i = 0; i < newUserList.length; i++) {
            if (!newUserList[i].key.equals(myKey)) {
                playerList.add(newUserList[i]);
            }
        }
    }

    private void verifyPartnerList(String info) {
        try {
            User[] newUserList = gson.fromJson(info, User[].class);
            myRoomId = newUserList[0].roomId;
            gameActions.showMessageDialog("Welcome to room");
            partnerList = new LinkedList<>();
            for (int i = 0; i < newUserList.length; i++) {
                partnerList.add(newUserList[i]);
            }
        } catch (Exception e) {
            User user = gson.fromJson(info, User.class);
            getUserByKey(user.key).roomId = user.roomId;
            if (user.roomId.equals(myRoomId)) {
                partnerList.add(user);
                gameActions.showMessageDialog(user.username + " has joined to room");
            }
        }
    }

    private void manageLoginResponse(String response) {
        String loginResponse = response;
        if (loginResponse.equals("OK")) {
            gameActions.refresh();
        }
        gameActions.showMessageDialog(loginResponse);
        action = "";
    }

    private void manageRegisterResponse(String response) {
        String registerResponse = response;
        if (registerResponse.equals("OK")) {
            gameActions.refresh();
        }
        gameActions.showMessageDialog(registerResponse);
        action = "";
    }

    private void addUser(String userString) {
        User newUser = gson.fromJson(userString, User.class);
        if (!newUser.key.equals(myKey)) {
            playerList.add(newUser);
        }
        gameActions.refresh();
    }

    private void removeUser(String userKey) {
        playerList.remove(getUserByKey(userKey));
        User userRemoved = getPartnerByKey(userKey);
        if (partnerList.remove(userRemoved)) {
            gameActions.showMessageDialog(userRemoved.username + " has left the room");
        }
    }

    private void catchInvitation(String stringUser) {
        User userOwner = gson.fromJson(stringUser, User.class);
        int opt = gameActions.showConfirmDialog(userOwner.username + " has invited you to join a room");
        if (opt == 0) {
            myRoomId = userOwner.roomId;
            send(new Protocol("acceptInvitation", userOwner.key));
        } else {
            send(new Protocol("rejectInvitation", userOwner.key));
        }
    }

    private void changePlayerInRoom(String stringUser) {
        User userLeftRoom = gson.fromJson(stringUser, User.class);
        if (userLeftRoom.key.equals(myKey)) {
            myRoomId = "";
            partnerList = new LinkedList<>();
            gameActions.showMessageDialog("You have left the room");
        } else {
            if (partnerList.remove(getPartnerByKey(userLeftRoom.key))) {
                System.out.println(partnerList.size());
                gameActions.showMessageDialog(userLeftRoom.username + " has left the room");
            }
            getUserByKey(userLeftRoom.key).roomId = "";
        }
    }

    private void send(Protocol protocol) {
        client.send(new ContainerObject(
                myKey,
                protocol,
                new String[]{"server"}
        ));
    }

    private User getUserByKey(String key) {
        for (User user : playerList) {
            if (user.key.equals(key)) {
                return user;
            }
        }
        return null;
    }

    private User getPartnerByKey(String key) {
        for (User user : partnerList) {
            if (user.key.equals(key)) {
                return user;
            }
        }
        return null;
    }
}
