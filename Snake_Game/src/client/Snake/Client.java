package client.Snake;
import client.Snake.Renderer.SwingRender;
import client.Snake.Utilities.BitmapConverter;

public class Client {

    private static SwingRender render;

    void Start() {

        SwingRender render = new SwingRender();

//        BitmapConverter.BMPToTerrain("Snake_Game/img/seaside_road.bmp");
    }

    public static void main(String[] args) {
        Client game = new Client();

        game.Start();
    }
}
