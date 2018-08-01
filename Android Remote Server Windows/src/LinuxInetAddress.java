import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

public class LinuxInetAddress {

    public static InetAddress getLocalHost() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        if(!localHost.isLoopbackAddress()) return localHost;
        InetAddress[] addrs = getAllLocalUsingNetworkInterface();
        for(int i=0; i<addrs.length; i++) {
            //Check for "." to ensure IPv4
            if(!addrs[i].isLoopbackAddress() && addrs[i].getHostAddress().contains(".")) return addrs[i];
        }
        return localHost;
    }

    public static InetAddress[] getAllLocal() throws UnknownHostException {
        InetAddress[] iAddresses = InetAddress.getAllByName("127.0.0.1");
        if(iAddresses.length != 1) return iAddresses;
        if(!iAddresses[0].isLoopbackAddress()) return iAddresses;
        return getAllLocalUsingNetworkInterface();
    }

    private static InetAddress[] getAllLocalUsingNetworkInterface() throws UnknownHostException {
        ArrayList addresses = new ArrayList();
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            throw new UnknownHostException("127.0.0.1");
        }
        while(e.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface)e.nextElement();
            for(Enumeration e2 = ni.getInetAddresses(); e2.hasMoreElements();) {
                addresses.add(e2.nextElement());
            }
        }
        InetAddress[] iAddresses = new InetAddress[addresses.size()];
        for(int i=0; i<iAddresses.length; i++) {
            iAddresses[i] = (InetAddress)addresses.get(i);
        }
        return iAddresses;
    }
}