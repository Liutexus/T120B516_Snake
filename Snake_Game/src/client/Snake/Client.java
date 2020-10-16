package client.Snake;
import client.Snake.Renderer.SwingRender;

public class Client {

    private static SwingRender render;

    void Start() {
        SwingRender render = new SwingRender();
    }

    public static void main(String[] args) {
        Client game = new Client();
        game.Start();
    }
}
