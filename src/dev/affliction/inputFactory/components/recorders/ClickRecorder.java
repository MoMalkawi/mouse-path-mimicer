package dev.affliction.inputFactory.components.recorders;

import dev.affliction.inputFactory.components.mouse.Mouse;
import dev.affliction.inputFactory.data.Clicks;
import dev.affliction.inputFactory.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ClickRecorder {

    /*
    TODO: add a method to constrict movement only inside of bounding box (When implemented inside client)
     */

    public static long previousTime = 0;

    public static long previousDragTime = 0;

    public static void startClickRecording(MouseEvent e) {
        Clicks.drags = new ArrayList<>();
        previousTime = System.currentTimeMillis();
        Clicks.drags.add(new int[] {e.getPoint().x,e.getPoint().y});
    }

    public static void registerClickDrag(MouseEvent e) {
        if(previousDragTime == 0) previousDragTime = previousTime;
        long current = System.currentTimeMillis();
        Clicks.drags.add(new int[] {e.getPoint().x, e.getPoint().y,
                (int) (current - previousDragTime)});
        previousDragTime = current;
    }

    public static void playClick(int[] clickDrags, int clickX, int clickY, int clickType) {
        Mouse.event(clickX,clickY,System.currentTimeMillis(),clickType,1,MouseEvent.MOUSE_PRESSED);
        //if(click.drags.size() > 0) {
        int[] lastPoint = new int[] {clickDrags[0], clickDrags[1]};
        int rand = Utils.rand(3,clickDrags.length);
        double randAngle = Math.toRadians(Utils.rand(-180,180));
        for(int i = 3; i < clickDrags.length-3; i+=3) {
            double dis = new Point(lastPoint[0],lastPoint[1]).distance(clickDrags[i],clickDrags[i+1]);
            if(i == rand) {
                addLoop(clickType);
                if(i < (clickDrags.length - 1)) rand = Utils.rand(i+1, clickDrags.length); //TODO> Random value may be y
            }
            Utils.sleep(Math.abs(Utils.deviateFromMin(clickDrags[i+2], 1, 10))); // Make the "10" configurable in client
            int x = (int) ((Mouse.location.x + (clickDrags[i] - lastPoint[0])) + dis * Math.cos(randAngle));
            int y = (int) ((Mouse.location.y + (clickDrags[i+1] - lastPoint[1])) + dis * Math.sin(randAngle));
            Mouse.event(x,y,System.currentTimeMillis(),clickType,1,
                    MouseEvent.MOUSE_DRAGGED);
            lastPoint[0] = clickDrags[i];
            lastPoint[1] = clickDrags[i+1];
        }
        Mouse.event(Mouse.location.x,Mouse.location.y,System.currentTimeMillis(),clickType,1,
                MouseEvent.MOUSE_RELEASED);
    }

    private static void addLoop(int clickType) {
        int num = Utils.rand(-3,3);
        if(num == 0) return;
        System.out.println("BEFORE: "+Mouse.location);
        Utils.sleep(Utils.rand(1,5));
        Mouse.event(Mouse.location.x + num,Mouse.location.y + num,System.currentTimeMillis(),clickType,1,
                MouseEvent.MOUSE_DRAGGED);
        System.out.println("AFTER ADDITION: "+Mouse.location);
        Utils.sleep(Utils.rand(1,5));
        Mouse.event(Mouse.location.x - num,Mouse.location.y - num,System.currentTimeMillis(),clickType,1,
                MouseEvent.MOUSE_DRAGGED);
        System.out.println("AFTER RETREAT: "+Mouse.location);
    }

}
