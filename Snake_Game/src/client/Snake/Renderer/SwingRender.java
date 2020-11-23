package client.Snake.Renderer;

import client.Snake.Renderer.Enumerator.ERendererState;
import server.Snake.Utility.Utils;

import javax.swing.JFrame;
import java.awt.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable {
    private static Socket clientSocket;
    private static boolean serverConnected = false;

    private static ERendererState currentState = ERendererState.MENU;

    private Dimension prefScreenSize = new Dimension(1000, 1000);
    private SnakePanel gamePanel;
    private MenuPanel menuPanel;
    private SettingsPanel settingsPanel;
    private HostGamePanel hostGamePanel;

    public SwingRender() {
        // Creating this client's window
        super(Utils.parseConfig("client", "name"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try { // Connecting to the server
            clientSocket = new Socket(
                    Utils.parseConfig("network", "address"),
                    Integer.parseInt(Utils.parseConfig("network", "port")));
            serverConnected = true;
        } catch (Exception e) {
            System.out.println("No server to connect to.");
        }

        // 'Join game view button' here
        initiateViews();

    }

    public void setCurrentState(ERendererState state){
        currentState = state;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public ERendererState getCurrentState(){
        return currentState;
    }

    public void close(){
        this.setCurrentState(ERendererState.CLOSED);
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void run() {
        while(this.currentState != ERendererState.CLOSED) {
            // Switch between views
            switch(currentState) {
                case TESTING:
                case MENU:
                    this.add(menuPanel.getInstance());
                    this.pack();
                    this.setVisible(true);
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
                    this.add(settingsPanel.getInstance());
                    this.pack();
                    this.setVisible(true);
                    break;
                case HOST_GAME:
                    this.add(hostGamePanel.getInstance());
                    this.pack();
                    this.setVisible(true);
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
