package client.Snake.Renderer;

import client.Snake.Entities.Player;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;


public class SwingRender extends JFrame implements Runnable {

    SnakePanel gamePanel;
    // Menu panel goes here
    // Join game panel goes here

    public SwingRender() {
        // Creating this client's window
        super("imma ssssssseee sneek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1000, 1000);
//        setResizable(false);

        // Creating this client's ID
        Player myPlayer = new Player(randomId());


        // Join game view here

        // Adding game to the window
        gamePanel = new SnakePanel(myPlayer);
        gamePanel.setPreferredSize(new Dimension(1000, 1000));
//            gamePanel.addPlayer(myPlayer);
        add(gamePanel);

        // Host game view here

        pack();
        setVisible(true);

        run();
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
                // Game logic goes here
                // TODO: Get info from server here


                gamePanel.getCurrentPlayer().movePlayer();





                gamePanel.repaint();
            }





            try {Thread.sleep(100);} catch (Exception e) { };
        }

    }
}