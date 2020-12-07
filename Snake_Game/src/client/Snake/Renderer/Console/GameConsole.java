package client.Snake.Renderer.Console;

import client.Snake.Renderer.SwingRender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class GameConsole extends JPanel implements Runnable {
    private static GameConsole consoleInstance = null;
    private static Terminal terminal = null;
    private static boolean visible = false;

    private GameConsole() {
        setLayout(new BorderLayout());
        terminal = new Terminal();
        add(terminal);
        setOpaque(false);
        setVisible(visible);
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyResponse(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == '`')
                    e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Be useless for now
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public static GameConsole getInstance(){
        if (consoleInstance == null){
            consoleInstance = new GameConsole();
        }
        return consoleInstance;
    }

    public static void setupConsole(JPanel panel){
        GameConsole console = GameConsole.getInstance();
        consoleInstance.setPreferredSize(new Dimension(SwingRender.getInstance().getWidth()-20, SwingRender.getInstance().getHeight()-50)); // Set this component's size
        panel.add(console); // Add this component to parent
    }

    public static void setVisibility(boolean bool){
        visible = bool;
        consoleInstance.setVisible(visible);
    }

    public static void toggleVisibility(){
        setVisibility(!visible);
        if(visible) consoleInstance.requestFocus();
    }

    @Override
    public void requestFocus(){
        setFocusable(false);
        terminal.requestFocus();
    }

    private void keyResponse(KeyEvent key) {
        switch (key.getKeyCode()){
            case 192: // To control console's visibility. Set key - " ` ";
                GameConsole.toggleVisibility();
                setFocusable(false);
            break;
        }
    }

    @Override
    public void run() {
        while(true){
//            consoleInstance.setPreferredSize(new Dimension(200, 200));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
