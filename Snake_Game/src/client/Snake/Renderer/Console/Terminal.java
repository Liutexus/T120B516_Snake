package client.Snake.Renderer.Console;

import client.Snake.Command.CustomPacketCommand;
import client.Snake.Renderer.SwingRender;
import server.Snake.Enumerator.EPacketHeader;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public class Terminal extends JPanel {
    private static JTextArea output;
    private static JTextField input;

    public Terminal(){
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBackground(new Color(25, 25, 25, 50));
        setupOutput();
        setupInput();
    }

    protected void paintComponent(Graphics g){
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private void setupOutput(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Setting up Output field preferences
        output = new JTextArea();
        output.setEditable(false);
        output.setWrapStyleWord(true);
        output.setOpaque(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                output.append(String.valueOf((char) b));
            }
        }));

        output.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                input.requestFocus(); // On click -> Focus the input field
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                return;
            }
        });

        // Setting up Scroll Pane preferences holding the Output text area
        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        DefaultCaret caret = (DefaultCaret) output.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(scrollPane, gbc);
    }

    private void setupInput(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;

        // Setting up Input field preferences
        input = new JTextField();
        input.setBorder(null);
        input.getDocument().putProperty("filterNewlines", Boolean.TRUE);

        input.addKeyListener(new KeyListener() {
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

        add(input, gbc);
    }

    private void keyResponse(KeyEvent key) {
        switch (key.getKeyCode()){
            case KeyEvent.VK_ENTER:
                if(input.getText().trim().length() == 0) return;
                output.append(input.getText() + "\n");

                // Send input.getText() -> Server's to interpreter
                try {
                    OutputStreamWriter out = new OutputStreamWriter(SwingRender.getInstance().getClientSocket().getOutputStream());
                    CustomPacketCommand.customPacket(EPacketHeader.COMMAND, input.getText(), out);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                input.setText("");
                break;
            case 192: // To control console's visibility. Set key - " ` ";
                GameConsole.toggleVisibility();
                setFocusable(false);
                break;
        }
    }

    @Override
    public void requestFocus(){
        input.requestFocus();
    }

}
