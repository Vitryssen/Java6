/*
 * André Nordlund
 * 2021-02-19
 * Java 2
 * Lab 4
 */
package labb4.DAO;

import java.util.Map;
import java.util.List;
import labb4.IO.LogWriter;
import java.util.ArrayList;
import labb4.DataStructures.Chat;
import labb4.DataStructures.Friend;
import labb4.DataStructures.Message;

/**
 *
 * @author André
 */
public class ChatDAOImp implements ChatDAO{
    private FriendDAO friendDao = new FriendDAOImp();
    private Friend chatUser = new Friend();
    private Friend chattingWith = new Friend();
    private final Chat allChats = new Chat(chatUser.getNick());
    public ChatDAOImp(){
        
    }
    @Override
    public void saveChats(){
        Map<String, List<Message>> chats = allChats.getAllChats();
        for(String key: chats.keySet()){
            Friend newFriend = new Friend();
            newFriend.setNick(key);
            for(int i = 0; i < friendDao.getAllFriends().size(); i++){
                if(friendDao.getAllFriends().get(i).getNick().equals(key))
                    newFriend = friendDao.getAllFriends().get(i);
            }
            new LogWriter(newFriend, chats.get(key));
        }
    }
    @Override
    public void saveChat(String nick){
        Map<String, List<Message>> chats = allChats.getAllChats();
        for(String key: chats.keySet()){
            if(key == nick){
                Friend newFriend = new Friend();
                newFriend.setNick(key);
                for(int i = 0; i < friendDao.getAllFriends().size(); i++){
                    if(friendDao.getAllFriends().get(i).getNick().equals(key))
                        newFriend = friendDao.getAllFriends().get(i);
                }
                new LogWriter(newFriend, chats.get(key));
            }
        }
    }
    @Override
    public List<String> getChat(String nick) { 
        List<String> returnList = new ArrayList<>();
        List<Message> msgs = allChats.getMessages(nick);
        for(int i = 0; i < msgs.size(); i++){
            Friend author = msgs.get(i).getAuthor();
            String newString = "";
            if(author.getNick().length() > 0)
               newString = "<"+author.getNick()+author.getTag()+"> "+msgs.get(i).getMessage()+"\n";
            returnList.add(newString);
        }
        return returnList;
    }
    @Override
    public void setChatUser(String newUser) {
        this.chatUser.setNick(newUser);
    }
    @Override
    public void sendMessagePublic(Friend author, String msg){
        if(msg.length() > 0){
            Message newMsg = new Message(author, msg);
            allChats.addMessage(newMsg, chatUser.getNick());
        }
    }
    @Override
    public void sendMessagePrivate(Friend author, Friend receiver, String msg){
        if(msg.length() > 0){
            Message newMsg = new Message(author, msg);
            allChats.addMessage(newMsg, receiver.getNick());
        }
    }
    @Override
    public void setReciever(String newReciever){
        chattingWith.setNick(newReciever);
    }
    @Override
    public Friend getReceiever(){
        return chattingWith;
    }
    @Override
    public Friend getChatUser(){
        return chatUser;
    }
    @Override
    public boolean isChatLoaded(String nick){
        return allChats.chatExists(nick);
    }
}
