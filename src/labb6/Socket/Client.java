/*
 * André Nordlund
 * 2021-02-19
 * Java 2
 * Lab 4
 */
package labb6.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import labb6.DAO.ChatDAO;
import labb6.DAO.ChatDAOImp;
import labb6.DataStructures.Friend;
import labb6.DataStructures.Message;
import labb6.Exceptions.SystemExceptionHandler;

/**
 *
 * @author André
 */
public class Client implements HostListener {

    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final String host;
    private final int port;

    private final String register = "<REGISTER><%1$s><%2$s><%3$s><IMAGE>";
    private final String privateMessage = "<PRIVATE><%1$s><%2$s><%3$s>";
    private final String publicMessage = "<PUBLIC><%1$s><%2$s>";
    private final String logout = "<LOGOUT><%1$s>";

    private String nickName;
    private String fullName;
    private boolean connected;
    private String ip;
    
    private List<Friend> friends = new ArrayList<>();
    
    private ChatDAO chatDao = new ChatDAOImp();
    
    public Client(ChatDAO newDao, String host, int port) {
        this.chatDao = newDao;
        this.host = host;
        this.port = port;
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException uhe) {
            System.err.println("Coudn't access local interface adress, using 127.0.0.1");
            this.ip = "127.0.0.1";
        }
    }

    public void register(String nickname, String fullname) {
        assertConnected();
        this.nickName = nickname;
        this.fullName = fullname;
        sendRegister();

    }

    public void sendPublicMessage(String messageText) {
        String message = String.format(publicMessage, nickName, messageText);
        out.println(message);
        out.flush();
    }
    
    public void sendPrivateMessage(String messageText, String receiver) {
        String message = String.format(privateMessage, nickName, receiver, messageText);
        out.println(message);
        out.flush();
    }
    
    public void sendLogout() {
        String message = String.format(logout, nickName);
        out.println(message);
        out.flush();
        out.close();
        try {
            in.close();
            hostSocket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    public void register(String name) {
        register(name, name);
    }

    public void connect() throws UnknownHostException, IOException {
        try{
            hostSocket = new Socket(host, port);
            out = new PrintWriter(hostSocket.getOutputStream());
            in = new BufferedReader(
                    new InputStreamReader(hostSocket.getInputStream()));
            connected = true;
        }
        catch(UnknownHostException e){new SystemExceptionHandler().manageExceptionUnknownHost(e.toString());}
        catch(IOException e){new SystemExceptionHandler().manageExceptionIO(e.toString());}
        finally{
            new ListenerThread(this, in).start();
            new AliveThread().start();
        }
    }
    private void sendRegister() {
        assertConnected();
        String message = String.format(register, nickName, fullName, ip);
        out.println(message);
        out.flush();
    }

    private void assertConnected(){
        if (!connected) {
            new SystemExceptionHandler().manageExceptionNotConnected("Not connected when trying to register");
        }
    }

    @Override
    public void messageRecieved(String message) {
        if(message.contains("<FRIEND>")){
            addFriend(message);
        }
        else if(message.contains("<LOGOUT>")){
            removeFriend(message);
        }
        else if(message.contains("<PUBLIC>")){
            addServerMessage(message, true);
        }
        else if(message.contains("<PRIVATE>")){
            addServerMessage(message, false);
        }
    }
    private void addServerMessage(String old, boolean publicMode){
        Friend newFriend = new Friend();
        //-------------------------------------
        String temp = old;
        temp = temp.substring(temp.indexOf(">")+2);
        String nick = temp.substring(0, temp.indexOf(">"));
        newFriend.setNick(nick);
        temp = temp.substring(temp.indexOf(">")+2);
        String message = temp.substring(0, temp.indexOf(">"));
        //--------------------------------------
        Message newMsg = new Message(newFriend, message);
        if(publicMode)
            chatDao.sendMessagePublic(newFriend, message);
        else
            chatDao.sendMessagePrivate(newFriend, newFriend, message);
        
        Thread writeToFile = new Thread(new Runnable() {
        public void run()
        {
             chatDao.saveChat(nick);
        }});  
        writeToFile.start();
    }
    private void addFriend(String newFriend){
        Friend currentFriend = new Friend();
        String start = newFriend.substring(newFriend.indexOf("<", newFriend.indexOf("<")+2)+1);
        
        currentFriend.setNick(start.substring(0, start.indexOf(">")));
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(currentFriend.getNick()))
                return;
        }
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setName(start.substring(0, start.indexOf(">")));
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setIp(start.substring(0, start.indexOf(">")));
        start = start.substring(start.indexOf(">")+2);
        currentFriend.setImage(start.substring(0, start.indexOf(">")));
        
        friends.add(currentFriend);
    }
    private void removeFriend(String nick){
        String newNick = nick.substring(nick.indexOf('>')+1);
        newNick = newNick.substring(1, newNick.length()-1);
        for(int i = 0; i < friends.size(); i++){
            if(friends.get(i).getNick().equals(newNick))
                friends.remove(i);
        }
    }
    public List<Friend> getFriends(){
        return friends;
    }
    private class AliveThread extends Thread{
        public void run(){
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            while(true){
                Socket socket = new Socket();
                try{
                    socket.connect(socketAddress, 2000);
                    socket.close();
                    connected = true;
                    Thread.sleep(2000);
                } catch (IOException ex) {
                   new SystemExceptionHandler().manageExceptionSocketNotAlive(ex.toString());
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
    private class ListenerThread extends Thread {

        private final HostListener listener;
        private final BufferedReader in;

        public ListenerThread(HostListener listener, BufferedReader in) {
            this.listener = listener;
            this.in = in;
        }

        public void run() {
            try {
                while (connected) {
                    String line = in.readLine();
                    listener.messageRecieved(line);
                }
            } catch (IOException ex) {
                new SystemExceptionHandler().manageExceptionIO(ex.toString());
            }
        }
    }

}
