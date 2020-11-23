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

        if (joinGameButton.equals(sender)) {
            try {
                if (render.getClientSocket() != null) { // Is the client connected to the server
                    // Ask the server for login info if the connection has been established
                    NetworkCommand.requestMatchJoin("", new OutputStreamWriter(render.getClientSocket().getOutputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeRenderState(ERendererState.IN_GAME);
        } else if (settingsButton.equals(sender)) {
            changeRenderState(ERendererState.SETTINGS);
        } else if (hostGameButton.equals(sender)) {
            changeRenderState(ERendererState.HOST_GAME);
        }
    }

    private void changeRenderState(ERendererState state){
        render.remove(this);
        this.invalidate();
        this.validate();
        synchronized (render) {
            render.setCurrentState(state);
            render.repaint();
            render.notify();
        }
    }
}
