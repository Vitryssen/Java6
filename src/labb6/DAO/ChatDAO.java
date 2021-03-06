/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.DAO;

import java.util.List;
import labb6.DataStructures.Friend;

/**
 *
 * @author André
 */
public interface ChatDAO {
    //Functions
    public void saveChats();
    public void saveChat(String nick);
    public void sendMessagePublic(Friend newFriend, String msg);
    public void sendMessagePrivate(Friend author, Friend receiver, String msg);
    public List<String> updateChat(String nick);
    //Getters
    public List<String> getChat(String nick);
    public Friend getReceiever();
    public Friend getChatUser();
    //Setters
    public void setReciever(String newReciever);
    public void setChatUser(String newUser);
    //Booleans
    public boolean isChatLoaded(String nick);
}
