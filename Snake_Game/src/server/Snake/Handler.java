package server.Snake;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Handler implements Runnable {
    private Socket socket;
    private int count = 0;


    Handler(Socket socket, int count) {
        this.socket = socket;
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println("Connected: " + socket);

        try {

            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);

            // TODO:
            //synchronized(sync_object){} // Synchronize online players' array
            String aaa = "<body>" +
                    "heyhey" +
                    "you are " + count + " visitor!" +
                    "</body>";
            char[] chars = aaa.toCharArray();
            out.write(chars);

            count++;
            while (in.hasNextLine()) { // Main server loop
//                System.out.println(in.nextLine().toUpperCase());
                out.println(in.nextLine().toUpperCase());
            }
            socket.close();

        } catch (Exception e) {
            System.out.println("Error:" + socket);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            System.out.println("Closed: " + socket);
        }

    }

}
