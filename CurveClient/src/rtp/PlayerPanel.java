package rtp;

import javax.media.Player;
import java.awt.*;

/**
 * Created by user on 2015/4/8.
 */
public class PlayerPanel extends Panel {
    Component vc, cc;

    PlayerPanel(Player p) {
        setLayout(new BorderLayout());
        if ((vc = p.getVisualComponent()) != null)
            add("Center", vc);
        if ((cc = p.getControlPanelComponent()) != null)
            add("South", cc);
    }

    public Dimension getPreferredSize() {
        int w = 0, h = 0;
        if (vc != null) {
            Dimension size = vc.getPreferredSize();
            w = size.width;
            h = size.height;
        }
        if (cc != null) {
            Dimension size = cc.getPreferredSize();
            if (w == 0)
                w = size.width;
            h += size.height;
        }
        if (w < 160)
            w = 160;
        return new Dimension(w, h);
    }
}
