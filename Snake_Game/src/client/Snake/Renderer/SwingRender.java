package client.Snake.Renderer;

import client.Snake.Entities.Player;

import javax.swing.JFrame;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;

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


    // Used to generate a random ID for current client
    private String randomId() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

//        System.out.println(generatedString);
        return generatedString;
    }

    @Override
    public void run() {
        while(true) {

            // TODO: Switch between views logic here

            if(gamePanel.isDisplayable()){ // Is current panel is the game panel

                // TODO: Get info from server here
                gamePanel.updatePlayers();
//                gamePanel.getCurrentPlayer().movePlayer();
//                System.out.println(gamePanel.getLocalCurrentPlayer().toString());

                // TODO: Send info from client to server
                gamePanel.repaint();
            }


            try {Thread.sleep(100);} catch (Exception e) { };
        }

    }
}