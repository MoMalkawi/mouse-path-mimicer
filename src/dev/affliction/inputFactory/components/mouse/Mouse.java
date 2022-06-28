package dev.affliction.inputFactory.components.mouse;

import dev.affliction.inputFactory.app.App;
import dev.affliction.inputFactory.components.recorders.ClickRecorder;
import dev.affliction.inputFactory.components.recorders.PathRecorder;
import dev.affliction.inputFactory.data.Clicks;
import dev.affliction.inputFactory.data.Paths;
import dev.affliction.inputFactory.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    public static Point location = new Point(-1,-1);

    public static int LEFT_BUTTON = MouseEvent.BUTTON1;
    public static int MIDDLE_BUTTON = MouseEvent.BUTTON2;
    public static int RIGHT_BUTTON = MouseEvent.BUTTON3;

    public static boolean onScreen = false;

    private static Component canvas;
    private static App app;

    public Mouse(App app) {
        Mouse.app = app;
        Mouse.canvas = app.getRg();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    public static void event(int x, int y, long time, int eventID) {
        canvas.dispatchEvent(new MouseEvent(canvas, eventID, time,
                0, x, y, 0,false));
    }

    public static void event(int x, int y, long time, int button, int clickCount, int eventID) {
        int btnMask = (button == 1 ? MouseEvent.BUTTON1_DOWN_MASK : 0) | (button == 2 ? (MouseEvent.BUTTON2_DOWN_MASK |
                MouseEvent.META_DOWN_MASK) : 0) | (button == 3 ? (MouseEvent.BUTTON3_DOWN_MASK | MouseEvent.META_DOWN_MASK) : 0);
        int buttonNum = 0;
        switch (button) {
            case 1:
                buttonNum = MouseEvent.BUTTON1;
                break;
            case 2:
                buttonNum = MouseEvent.BUTTON2;
                break;
            case 3:
                buttonNum = MouseEvent.BUTTON3;
                break;
        }
        canvas.dispatchEvent(new MouseEvent(canvas, eventID, time,
                btnMask, x, y, clickCount,false, buttonNum));
    }

    public static void handleOutOfScreen(MouseEvent e) {
        if(onScreen && (e.getPoint().x > canvas.getWidth() || e.getPoint().x < canvas.getWidth()
        || e.getPoint().y > canvas.getHeight() || e.getPoint().y < canvas.getHeight()))
            event(e.getPoint().x, e.getPoint().y,System.currentTimeMillis(),MouseEvent.MOUSE_EXITED);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*if(app.recording) { // NO LONGER RECORDING DRAG-LESS CLICKS
            switch ((String)app.getDataType().getSelectedItem()) {
                case "Fast-Clicks":
                    if(Clicks.drags == null) break;
                    HumanClick.current.releaseTime = (int) (System.currentTimeMillis() - ClickRecorder.previousTime);
                    HumanClick.RAPID_CLICKS.add(HumanClick.current);
                    HumanClick.current = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
                case "Regular Clicks":
                    if(HumanClick.current == null) break;
                    HumanClick.current.releaseTime = (int) (System.currentTimeMillis() - ClickRecorder.previousTime);
                    HumanClick.REGULAR_CLICKS.add(HumanClick.current);
                    HumanClick.current = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
                case "Slower-Draggy Clicks":
                    if(HumanClick.current == null) break;
                    HumanClick.current.releaseTime = (int) (System.currentTimeMillis() - ClickRecorder.previousTime);
                    HumanClick.SLOWER_DRAGGY_CLICKS.add(HumanClick.current);
                    HumanClick.current = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
            }
        }*/
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(app.recording) {
            switch ((String)app.getDataType().getSelectedItem()) {
                case "Regular Moves":
                    PathRecorder.finalizePath();
                    Paths.STANDARD_MOVEMENTS.put(Utils.to1D(Paths.pointsList.toArray(new int[0][])),Paths.longestDistanceFromOrigin);
                    Paths.pointsList = null;
                    break;
                case "Slower Angular Moves":
                    PathRecorder.finalizePath();
                    Paths.SLOWER_ANGULAR_MOVEMENTS.put(Utils.to1D(Paths.pointsList.toArray(new int[0][])),Paths.longestDistanceFromOrigin);
                    Paths.pointsList = null;
                    break;
                case "Rapid Moves":
                    PathRecorder.finalizePath();
                    Paths.RAPID_MOVEMENTS.put(Utils.to1D(Paths.pointsList.toArray(new int[0][])),Paths.longestDistanceFromOrigin);
                    Paths.pointsList = null;
                    break;
                case "Fast-Clicks":
                case "Regular Clicks":
                case "Slower-Draggy Clicks":
                    ClickRecorder.startClickRecording(e);
                    break;
            }
            if(app.getRg().target.contains(e.getPoint())) app.getRg().shuffleTarget();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(app.recording) {
            switch ((String)app.getDataType().getSelectedItem()) {
                case "Regular-Noisy Drags":
                    PathRecorder.finalizePath();
                    Paths.STANDARD_NOISY_DRAGS.put(Utils.to1D(Paths.pointsList.toArray(new int[0][])),Paths.longestDistanceFromOrigin);
                    Paths.pointsList = null;
                    break;
                case "Rapid Drags":
                    PathRecorder.finalizePath();
                    Paths.RAPID_DRAGS.put(Utils.to1D(Paths.pointsList.toArray(new int[0][])),Paths.longestDistanceFromOrigin);
                    Paths.pointsList = null;
                    break;
                case "Fast-Clicks":
                    Clicks.RAPID_CLICKS.put(Utils.to1D(Clicks.drags.toArray(new int[0][])),
                            (int) (System.currentTimeMillis() - ClickRecorder.previousTime));
                    Clicks.drags = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
                case "Regular Clicks":
                    Clicks.REGULAR_CLICKS.put(Utils.to1D(Clicks.drags.toArray(new int[0][])),
                            (int) (System.currentTimeMillis() - ClickRecorder.previousTime));
                    Clicks.drags = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
                case "Slower-Draggy Clicks":
                    Clicks.SLOWER_DRAGGY_CLICKS.put(Utils.to1D(Clicks.drags.toArray(new int[0][])),
                            (int) (System.currentTimeMillis() - ClickRecorder.previousTime));
                    Clicks.drags = null;
                    ClickRecorder.previousDragTime = 0;
                    ClickRecorder.previousTime = 0;
                    break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        onScreen = true;
        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        onScreen = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        location = e.getPoint();
        String key = (String)app.getDataType().getSelectedItem();
        if(app.recording) {
            if(key.toLowerCase().contains("drag"))
                PathRecorder.registerPath(e);
            if(key.toLowerCase().contains("click"))
                ClickRecorder.registerClickDrag(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        location = e.getPoint();
        String key = (String)app.getDataType().getSelectedItem();
        if(app.recording && key.toLowerCase().contains("move"))
            PathRecorder.registerPath(e);
    }


}
