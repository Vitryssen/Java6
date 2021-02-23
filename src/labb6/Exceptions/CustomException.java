/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.Exceptions;

/**
 *
 * @author André
 */
public class CustomException extends Exception{
    String message;
    public CustomException(){
        System.out.println("custom");
    }
    public CustomException(String msg){
        super(msg);
        this.message = msg;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
