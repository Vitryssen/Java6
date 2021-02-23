/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.Exceptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author André
 */
public class SystemExceptionHandler {
    public void manageExceptionFileNotFound(String extra){
        write(extra);
        JOptionPane.showMessageDialog(null,
            extra,
            "File error",
            JOptionPane.ERROR_MESSAGE);
    }
    public void manageExceptionSocketNotAlive(String ex){
        write(ex);
        int response = JOptionPane.showConfirmDialog(null, "No connection to server\nTry to reconnect? \nSelecting no will close the program", "No connection",
        JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null,
            "Trying to reconnect...",
            "Reconnecting",
            JOptionPane.INFORMATION_MESSAGE);
        } 
        else if (response == JOptionPane.NO_OPTION) {
          System.exit(0);
        }
    }
    public void manageExceptionUnknownHost(String ex){
        write(ex);
        JOptionPane.showMessageDialog(null,
            "Cant reach given hostname\nClosing program",
            "Cant resolve hostname",
            JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    public void manageExceptionInterrupted(String ex, String extra){
        write(ex);
        JOptionPane.showMessageDialog(null,
            extra+" Thread interrupted during sleep",
            "Error with thread",
            JOptionPane.ERROR_MESSAGE);
    }
    public void manageExceptionIO(String ex){
        write(ex);
        JOptionPane.showMessageDialog(null,
            "Failed to open/read file",
            "File error",
            JOptionPane.ERROR_MESSAGE);
    }
    public void manageExceptionIO(String ex, String extra){
        write(ex+" "+extra);
        JOptionPane.showMessageDialog(null,
            "Failed to open/read file",
            "File error",
            JOptionPane.ERROR_MESSAGE);
    }
    public void manageExceptionNotConnected(String ex){
        write(ex);
        JOptionPane.showMessageDialog(null,
            "Failed to connect to server on registration\nCheck your connection and try again\nClosing program",
            "Not connected",
            JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    public void write(String error){
        String workingPath = System.getProperty("user.dir");
        File file=new File(workingPath+"\\logs\\system.log");  
        try {
            if (file.createNewFile()){
                System.out.println("File is created!");
            }
            else{
                System.out.println("File already exists.");
            }
            FileWriter fr = new FileWriter(file, true);
            fr.write(error);
            fr.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
            "Writing error to file failed",
            "Cant write error to system log",
            JOptionPane.ERROR_MESSAGE);
        }
    }
}
