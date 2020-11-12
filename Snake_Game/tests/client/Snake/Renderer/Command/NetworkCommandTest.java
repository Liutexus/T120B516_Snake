package client.Snake.Renderer.Command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Snake.Utility.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class NetworkCommandTest {
    private ExecutorService pool;
    private OutputStreamWriter out;
    private InputStreamReader in;

    @BeforeEach
    void setUp() {
        pool = Executors.newFixedThreadPool(3);
    }

    @Test
    void requestLogin() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_LOGIN\":\"{}\"}", inb.readLine());
                assertEquals("{\"CLIENT_LOGOUT\":{\"id\":\"test\"}}", inb.readLine());
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
        NetworkCommand.requestLogin("test", this.out);
        NetworkCommand.undo("test", this.out);
    }

    @Test
    void requestLogout() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_LOGOUT\":{\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_LOGIN\":\"{}\"}", inb.readLine());
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
        NetworkCommand.requestLogout("test", this.out);
        NetworkCommand.undo("test", this.out);
    }

    @Test
    void requestMatchJoin() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_REQUEST_MATCH_JOIN\":\"{}\"}", inb.readLine());
                assertEquals("{\"CLIENT_REQUEST_MATCH_LEAVE\":{\"id\":\"test\"}}", inb.readLine());
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
        NetworkCommand.requestMatchJoin("test", this.out);
        NetworkCommand.undo("test", this.out);
    }

    @Test
    void requestMatchLeave() {
        final Socket[] clientSocket = {new Socket()};

        pool.execute(() -> {
            try (var listener = new ServerSocket(Integer.parseInt(Utils.parseConfig("network", "port")))) {
                Socket testSocket = listener.accept();
                in = new InputStreamReader(testSocket.getInputStream());
                BufferedReader inb = new BufferedReader(in);
                synchronized (this) {
                    this.notify();
                }
                assertEquals("{\"CLIENT_REQUEST_MATCH_LEAVE\":{\"id\":\"test\"}}", inb.readLine());
                assertEquals("{\"CLIENT_REQUEST_MATCH_JOIN\":\"{}\"}", inb.readLine());
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
        NetworkCommand.requestMatchLeave("test", this.out);
        NetworkCommand.undo("test", this.out);
    }
}
