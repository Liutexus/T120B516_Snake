package client.Snake.Renderer;

import client.Snake.Renderer.Command.NetworkCommand;
import client.Snake.Renderer.Components.MenuButton;
import client.Snake.Renderer.Enumerator.ERendererState;
import client.Snake.Renderer.Interface.IMediator;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class MenuPanel extends JPanel implements Runnable, IMediator {
    private static SwingRender render = null;
    private static MenuPanel panelInstance = null;

    private static MenuButton joinGameButton;

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
        this.add(joinGameButton);
    }

    @Override
    public void run() {

    }

    @Override
    public void notify(Object sender) {

        if(sender == this.joinGameButton){
            try {
                if(this.render.getClientSocket() != null){
                    NetworkCommand.requestMatchJoin("", new OutputStreamWriter(this.render.getClientSocket().getOutputStream()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.render.setCurrentState(ERendererState.INGAME);
            this.render.remove(this);
            this.invalidate();
            this.validate();
            synchronized (this.render){
                this.render.notify();
            }
        }


    }
}
