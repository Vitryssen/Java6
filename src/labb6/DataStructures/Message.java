/*
 * André Nordlund
 * 2021-02-19
 * Java 2
 * Lab 4
 */
package labb6.DataStructures;

/**
 *
 * @author André
 */
public class Message {
    private Friend author;
    private String message;
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
