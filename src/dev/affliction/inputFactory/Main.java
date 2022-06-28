package dev.affliction.inputFactory;

import dev.affliction.inputFactory.app.App;
import dev.affliction.inputFactory.components.keyboard.Keyboard;
import dev.affliction.inputFactory.components.mouse.Mouse;

public class Main {

    private static Mouse mouse;
    private static Keyboard keyboard;

    public static void main(String[] args) {
        App app = new App();
        app.start();
        mouse = new Mouse(app);
        keyboard = new Keyboard(app);
    }

    public static Mouse getMouse() {
        return mouse;
    }

    public static Keyboard getKeyboard() { return keyboard; }
}
