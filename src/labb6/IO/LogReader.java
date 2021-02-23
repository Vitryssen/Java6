/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import labb6.DataStructures.Friend;
import labb6.DataStructures.Message;
import labb6.Exceptions.SystemExceptionHandler;


/**
 * @author André Nordlund
 * @date 2021-02-10
 * @course name Java 2
 * @Lab number 3
 */
public class LogReader {
    private List<Message> loadedMsgs = new ArrayList<Message>();
    private String workingPath;
    private final Map<String, List<Message>> userChats = new HashMap<String, List<Message>>(); 
    private final SystemExceptionHandler logger = new SystemExceptionHandler();
    public void readFile(String fileUrl){
        String orgName = fileUrl;
        try
        {
            this.workingPath = System.getProperty("user.dir");
            File file=new File(this.workingPath+"\\logs\\");    //creates a new file instance
            //------------------------------------------------------------
            String[] pathnames = file.list();
            String[] orgs = file.list();
            fileUrl += ".log";
            for(int i = 0; i < pathnames.length;i++){
                if(pathnames[i].indexOf('[') != -1){ //Formatt all files in dir to remove tags
                    pathnames[i] = pathnames[i].substring(0, pathnames[i].indexOf('['))+pathnames[i].substring(pathnames[i].indexOf(']')+1, pathnames[i].length());
                }
                if(fileUrl.equals(pathnames[i])){ //If formatted filename matches with input, open the unformatted file
                    file=new File(this.workingPath+"\\logs\\"+orgs[i]);
                }
            }
            //-------------------------------------------------------------
            FileReader fr=new FileReader(file);   //reads the file  
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
            String line; 
            String username;
            while((line=br.readLine())!=null)  {
                String tag = "";
                String text = line.substring(line.indexOf('>')+1);
                if(line.indexOf(']') != -1){
                    username = line.substring(1,line.indexOf('['));
                    tag = line.substring(line.indexOf('['),line.indexOf(']')+1);
                }
                else{
                    username = line.substring(1, line.indexOf('>'));
                }
                Friend currentFriend = new Friend();
                currentFriend.setNick(username);
                currentFriend.setTag(tag);
                Message currentMsg = new Message(currentFriend, text);
                loadedMsgs.add(currentMsg);
            }
            userChats.put(orgName, loadedMsgs); //Saves the chat to the given username
            loadedMsgs = new ArrayList<Message>();
            fr.close();
            br.close();
        }
        catch (FileNotFoundException ex) 
        {
            Friend currentFriend = new Friend();
            Message currentMsg = new Message(currentFriend, "");
            loadedMsgs.add(currentMsg);
            userChats.put(orgName, loadedMsgs);
            loadedMsgs = new ArrayList<Message>();
            logger.manageExceptionFileNotFound("Log for user "+orgName+" could not be found, creating a new one");
        } 
        catch (IOException ex) 
        {
            logger.manageExceptionIO(ex.toString(), "I/O error in LogReader");
        }
    }
    public Map<String, List<Message>> getChats(){
        return userChats;
    }
}
