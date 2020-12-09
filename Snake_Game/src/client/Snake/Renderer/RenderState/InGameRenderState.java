package client.Snake.Renderer.RenderState;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.Panels.SnakePanel;
import client.Snake.Renderer.SwingRender;

public class InGameRenderState implements IRenderState {
    @Override
    public void run(SwingRender swingRender) {
        var gamePanel = swingRender.getGamePanel();
        gamePanel = SnakePanel.getInstance(swingRender.getClientSocket());
        gamePanel.setPreferredSize(swingRender.getPreferredSize());
        swingRender.add(gamePanel);
        gamePanel.requestFocus();
        swingRender.validate();
        if(gamePanel.isDisplayable()) {
            gamePanel.run();
        }
    }
}
