package client.Snake.Renderer.RenderState;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.Panels.SettingsPanel;
import client.Snake.Renderer.SwingRender;

public class SettingsRenderState implements IRenderState {
    @Override
    public void run(SwingRender swingRender) {
        var settingsPanel = SettingsPanel.getInstance();
        swingRender.add(settingsPanel);
        swingRender.pack();
        swingRender.setVisible(true);
        settingsPanel.repaint();
    }
}
