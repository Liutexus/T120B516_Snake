package client.Snake;
import client.Snake.Renderer.SwingRender;

public class Client {
    private static SwingRender render = SwingRender.getInstance();

    void Start() {
        render.run();
    }

    public static void main(String[] args) {
        Client game = new Client();
        game.Start();
    }
}
