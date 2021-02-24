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
    String message = null;
    Integer code;
    public CustomException(){
        super();
    }
    public CustomException(String msg){
        super(msg);
        this.message = msg;
    }
    public CustomException(int code){
        super();
        this.code = code;
    }
    public CustomException(String msg, int code){
        super(msg);
        this.message = msg;
        this.code = code;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
    public int getCode(){
        return this.code;
    }
}
