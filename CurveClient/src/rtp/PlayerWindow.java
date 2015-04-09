package rtp;

import javax.media.Player;
import javax.media.rtp.ReceiveStream;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by user on 2015/4/8.
 */
public class PlayerWindow extends Frame {
    Player player;
    ReceiveStream stream;

    PlayerWindow(Player p, ReceiveStream strm) {
        player = p;
        stream = strm;
    }

    public void initialize() {
        add(new PlayerPanel(player));
    }

    public void close() {
        player.close();
        setVisible(false);
        dispose();
    }

    public void addNotify() {
        super.addNotify();
        pack();
    }

    public void addClose(){
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                close();
            }
        });
    }
}
