package client.Snake.Renderer.RenderState;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.Panels.MenuPanel;
import client.Snake.Renderer.SwingRender;

public class MenuRenderState implements IRenderState {
    @Override
    public void run(SwingRender swingRender) {
        swingRender.add(MenuPanel.getInstance());
        swingRender.pack();
        swingRender.setVisible(true);
        swingRender.getMenuPanel().repaint();
    }
}
