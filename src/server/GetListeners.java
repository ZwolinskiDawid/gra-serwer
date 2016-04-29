package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class GetListeners implements Runnable {
    private int port;
    private int number;
    private MainController mc;
    
    GetListeners(int port, int number, MainController mc) {
        this.port = port;
        this.number = number;
        this.mc = mc;
    }

    @Override
    public void run() {
        try {
            this.mc.serverListeners = new Server(this.port, this.number, this.mc);
        } catch (IOException ex) {
            Logger.getLogger(GetListeners.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
