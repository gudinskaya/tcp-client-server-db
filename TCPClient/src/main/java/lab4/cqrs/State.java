package lab4.cqrs;

import lab4.cqrs.datatypes.Result;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.lang.Integer;

public enum State {
    INSTANCE;

    private String host;
    private int    port;

    private InetSocketAddress currentSocketAddr;
    private Socket    currentSocket;

    private Object connSync = new Object();

    private List<Result> results = new ArrayList<Result>();

    public String FormatCurrentConnAddr() {
        return host + ":" + Integer.toString(port);
    }

    public void SetConnectionAddress(String connAddr) throws URISyntaxException, IllegalArgumentException, IOException, SocketException {
        if (connAddr.length() == 0) {
            throw new IllegalArgumentException("Connection address could not be empty.");
        }
        URI uri;
        try {
            uri = new URI("dummy://" + connAddr);
        } catch(Throwable e) {
            throw new URISyntaxException(connAddr, "Illegal connection address: ");
        }
        String newHost = uri.getHost();
        Boolean hostIllegal = newHost == null ||
            newHost == "0.0.0.0" || // you can't connect to the "any address".
            newHost == "255.255.255.255"; // broadcast addresses also unavailable.

        if (hostIllegal || uri.getPort() < 1) {
            throw new URISyntaxException(
                connAddr,
                "URI must have both host and port parts, divided by colon. E.g. 127.0.0.1:5533"
            );
        }
        host = newHost;
        port = uri.getPort();

        Socket sock;
        InetSocketAddress newSockAddr = new InetSocketAddress(host, port);
        sock = new Socket();
        try {
            sock.connect(newSockAddr);
        } catch(IOException e) {
            throw e; // just rethrow. handled above.
        }

        synchronized(connSync) {
            if (currentSocket != null && !currentSocket.isClosed()) {
                try {
                    currentSocket.close();
                } catch(IOException e) {
                    // ignore
                }
            }

            currentSocketAddr = newSockAddr;
            currentSocket = sock;
        }
    }

    public Socket GetCurrentSocket() {
        Socket sock;
        synchronized(connSync) {
            sock = currentSocket;
        }
        return sock;
    }

    public InetSocketAddress GetCurrentSocketAddr() {
        InetSocketAddress x;
        synchronized (connSync) {
            x = currentSocketAddr;
        }
        return x;
    }

    public synchronized void AddResult(Result e) {
        if (e != null) {
            results.add(e);
        }
    }

    public List<Result> GetResults() {
        List<Result> l = new ArrayList<Result>();
        l.addAll(results);
        return l;
    }
    // public void Reconnection() throws IOException {
    //         if(currentSocket.isClosed()) {
    //             try {
    //                 currentSocket.connect(currentSocketAddr);
    //             } catch(IOException e) {
    //                 throw e; // just rethrow. handled above.
    //             }
    //         }
    // }
}
