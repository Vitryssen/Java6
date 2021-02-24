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
public class CustomExceptionHandler {
    /*
    Codes:
    0: Invalid ip
    1: User tried to logon with your nick
    2: The nick from a message does not exist in friendlist
    */
    private SystemExceptionHandler logger = new SystemExceptionHandler();
    
    public CustomExceptionHandler(CustomException ex){
        switch(ex.getCode()){
            case 0 -> {
                manageIpException(ex);
            }
            case 1 -> {
                manageUsedNickException(ex);
            }
            case 2 -> {
                manageMissingNick(ex);
            }
            case 3 -> {
                manageInvalidMessageFormat(ex);
            }
        }
    }
    private void manageIpException(CustomException ex){
        logger.write("Code: "+ex.getCode()+" Error message: "+ex.getMessage()+"; Invalid IP address given from server");
    }
    private void manageUsedNickException(CustomException ex){
        logger.write("Code: "+ex.getCode()+" Error message: "+ex.getMessage()+"; Another user tried to log on with the same nick");
    }
    private void manageMissingNick(CustomException ex){
        logger.write("Code: "+ex.getCode()+" Error message: "+ex.getMessage()+"; Received a message that came from a friend not in the friendlist");
    }
    private void manageInvalidMessageFormat(CustomException ex){
        logger.write("Code: "+ex.getCode()+" Error message: "+ex.getMessage()+"; Given message from server is incorrectly formatted");
    }
}
