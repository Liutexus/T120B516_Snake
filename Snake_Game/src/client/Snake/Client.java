package client.Snake;
import client.Snake.Renderer.SwingRender;
import client.Snake.Utilities.BitmapConverter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static SwingRender render;

    void Start() {

        SwingRender render = new SwingRender();

//        BitmapConverter.BMPToTerrain("Snake_Game/img/seaside_road.bmp");


//            System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
//            var scanner = new Scanner(System.in);
//            var in = new Scanner(socket.getInputStream());
//            var out = new PrintWriter(socket.getOutputStream(), true);
//            while (scanner.hasNextLine()) {
//                out.println(scanner.nextLine());
//                System.out.println(in.nextLine());
//            }

    }

    public static void main(String[] args) {
        Client game = new Client();

        game.Start();
    }
}
