package client.Snake.Renderer;

import client.Snake.Renderer.Components.MenuButton;
import client.Snake.Renderer.Enumerator.ERendererState;
import client.Snake.Renderer.Interface.IMediator;

import javax.swing.*;

public class HostGamePanel extends JPanel implements Runnable, IMediator {
    private static SwingRender render = null;
    private static HostGamePanel panelInstance = null;

    private static MenuButton backButton;

    private HostGamePanel() {
        setUpControls();
    }

    public static HostGamePanel getInstance() {
        if (panelInstance == null)
            panelInstance = new HostGamePanel();
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
        if (this.backButton.equals(sender)) {
            this.render.setCurrentState(ERendererState.MENU);
            this.render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (this.render) {
                this.render.notify();
            }
        }
    }

    @Override
    public void run() {

    }
}
