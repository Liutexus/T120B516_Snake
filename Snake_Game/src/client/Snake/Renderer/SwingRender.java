package client.Snake.Renderer;

import javax.swing.JFrame;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static Socket clientSocket;

    private SnakePanel gamePanel;
//    private MenuPanel menuPanel; // Placeholder
//    private JoinGamePanel joinGamePanel; // Placeholder
//    private HostGamePanel hostGamePanel; // Placeholder

    public SwingRender() {
        // Creating this client's window
        super("imma sssessee sneekk hehehe boii");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);

        // TODO: Join game view here


        // TODO: Adding game to the window
        try {
            clientSocket = new Socket("localhost", 80);
            gamePanel = new SnakePanel(clientSocket);
            gamePanel.setPreferredSize(new Dimension(1000, 1000));
            add(gamePanel);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("No server to connect to.");
            return;
        }

        // TODO: Host game view here

        pack();
        setVisible(true);

        run();
    }


    private void switchViews(){
        // TODO: Add a menu view to switch to other views

    }

    @Override
    public void run() {
        // TODO: Switch between views logic here
        if(gamePanel.isDisplayable()) { // Is current panel is the game panel
            gamePanel.run();
        }
    }
}