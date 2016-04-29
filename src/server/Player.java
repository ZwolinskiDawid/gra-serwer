package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class Player implements Runnable {
    private List<DataOutputStream> Players;
    private Socket player;
    private int index;
    private int number;

    Player(Socket player, List<DataOutputStream> Players, int index, int number) {
        this.player = player;
        this.Players = Players;
        this.index = index;
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("Thread: Player " + this.index + " has started");
        
        byte[] buffor;
        byte[] len;
        
        //===================SENDING INDEX==============================
                
        buffor = (this.index + "").getBytes(StandardCharsets.UTF_8);
        len = ByteBuffer.allocate(4).putInt(buffor.length).array();
        try {
            this.Players.get(index).write(len);
            this.Players.get(index).write(buffor);
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //===================SENDING NUMBER==============================
        
        buffor = (this.number + "").getBytes(StandardCharsets.UTF_8);
        len = ByteBuffer.allocate(4).putInt(buffor.length).array();
        try {
            this.Players.get(index).write(len);
            this.Players.get(index).write(buffor);
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //======================LISTEN==================================
        
        try {
            this.player.setSoTimeout(4000);
        } catch (SocketException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(this.player.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        buffor = new byte[16];
        //byte[] idx = (this.index + "").getBytes(StandardCharsets.UTF_8);
        
        while(true)
        {
            try {
                dis.read(buffor);

                //System.out.println(action[0] + " " + action[1] + " " + action[2]);
                //this.Players.get(this.index).write(buffor, 0, 16);                

                for(int i = 0 ; i < this.number; i++)
                {
                    if(i != this.index)
                    {
                        synchronized(this.Players.get(i))
                        {
                            this.Players.get(i).write(buffor, 0, 16);
                        }
                    }
                }        
                        
                
            } 
            catch (SocketException e) {
                System.out.println("disconect");
                break;
            }
            catch (SocketTimeoutException e) {
                
                //System.out.println("timeout");
                
                /*
                
                try {
                    this.Players.get(index).close();
                } catch (IOException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(this.player.isClosed())
                {
                    System.out.println("socket is closed");
                }
                
                */
                
            }
            catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("is out of while");
    }    
}
