/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.DataStructures;

/**
 *
 * @author André
 */
public class Message {
    private final Friend author;
    private final String message;
    public Message(Friend user, String newMessage){
        this.author = user;
        this.message = newMessage;
    }
    public Friend getAuthor(){
        return author;
    }
    public String getMessage(){
        return message;
    }
}
