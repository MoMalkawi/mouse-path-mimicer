package dev.affliction.inputFactory.components.keyboard;

import dev.affliction.inputFactory.app.App;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    /*
    When holding typable character = Spams Pressed -> Typed , then released when released
    When holding untypable character (Ex: left key) = Spams Pressed , then released when released
     */

    public static long lastTyped = 0;
    public static long lastPressed = 0;
    public static long lastReleased = 0;

    private static App app;
    private static Component canvas;

    public Keyboard(App app) {
        Keyboard.app = app;
        Keyboard.canvas = app.getRg();
        canvas.addKeyListener(this);
    }

    //TODO: DO THIS ONCE YOU HAVE STUDIED HOW KEY EVENTS OCCUR IN OSRS


    @Override
    public void keyTyped(KeyEvent e) {
        lastTyped = System.currentTimeMillis();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        lastPressed = System.currentTimeMillis();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        lastReleased = System.currentTimeMillis();
    }

}
