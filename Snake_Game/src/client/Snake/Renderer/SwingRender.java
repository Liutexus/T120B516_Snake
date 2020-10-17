package client.Snake.Renderer;

import javax.swing.JFrame;
import java.awt.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static Socket clientSocket;
    private static boolean serverConnected = false;

    private static ViewStates currentState = ViewStates.MENU;

    private Dimension prefScreenSize = new Dimension(1000, 1000);
    private SnakePanel gamePanel;
    private MenuPanel menuPanel; // Placeholder
//    private JoinGamePanel joinGamePanel; // Placeholder
//    private HostGamePanel hostGamePanel; // Placeholder

    enum ViewStates {
        MENU, SETTINGS, INGAME, POSTGAME
    }

    public SwingRender() {
        // Creating this client's window
        super("imma sssessee sneekk hehehe boii");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try { // Connecting to the server
            this.clientSocket = new Socket("localhost", 80);
            this.serverConnected = true;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("No server to connect to.");
//            return;
        }

        // TODO: Join game view here
        this.menuPanel = MenuPanel.getInstance();
        this.menuPanel.setPreferredSize(prefScreenSize);
        this.menuPanel.menuButtonMap.get("joinGame").addActionListener(actionEvent -> {
            this.currentState = ViewStates.INGAME;
            this.remove(this.menuPanel);
            this.invalidate();
            this.validate();
            synchronized (this){
                this.notify();
            }
        });
        this.menuPanel.add(menuPanel.menuButtonMap.get("joinGame"));

        // TODO: Adding game to the window
//        gamePanel = SnakePanel.getInstance(clientSocket);
//        gamePanel.setPreferredSize(prefScreenSize);
//        this.add(gamePanel);

        // TODO: Host game view here

    }

    @Override
    public void run() {
        while(true) {
            // TODO: Switch between views logic here
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
            System.out.println("Hello");
            synchronized (this){
                try { this.wait(); } catch (Exception e) { }
            }
        }
    }
}