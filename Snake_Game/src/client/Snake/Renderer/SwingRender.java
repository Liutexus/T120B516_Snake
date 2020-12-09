package client.Snake.Renderer;

import client.Snake.Interface.IRenderState;
import client.Snake.Renderer.Panels.HostGamePanel;
import client.Snake.Renderer.Panels.MenuPanel;
import client.Snake.Renderer.Panels.SettingsPanel;
import client.Snake.Renderer.Panels.SnakePanel;
import client.Snake.Renderer.RenderState.ClosedRenderState;
import client.Snake.Renderer.RenderState.MenuRenderState;
import server.Snake.Utility.Utils;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static SwingRender instance;

    private static Socket clientSocket;

    private Dimension prefScreenSize = new Dimension(1000, 1000);

    private static IRenderState currentState;
    private SnakePanel gamePanel;
    private MenuPanel menuPanel;
    private SettingsPanel settingsPanel;

    public SnakePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(SnakePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public void setSettingsPanel(SettingsPanel settingsPanel) {
        this.settingsPanel = settingsPanel;
    }

    public HostGamePanel getHostGamePanel() {
        return hostGamePanel;
    }

    public void setHostGamePanel(HostGamePanel hostGamePanel) {
        this.hostGamePanel = hostGamePanel;
    }

    private HostGamePanel hostGamePanel;

    private SwingRender() {
        // Creating this client's window
        super(Utils.parseConfig("client", "name"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try { // Connecting to the server
            clientSocket = new Socket(
                    Utils.parseConfig("network", "address"),
                    Integer.parseInt(Utils.parseConfig("network", "port")));
        } catch (Exception e) {
            System.out.println("No server to connect to.");
        }
        currentState = new MenuRenderState();
        // 'Join game view button' here
        initiateViews();
    }

    public static SwingRender getInstance(){
        if(instance == null)
            instance = new SwingRender();
        return instance;
    }

    public IRenderState getCurrentState(){
        return currentState;
    }

    public void setCurrentState(IRenderState state){
        currentState = state;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public void close(){
        this.setCurrentState(new ClosedRenderState());
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void run() {
        while(currentState.getClass() != ClosedRenderState.class) {
            currentState.run(this);
            synchronized (this){
                try { this.wait(); } catch (Exception ignored) { }
            }
        }
    }

    private void initiateViews(){
        this.menuPanel = MenuPanel.getInstance();
        this.menuPanel.setFrame(this);
        this.menuPanel.setPreferredSize(prefScreenSize);

        this.settingsPanel = SettingsPanel.getInstance();
        this.settingsPanel.setFrame(this);
        this.settingsPanel.setPreferredSize(prefScreenSize);

        this.hostGamePanel = HostGamePanel.getInstance();
        this.hostGamePanel.setFrame(this);
        this.hostGamePanel.setPreferredSize(prefScreenSize);

    }

}
