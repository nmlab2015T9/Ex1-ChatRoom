/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatFrame.java
 *
 * Created on 2010/4/9, 下午 08:39:45
 */

package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Color;
import java.util.*;

import testclient.*;

/**
 *
 * @author Eddie
 */
public class ChatFrame extends javax.swing.JFrame {

    //Client Object
    Client ClientObject;

    //
    String username;

    private Vector<String> userList;
    private Map<Integer, ChatTab> map;
    private Vector<ChatTab> tabs;

    /** Creates new form ChatFrame */
    public ChatFrame( Client co ) {
        initComponents();

        ClientObject = co;
        MainChatTab.setFrame(this);
        MainChatTab.setClientObject(co);

        username = new String();
        userList = new Vector<String>();
        tabs = new Vector<ChatTab>();
        map = new HashMap<Integer, ChatTab>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TabPane = new javax.swing.JTabbedPane();
        MainChatTab = new gui.MainChatTab();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TabPane.addTab("Main", MainChatTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */

    // "/q+" command
    public void addUser ( String user , int texture ) {
        userList.add(user);
        MainChatTab.addUser(user, texture);
    }

    // "/q-" command
    public void delUser ( String user ) {
        userList.remove(user);
        MainChatTab.delUser(user);
    }

    // "/r+" command
    public void addUser ( String user , int texture , int roomID ) {
        ChatTab tab = map.get(roomID);
        tab.addUser(user, texture);
    }

    // "/r-" command
    public void delUser ( String user , int roomID ) {
        ChatTab tab = map.get(roomID);
        tab.delUser(user);
    }

    //add new lines
    public void addNewLine ( String text , String texture , int roomID ) {
        if (roomID == 0) {
            MainChatTab.addNewLine(text, texture);
        }
        else {
            ChatTab tab = map.get(roomID);
            tab.addNewLine(text, texture);
        }
    }

    public void addSysLine ( String text ) {
        MainChatTab.addSysLine(text);
    }

    public void addWarnLine ( String text ) {
        MainChatTab.addWarnLine(text);
    }

    public void setUsername ( String name ) {
        username = name;
        MainChatTab.setUsername(name);
    }

    public void setLastWhisper ( String name , int roomID ) {
        if (roomID == 0) {
            MainChatTab.setLastWhisper(name);
        }
        else {
            ChatTab tab = map.get(roomID);
            tab.setLastWhisper(name);
        }
    }

    // "/a" command
    public void addTab ( int roomID ) {
        ChatTab newTab = new ChatTab(roomID, username);
        newTab.setFrame(this);
        newTab.setClientObject(ClientObject);
        tabs.add(newTab);
        map.put(roomID, newTab);
        TabPane.add("Room "+roomID, newTab);
    }

    // "/l" command
    public void delTab ( int roomID ) {
        ChatTab tab = map.get(roomID);
        tabs.remove(tab);
        map.remove(roomID);
        TabPane.remove(tab);
    }

    // "/c" command
    public void userChangeColor ( String name, int c ) {
        MainChatTab.userChangeColor(name, c);
        for (ChatTab t:tabs) {
            t.userChangeColor(name, c);
        }
    }

    // "/f" command
    public void sendFile( String dest ) {
	ClientObject.sendFile(username, dest);
    }

    public Vector<String> getUserList() {
        return userList;
    }

    public void clear () {
        MainChatTab.clear();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.MainChatTab MainChatTab;
    private javax.swing.JTabbedPane TabPane;
    // End of variables declaration//GEN-END:variables

}