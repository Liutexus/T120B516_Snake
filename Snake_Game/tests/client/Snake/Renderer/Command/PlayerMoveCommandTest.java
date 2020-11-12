package client.Snake.Renderer.Command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Utility.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMoveCommandTest {
    private ExecutorService pool;
    private OutputStreamWriter out;
    private InputStreamReader in;


    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
    }

    @Test
    void moveUp() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"-1\",\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"1\",\"id\":\"test\"}}", inb.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        synchronized (this){
            try {
                this.wait();
                this.out = new OutputStreamWriter(clientSocket[0].getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerMoveCommand.moveUp("test", this.out);
        PlayerMoveCommand.undo("test", this.out);
    }

    @Test
    void moveDown() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"1\",\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"-1\",\"id\":\"test\"}}", inb.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        synchronized (this){
            try {
                this.wait();
                this.out = new OutputStreamWriter(clientSocket[0].getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerMoveCommand.moveDown("test", this.out);
        PlayerMoveCommand.undo("test", this.out);
    }

    @Test
    void moveRight() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"1\",\"directionY\":\"0\",\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"-1\",\"directionY\":\"0\",\"id\":\"test\"}}", inb.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        synchronized (this){
            try {
                this.wait();
                this.out = new OutputStreamWriter(clientSocket[0].getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerMoveCommand.moveRight("test", this.out);
        PlayerMoveCommand.undo("test", this.out);
    }

    @Test
    void moveLeft() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"-1\",\"directionY\":\"0\",\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"1\",\"directionY\":\"0\",\"id\":\"test\"}}", inb.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        synchronized (this){
            try {
                this.wait();
                this.out = new OutputStreamWriter(clientSocket[0].getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerMoveCommand.moveLeft("test", this.out);
        PlayerMoveCommand.undo("test", this.out);
    }

    @Test
    void moveStop() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"0\",\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_RESPONSE\":{\"directionX\":\"0\",\"directionY\":\"-1\",\"id\":\"test\"}}", inb.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pool.execute(() -> {
            try { // Connecting to the server
                clientSocket[0] = new Socket(
                        Utils.parseConfig("network", "address"),
                        Integer.parseInt(Utils.parseConfig("network", "port")));
            } catch (Exception e) {
                System.out.println("No server to connect to.");
            }
        });

        synchronized (this){
            try {
                this.wait();
                this.out = new OutputStreamWriter(clientSocket[0].getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PlayerMoveCommand.moveStop("test", this.out);
        PlayerMoveCommand.undo("test", this.out);
    }
}