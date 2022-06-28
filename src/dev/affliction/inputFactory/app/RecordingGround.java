package dev.affliction.inputFactory.app;

import dev.affliction.inputFactory.components.mouse.Mouse;
import dev.affliction.inputFactory.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class RecordingGround extends JPanel {

    private App app;

    public RecordingGround(App app) { this.app = app; }

    public Rectangle target = new Rectangle(-50,-50,50,50);

    @Override
    public void paint(Graphics g) {
        cursor(g);
        if(app.getDataType().getSelectedItem().toString().toLowerCase().contains("click"))
            g.drawRect((app.getWidth()/2) - 50, (app.getHeight()/2)-50,target.width,target.height);
        else
            g.drawRect(target.x,target.y,target.width,target.height);
        repaint();
    }

    private void cursor(Graphics g) {
        Point p = Mouse.location;
        g.setColor(Color.white);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(Color.BLACK);
        g.drawLine(p.x-10,p.y,p.x+10,p.y);
        g.setColor(Color.RED);
        g.drawLine(p.x,p.y-10,p.x,p.y+10);
        g.setColor(Color.BLACK);
        g.drawLine(p.x-10,p.y+10,p.x+10,p.y-10);
        g.setColor(Color.RED);
        g.drawLine(p.x-10,p.y-10,p.x+10,p.y+10);
    }

    public void shuffleTarget() {
        target.setLocation(Utils.rand(0, getWidth()-50),Utils.rand(0,getHeight()-50));
    }

}
