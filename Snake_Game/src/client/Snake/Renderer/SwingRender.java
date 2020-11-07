package client.Snake.Renderer;

import client.Snake.Renderer.Command.NetworkCommand;
import server.Snake.Utility.Utils;

import javax.swing.JFrame;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static Socket clientSocket;
    private static boolean serverConnected = false;

    private static ViewStates currentState = ViewStates.MENU;

    private Dimension prefScreenSize = new Dimension(1000, 1000);
    private SnakePanel gamePanel;
    private MenuPanel menuPanel; // Placeholder

    enum ViewStates {
        MENU, SETTINGS, INGAME, POSTGAME
    }

    public SwingRender() {
        // Creating this client's window
        super(Utils.parseConfig("client", "name"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try { // Connecting to the server
            this.clientSocket = new Socket(
                    Utils.parseConfig("network", "address"),
                    Integer.parseInt(Utils.parseConfig("network", "port")));
            this.serverConnected = true;
        } catch (Exception e) {
            System.out.println("No server to connect to.");
        }

        // TODO: Join game view here
        this.menuPanel = MenuPanel.getInstance();
        this.menuPanel.setPreferredSize(prefScreenSize);
        this.menuPanel.menuButtonMap.get("joinGame").addActionListener(actionEvent -> {
            try {
                NetworkCommand.requestMatchJoin("", new OutputStreamWriter(this.clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.currentState = ViewStates.INGAME;
            this.remove(this.menuPanel);
            this.invalidate();
            this.validate();
            synchronized (this){
                this.notify();
            }
        });
        this.menuPanel.add(menuPanel.menuButtonMap.get("joinGame"));

        // TODO: Host game view here

    }

    @Override
    public void run() {
        while(true) {
            // Switch between views
            switch(currentState) {
                case MENU:
                    this.add(menuPanel);
                    this.pack();
                    this.setVisible(true);
                    break;
                case INGAME:
                    gamePanel = SnakePanel.getInstance(clientSocket);
                    gamePanel.setPreferredSize(prefScreenSize);
                    this.add(gamePanel);
                    gamePanel.requestFocus();
                    this.validate();
                    if(gamePanel.isDisplayable()) { // Is current panel is the game panel
                        gamePanel.run();
                    }
                    break;
                case SETTINGS:
                    break;
                case POSTGAME:
                    break;
                default:
                    break;
            }
            synchronized (this){
                try { this.wait(); } catch (Exception e) { }
            }
        }
    }
}