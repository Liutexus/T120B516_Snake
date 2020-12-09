package client.Snake.Renderer.RenderState;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.SwingRender;

public class PostGameState implements IRenderState {
    @Override
    public void run(SwingRender swingRender) {
        System.out.println("Post Game View Opened");
    }
}
