/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import labb6.DataStructures.Friend;
import labb6.DataStructures.Message;
import labb6.Exceptions.SystemExceptionHandler;

/**
 *
 * @author André
 */
public class LogWriter {
    public LogWriter(Friend currentFriend, List<Message> msg){
        String workingPath = System.getProperty("user.dir");
        File file=new File(workingPath+"\\logs\\"+currentFriend.getNick()+currentFriend.getTag()+".log");  
        try {
            if (file.createNewFile()){
                new SystemExceptionHandler().write("New file created for "+currentFriend.getNick());
            }
            FileWriter fr = new FileWriter(file, false);
            for(int i = 0; i < msg.size(); i++){
                fr.write("<"+msg.get(i).getAuthor().getNick()+msg.get(i).getAuthor().getTag()+">"+msg.get(i).getMessage()+"\n");
            }
            fr.close();
        } catch (IOException ex) {
            new SystemExceptionHandler().manageExceptionIO(ex.toString(), "I/O error in LogWriter");
        }
    }
}
