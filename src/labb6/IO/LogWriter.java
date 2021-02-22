/*
 * André Nordlund
 * 2021-02-19
 * Java 2
 * Lab 4
 */
package labb6.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import labb6.DataStructures.Friend;
import labb6.DataStructures.Message;

/**
 *
 * @author André
 */
public class LogWriter {
    public LogWriter(Friend nick, List<Message> msg){
        String workingPath = System.getProperty("user.dir");
        File file=new File(workingPath+"\\logs\\"+nick.getNick()+nick.getTag()+".log");  
        try {
            if (file.createNewFile()){
                System.out.println("File is created!");
            }
            else{
                System.out.println("File already exists.");
            }
            FileWriter fr = new FileWriter(file, false);
            for(int i = 0; i < msg.size(); i++){
                fr.write("<"+msg.get(i).getAuthor().getNick()+msg.get(i).getAuthor().getTag()+">"+msg.get(i).getMessage()+"\n");
            }
            fr.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
