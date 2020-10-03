// Handler.java class is responsible of keeping a connection with a client,
// receiving inputs and processing them accordingly.
package server.Snake;

import client.Snake.Entities.Player;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Handler implements Runnable {
    private Socket serverSocket;
    public static GameLogic gameLogic;
    private OutputStream out;
    private InputStream in;

    private Player clientPlayer;
    private String clientId;
    public static Map<String, Player> players = new ConcurrentHashMap<>();

    Handler(Socket serverSocket, GameLogic gameLogic, Map players) {

        this.serverSocket = serverSocket; // Current socket object
        this.gameLogic = gameLogic;
        this.players = players;

        try {
            // We return data from server to the client through here
            out = serverSocket.getOutputStream();
            // We listen to our client here
            in = serverSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected: " + serverSocket);

        Listener clientListener = new Listener(new InputStreamReader(in));
        Sender clientSender = new Sender(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            clientSender.sendClientLogin();
            executor.execute(clientListener);
            while (true) {
                long start = System.currentTimeMillis(); // Benchmarking
                executor.execute(clientSender); // Sending packets to the client
//                System.out.println("Milliseconds passed: " + (System.currentTimeMillis() - start));
                try {Thread.sleep(100);} catch (Exception e) { };
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error:" + serverSocket);
        }
    }

    public void updatePlayersMap(Map<String, Player> players) {
        this.players = players;
    }

    // Some utilities
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

        return generatedString;
    }

    private Player createPlayer(String id) {
        if(players.containsKey(id)) {
            System.out.println("Player already exists.");
            return null;
        }
        int randX = ThreadLocalRandom.current().nextInt(5, 45);
        int randY = ThreadLocalRandom.current().nextInt(5, 45);
        // This could be improved by some more fancier initial position assignment
        Player player = new Player(id, randX, randY);

        gameLogic.addPlayer(player);
//        players.put(id, player); // Adding new client user to the players' pool
        return player;
    }

    // --- Client listener class ---
    private class Listener implements Runnable {
        InputStreamReader in;

        public Listener(InputStreamReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            BufferedReader inb = new BufferedReader(in);
            while(true) {
                try {
//                    System.out.println(inb.readLine());
                    gameLogic.updatePlayerField(inb.readLine());
                } catch (Exception e) {
//                    System.out.println("Couldn't receive packet from the client.");
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    // --- Client sender class ---
    private class Sender implements Runnable {
        private OutputStreamWriter out;

        public Sender(OutputStreamWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
//                long start = System.currentTimeMillis(); // Benchmarking

                synchronized (players){
                    players.forEach((key, value) -> {
                        sendPacket(value.toString());
                    });
                }
//                System.out.println("Milliseconds passed: " + (System.currentTimeMillis() - start));
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println("Error sending a packet to the client.");
            }
        }

        public void sendClientLogin() {
            clientId = randomId();
            sendPacket(clientId);

            clientPlayer = createPlayer(clientId);
            sendPacket(clientPlayer.toString());
        }

        private void sendPacket(String packet) {
            BufferedWriter bfw = new BufferedWriter(out);
            try {
                if(packet.length() < 8) packet = String.format("%" + -8 + "s", packet); // Making the packet big enough
                if(!packet.endsWith("\n")) packet += "\n";

//                System.out.print(packet);
                bfw.write(packet);
                bfw.flush();
            } catch (IOException e) {
                System.out.println("Error sending packet to client.");
                e.printStackTrace();
            }
        }

    }
}
