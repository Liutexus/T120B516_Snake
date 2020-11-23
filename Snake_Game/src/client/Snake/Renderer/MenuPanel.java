package client.Snake.Renderer;

import client.Snake.Renderer.Command.NetworkCommand;
import client.Snake.Renderer.Components.MenuButton;
import client.Snake.Renderer.Enumerator.ERendererState;
import client.Snake.Renderer.Interface.IMediator;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MenuPanel extends JPanel implements Runnable, IMediator {
    private static SwingRender render = null;
    private static MenuPanel panelInstance = null;

    private static MenuButton joinGameButton;
    private static MenuButton settingsButton;
    private static MenuButton hostGameButton;

    private MenuPanel() {
        setUpControls();
    }

    public static MenuPanel getInstance() {
        if (panelInstance == null)
            panelInstance = new MenuPanel();
        return panelInstance;
    }

    public void setFrame(SwingRender render){
        this.render = render;
    }

    private void setUpControls(){
        joinGameButton = new MenuButton("Join Online Match", this);
        settingsButton = new MenuButton("View Settings", this);
        hostGameButton = new MenuButton("Host Multiplayer game", this);
        this.add(joinGameButton);
        this.add(settingsButton);
        this.add(hostGameButton);
    }

    @Override
    public void run() {

    }

    @Override
    public void notify(Object sender) {
        // TODO: Add handling of more buttons


        if (this.joinGameButton.equals(sender)) {
            try {
                if (this.render.getClientSocket() != null) {
                    NetworkCommand.requestMatchJoin("", new OutputStreamWriter(this.render.getClientSocket().getOutputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.render.setCurrentState(ERendererState.IN_GAME);
            this.render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (this.render) {
                this.render.notify();
            }
        } else if (this.settingsButton.equals(sender)) {
            this.render.setCurrentState(ERendererState.SETTINGS);
            this.render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (this.render) {
                this.render.notify();
            }
        } else if (this.hostGameButton.equals(sender)) {
            this.render.setCurrentState(ERendererState.HOST_GAME);
            this.render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (this.render) {
                this.render.notify();
            }
        }


    }
}
