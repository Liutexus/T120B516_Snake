package client.Snake.Renderer;

import client.Snake.Renderer.Components.MenuButton;
import client.Snake.Renderer.Enumerator.ERendererState;
import client.Snake.Renderer.Interface.IMediator;

import javax.swing.*;

public class SettingsPanel extends JPanel implements Runnable, IMediator {
    private static SwingRender render = null;
    private static SettingsPanel panelInstance = null;

    private static MenuButton backButton;

    private SettingsPanel() {
        setUpControls();
    }

    public static SettingsPanel getInstance() {
        if (panelInstance == null)
            panelInstance = new SettingsPanel();
        return panelInstance;
    }

    public void setFrame(SwingRender render){
        this.render = render;
    }

    private void setUpControls(){
        backButton = new MenuButton("Back", this);
        this.add(backButton);
    }

    @Override
    public void notify(Object sender) {
        if (backButton.equals(sender)) {
            render.setCurrentState(ERendererState.MENU);
            render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (render) {
                render.notify();
            }
        }
    }

    @Override
    public void run() {

    }
}
