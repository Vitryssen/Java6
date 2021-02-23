/*
 * André Nordlund
 * 2021-02-23
 * Java 2
 * Lab 6
 */
package labb6.Windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.border.Border;
import labb6.DAO.ChatDAOImp;
import labb6.DataStructures.Friend;
import labb6.DAO.ChatDAO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import labb6.DAO.FriendDAO;
import labb6.DAO.FriendDAOImp;
import labb6.Exceptions.SystemExceptionHandler;
import labb6.Socket.Client;
/**
 *
 * @author André
 */
public class MainWindow {
    //Main window
    private final JFrame f;  
    //DAO's
    private final ChatDAO chatDao = new ChatDAOImp();
    private final FriendDAO friendDao = new FriendDAOImp();
    //Windows
    private final TopWindow topWindow = new TopWindow();
    private final ChatWindow chatWindow = new ChatWindow();
    private final FriendWindow friendsWindow = new FriendWindow();
    //Components
    private final JCheckBox publicButton = topWindow.getPublicButton();
    private final JCheckBox privateButton = topWindow.getPrivateButton();
    //Variables
    boolean privateMode = false;
    boolean exception = false;
    //Network socket
    private final Client readerThread = new Client(chatDao, "chatbot.miun.se", 8000);//Skolan : "chatbot.miun.se", 8000 hemma: "0.0.0.0", 8000
    public MainWindow() throws IOException{
        f=new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(topWindow.getWindow(), BorderLayout.NORTH);
        
        topWindow.getShowPanel().setBounds(111,45,110,70);
        topWindow.getExitPanel().setBounds(6,45,100,40);
        //----------------------------------------
        f.add(topWindow.getShowPanel());
        f.add(topWindow.getExitPanel());
        //----------------------------------------
        String nick = nickNameInput();
        chatDao.setChatUser(nick);
        //----------------------------------------
        readerThread.connect();
        readerThread.register(nick);
        updateThread();
        //----------------------------------------
        addPublicClick();
        addPrivateClick();
        addSendChatClick();
        exitButtonEvent();
        //----------------------------------------
        JPanel bottom = new JPanel();
        bottom.add(chatWindow.getWindow(), BorderLayout.WEST);
        bottom.add(friendsWindow.getWindow(), BorderLayout.EAST);
        //-----------------------------------------
        Border blackline = BorderFactory.createLineBorder(Color.black);
        bottom.setBorder(blackline);
        f.setTitle(nick);
        f.add(bottom);
        f.pack();
        f.setVisible(true); 
        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                resize();
            }
        });
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }
    private void updateThread(){
        new Thread(() -> {
            while(true){
                SwingUtilities.invokeLater(() -> {
                    friendDao.setFriendlist(readerThread.getFriends());
                    populateFriendlist();
                    if(!"Not chatting".equals(chatWindow.getChatLabel().getText())){
                        if(privateMode){
                            loadPrivateChat();
                        }
                        else{
                            loadPublicChat();
                        }
                    }
                });
                try {
                    java.lang.Thread.sleep(100);
                }
                catch(InterruptedException e) {
                    new SystemExceptionHandler().manageExceptionInterrupted(e.toString(),"Update");
                }
            }
        }).start();
    }
    public void resize(){
        chatWindow.getWindow().setPreferredSize(new Dimension(f.getWidth()-friendDao.getLongestNick()-40, f.getHeight()-80));
        friendsWindow.getWindow().setPreferredSize(new Dimension(friendDao.getLongestNick()+10,f.getHeight()-80));
        f.revalidate();
        f.repaint();
    }
    private void exit(){
        chatDao.saveChats();
        readerThread.sendLogout();
        f.dispose();
        System.exit(0);
    }
    private void addSendChatClick(){
        chatWindow.getMessageButton().addMouseListener(new MouseAdapter() { 
            @Override
            public void mousePressed(MouseEvent me) { 
                if(!"Not chatting".equals(chatWindow.getChatLabel().getText()) && chatWindow.getMessageInput().getText().length() > 0){
                    chatWindow.getChatText().setText("");
                    if(me.getButton() == 1 && privateMode){
                        chatDao.sendMessagePrivate(chatDao.getChatUser(),chatDao.getReceiever(),chatWindow.getMessageInput().getText());
                        readerThread.sendPrivateMessage(chatWindow.getMessageInput().getText(), chatDao.getReceiever().getNick());
                        loadPrivateChat();
                        chatWindow.getMessageInput().setText("");
                    }
                    else if(me.getButton() == 1 && !privateMode){
                        chatDao.setReciever(chatDao.getChatUser().getNick());
                        chatDao.sendMessagePublic(chatDao.getChatUser(),chatWindow.getMessageInput().getText());
                        readerThread.sendPublicMessage(chatWindow.getMessageInput().getText());
                        List<String> messages = chatDao.getChat(chatDao.getChatUser().getNick());
                        for(int i = 0; i < messages.size(); i++){
                            chatWindow.getChatText().append(messages.get(i));
                        }
                        chatWindow.getMessageInput().setText("");
                    }
                }
            } 
        });
    }
    private String nickNameInput(){
        String value = "";
        while(value.length() < 1){
            value = JOptionPane.showInputDialog(null, "Chose nickname", 
                        "Nickname: ", JOptionPane.INFORMATION_MESSAGE);
            if(value == null){
                System.exit(0);
            }
            if(value.length() < 1)
                JOptionPane.showMessageDialog(f, "Nickname cannot by empty");
        }
        return value;
    }
    private void popUp(String user){
        String[] options = {"Fullname","Image"};
        String attr = (String)JOptionPane.showInputDialog(null, "What attribute do you want to change?", 
                "Change attribute for "+user, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if(attr != null){
            if(!chatDao.isChatLoaded(user)){
                JOptionPane.showMessageDialog(f,"Please load the chat atleast once before changing nick","Alert",JOptionPane.WARNING_MESSAGE);
                return;
            }
            String value = JOptionPane.showInputDialog(null, attr+": ", 
                    "New value for "+attr, JOptionPane.INFORMATION_MESSAGE);
            if(value.length() == 0)
                return;
            friendDao.changeFriendAttr(user, attr, value);
        }
    }
    private void populateFriendlist(){
        friendsWindow.getNamePanel().removeAll();
        for(int i = 0; i < friendDao.getAllFriends().size(); i++){
            Friend currentFriend = friendDao.getAllFriends().get(i);
            String currentName = currentFriend.getNick();
            JLabel nameLabel = new JLabel(currentName+currentFriend.getTag());
            nameLabel.setName(currentName);
            friendsWindow.getNamePanel().add(nameLabel, BorderLayout.WEST);
        }
        addClickListiner();
        resize();
    }
    private void addClickListiner(){
        for(int i = 0; i < friendsWindow.getNamePanel().getComponentCount(); i++){
            friendsWindow.getNamePanel().getComponent(i).addMouseListener(new MouseAdapter() { 
                @Override
                public void mousePressed(MouseEvent me) { 
                    if(me.getButton() == 3){
                        popUp(me.getComponent().getName());
                    }
                    else if(privateMode == true && me.getButton() == 1){
                        chatDao.setReciever(me.getComponent().getName());
                        JLabel labelText = (JLabel) me.getComponent();
                        chatWindow.getChatLabel().setText("Chatting with "+labelText.getText());
                        chatWindow.getChatText().setText("");
                    }
                } 
            });
        }
    }
    private void loadPrivateChat(){
        chatWindow.getChatText().setText("");
        List<String> history = chatDao.updateChat(chatDao.getReceiever().getNick());
        for(int i = 0; i < history.size(); i++){
            chatWindow.getChatText().append(history.get(i));
        }
    }
    private void loadPublicChat(){
        chatWindow.getChatText().setText("");
        if(publicButton.isSelected()){
            chatDao.setReciever(chatDao.getChatUser().getNick());
            chatWindow.getChatLabel().setText("Chatting publicly");
            List<String> messages = chatDao.updateChat(chatDao.getReceiever().getNick());
            for(int i = 0; i < messages.size(); i++){
                chatWindow.getChatText().append(messages.get(i));
            }
        }
    }
    private void addPublicClick(){
        publicButton.addActionListener((ActionEvent e) -> { 
            chatWindow.getChatLabel().setText("Chatting publicly");
            chatDao.setReciever(chatDao.getChatUser().getNick());
            privateButton.setSelected(false);
            privateMode = false;
            chatWindow.getChatText().setText("");
            List<String> messages = chatDao.getChat(chatDao.getChatUser().getNick());
            for(int i = 0; i < messages.size(); i++){
                chatWindow.getChatText().append(messages.get(i));
            }
            topWindow.getShowPanel().setVisible(false);
            topWindow.getExitPanel().setVisible(false);
        });
    }
    private void addPrivateClick(){
        privateButton.addActionListener((ActionEvent e) -> {
            privateMode = true;
            publicButton.setSelected(false);
            chatWindow.getChatLabel().setText("Not chatting");
            chatWindow.getChatText().setText("");
            topWindow.getShowPanel().setVisible(false);
            topWindow.getExitPanel().setVisible(false);
        });
    }
    private void exitButtonEvent(){
        topWindow.getExitButton().addActionListener((ActionEvent e) -> {
            exit();
        });
    }
}
