package testing;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Test extends Thread implements KeyListener {
    private boolean isRunning = true;
    private Canvas canvas;
    private BufferStrategy strategy;
    private BufferedImage background;
    private Graphics2D backgroundGraphics;
    private Graphics2D graphics;
    private JFrame frame;
    private int width = 1000;
    private int height = 1000;
    private int scale = 1;
    private GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();


    public static void main(String[] args) {
        Test test = new Test();

        System.out.println("exited");
    }

    // create a hardware accelerated image
    public final BufferedImage create(final int width, final int height, final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    public Test(){
        JFrame frame = new JFrame("imma ssssseeee snek");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(width * scale, height * scale);
        frame.setVisible(true);
        frame.setResizable(false);

        // Canvas
        canvas = new Canvas(config);
        canvas.setSize(width * scale, height * scale);
        frame.add(canvas, 0);

        // Background & Buffer
        background = create(width, height, false);
        canvas.createBufferStrategy(2);
        do {
            strategy = canvas.getBufferStrategy();
        } while (strategy == null);
        graphics = (Graphics2D) canvas.getGraphics();
        graphics.drawLine(0,0, 100, 100);
        graphics.fillRect(100, 100, 500, 500);
        canvas.paint(graphics);

    }





    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public void run() {

    }
}
