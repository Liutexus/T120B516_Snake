package client.Snake.Renderer;

import client.Snake.Renderer.Enumerator.ERendererState;
import client.Snake.Renderer.Interface.IMediator;
import server.Snake.Utility.Utils;

import javax.swing.JFrame;
import java.awt.*;
import java.net.Socket;

public class SwingRender extends JFrame implements Runnable, IMediator {
    private static Socket clientSocket;
    private static boolean serverConnected = false;

    private static ERendererState currentState = ERendererState.MENU;

    private Dimension prefScreenSize = new Dimension(1000, 1000);
    private SnakePanel gamePanel;
    private MenuPanel menuPanel;

    public SwingRender() {
        // Creating this client's window
        super(Utils.parseConfig("client", "name"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try { // Connecting to the server
            this.clientSocket = new Socket(
                    Utils.parseConfig("network", "address"),
                    Integer.parseInt(Utils.parseConfig("network", "port")));
            this.serverConnected = true;
        } catch (Exception e) {
            System.out.println("No server to connect to.");
        }

        // 'Join game view button' here
        initiateMenuView();

        // TODO: Host game view here

    }

    public void setCurrentState(ERendererState state){
        this.currentState = state;
    }

    public Socket getClientSocket(){
        return this.clientSocket;
    }

    public ERendererState getCurrentState(){
        return this.currentState;
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
                    this.add(menuPanel);
                    this.pack();
                    this.setVisible(true);
                    break;
                case INGAME:
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
                    break;
                case POSTGAME:
                    break;
                default:
                    break;
            }
            synchronized (this){
                try { this.wait(); } catch (Exception e) { }
            }
        }
    }

    private void initiateMenuView(){
        this.menuPanel = MenuPanel.getInstance();
        this.menuPanel.setFrame(this);
        this.menuPanel.setPreferredSize(prefScreenSize);
    }

    @Override
    public void notify(Object sender) {

    }
}
