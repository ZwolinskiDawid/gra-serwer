package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

public class Server {
    public List<Socket> Players;   

    Server(int port, int number, MainController mc) throws IOException {
        
        ServerSocket serverSocket = new ServerSocket(port);
        this.Players = new ArrayList<>();
            
        while (true) {
            if(Players.size() == number)
            {
                synchronized(mc)
                {
                    if(!mc.isReady)
                    {
                        mc.isReady = true;
                        System.out.println(1 + " " + port);
                    }
                    else
                    {
                        mc.gameButton.setDisable(false);
                        System.out.println(2 + " " + port);
                    }
                }
                break;
            }
            
            Socket player = serverSocket.accept();
            this.Players.add(player);
        }
        
        System.out.println("ended");
    }
}