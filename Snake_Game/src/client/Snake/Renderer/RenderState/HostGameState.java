package client.Snake.Renderer.RenderState;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.Panels.HostGamePanel;
import client.Snake.Renderer.SwingRender;

public class HostGameState implements IRenderState {
    @Override
    public void run(SwingRender swingRender) {
        swingRender.add(HostGamePanel.getInstance());
        swingRender.pack();
        swingRender.setVisible(true);
        swingRender.getHostGamePanel().repaint();
    }
}
