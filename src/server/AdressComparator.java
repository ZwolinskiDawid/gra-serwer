package server;

import java.net.Socket;
import java.util.Comparator;

class AdressComparator implements Comparator<Socket> {

    public AdressComparator() {
    }

    @Override
    public int compare(Socket o1, Socket o2) {
        String adress1 = o1.getRemoteSocketAddress().toString();
        String adress2 = o2.getRemoteSocketAddress().toString();
        return adress1.compareTo(adress2);
    }
    
}
