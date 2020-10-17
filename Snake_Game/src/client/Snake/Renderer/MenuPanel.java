package client.Snake.Renderer;

import client.Snake.Renderer.Components.MenuButton;

import javax.swing.*;
import java.util.HashMap;

public class MenuPanel extends JPanel implements Runnable {
    private static MenuPanel panelInstance = null;

    public static HashMap<String, MenuButton> menuButtonMap = new HashMap<>();

    private MenuPanel() {
        MenuButton joinGameButton = new MenuButton("Join Online Match");
        menuButtonMap.put("joinGame", joinGameButton);
    }

    public static MenuPanel getInstance() {
        if (panelInstance == null)
            panelInstance = new MenuPanel();
        return panelInstance;
    }

    @Override
    public void run() {

    }
}
