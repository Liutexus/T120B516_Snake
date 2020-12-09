package client.Snake.Renderer;

import client.Snake.Enumerator.ERendererState;
import client.Snake.Renderer.Panels.HostGamePanel;
import client.Snake.Renderer.Panels.MenuPanel;
import client.Snake.Renderer.Panels.SettingsPanel;
import client.Snake.Renderer.Panels.SnakePanel;
import server.Snake.Utility.Utils;

import javax.swing.JFrame;
import java.awt.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static SwingRender instance;

    private static Socket clientSocket;

    private Dimension prefScreenSize = new Dimension(1000, 1000);

    private static ERendererState currentState = ERendererState.MENU;
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

        // 'Join game view button' here
        initiateViews();
    }

    public static SwingRender getInstance(){
        if(instance == null)
            instance = new SwingRender();
        return instance;
    }

    public ERendererState getCurrentState(){
        return currentState;
    }

    public void setCurrentState(ERendererState state){
        currentState = state;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public void close(){
        this.setCurrentState(ERendererState.CLOSED);
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void run() {
        while(currentState != ERendererState.CLOSED) {
            // Switch between views
            switch(currentState) {
                case TESTING:
                case MENU:
                    this.add(MenuPanel.getInstance());
                    this.pack();
                    this.setVisible(true);
                    menuPanel.repaint();
                    break;
                case IN_GAME:
                    gamePanel = SnakePanel.getInstance(clientSocket);
                    gamePanel.setPreferredSize(prefScreenSize);
                    this.add(gamePanel);
                    gamePanel.requestFocus();
                    this.validate();
                    if(gamePanel.isDisplayable()) { // Is current panel is the game panel
                        gamePanel.run();
                    }
                    break;
                case SETTINGS:
                    this.add(SettingsPanel.getInstance());
                    this.pack();
                    this.setVisible(true);
                    settingsPanel.repaint();
                    break;
                case HOST_GAME:
                    this.add(HostGamePanel.getInstance());
                    this.pack();
                    this.setVisible(true);
                    hostGamePanel.repaint();
                    break;
                case POST_GAME:
                    System.out.println("PostGame View Opened");
                    break;
                default:
                    break;
            }
            synchronized (this){
                try { this.wait(); } catch (Exception e) { }
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
