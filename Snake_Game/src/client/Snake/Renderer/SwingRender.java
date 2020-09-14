package client.Snake.Renderer;

import client.Snake.Entities.Player;

import javax.swing.JFrame;
import java.nio.charset.Charset;
import java.util.Random;


public class SwingRender extends JFrame {
    public int directionX = 0;
    public int directionY = 0;

    public SwingRender() {
        // Creating this client's window
        super("imma ssssssseee sneek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setResizable(false);

        // Creating this client's ID
        Player myPlayer = new Player(randomId());

        // TODO: Switch between views logic here

            // Join game logic here

            // Adding game to the window
            SnakePanel gamePanel = new SnakePanel(myPlayer);
//            gamePanel.addPlayer(player);
            add(gamePanel);

            // Host game logic here

        setVisible(true);
    }

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

}