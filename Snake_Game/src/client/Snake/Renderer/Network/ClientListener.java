package client.Snake.Renderer.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientListener implements Runnable{
    private InputStreamReader in;
    private ClientUpdater updater;

    public ClientListener(InputStreamReader in, ClientUpdater updater) {
        this.in = in;
        this.updater = updater;
    }

    @Override
    public void run() {
        BufferedReader inb = new BufferedReader(in);
        while(true) {
            try {
                updater.addPacket(inb.readLine());
                if(updater.sleeping)
                    synchronized (updater) {
                        updater.notify();
                    }
            } catch (Exception e) {
                System.out.println("Couldn't receive players from the server.");
//                    e.printStackTrace();
                try {Thread.sleep(100);} catch (Exception ex) { };
                continue;
            }
        }
    }
}
