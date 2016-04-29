package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class Game {  

    Game(int number, MainController.InformArea area, List<Socket> senders, List<Socket> listeners) throws IOException {
        
        senders.sort(new AdressComparator());
        listeners.sort(new AdressComparator());
        
        List<DataOutputStream> Players = new ArrayList<>();
        
        for(int i=0; i < number; i++)
        {
            DataOutputStream dos = new DataOutputStream (listeners.get(i).getOutputStream());
            Players.add(dos);
        }
        
        this.sendingMap(number, area, Players);
        
        //============STARTING THREADS=======================================
        
        
        
        for(int i=0; i < number; i++)
        {
            Player player = new Player(senders.get(i), Players, i, number);
            Thread t = new Thread(player);
            t.start();
        }
        
        
        area.addToArea("Game has been started");
        area.addToArea("LET'S PLAY!");
        
        
        
    }
    
    private void sendingMap(int number, MainController.InformArea area, List<DataOutputStream> Players) throws FileNotFoundException, IOException
    {
        File map = new File("..\\generator-server\\container.xml");
        FileInputStream fis = new FileInputStream(map);
        
        byte[] buffor = new byte[100];
        byte[] len;
        int dataSize;

        len = ByteBuffer.allocate(4).putInt(fis.available()).array();
        
        for(int i=0; i < number; i++)
        {
            Players.get(i).write(len);
        }
        
        while((dataSize = fis.read(buffor)) != -1)
        {
            for(int i=0; i < number; i++)
            {
                Players.get(i).write(buffor, 0, dataSize);
            }
        }
        
        area.addToArea("Map has sent to all players");
    }
}
