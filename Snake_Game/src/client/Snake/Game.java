package client.Snake;
import client.Snake.Renderer.SwingRender;

public class Game {
    private SwingRender render;

    void Start() {
        SwingRender render = new SwingRender();

    }

    public static void main(String[] args) {
        Game game = new Game();

        game.Start();
    }
}
