package Client;

import Server.TCP.*;

import java.net.Socket;
import java.rmi.RemoteException;

public class TCPClient extends Client {
    private static String s_serverHost = "localhost";
    private static int s_serverPort = 9090;
    private static String s_serverName = "Server";
    private static String s_rmiPrefix = "group_23_";

    public static void main(String args[])
    {
        if (args.length > 0)
        {
            s_serverHost = args[0];
        }

        if (args.length > 1)
        {
            s_serverName = args[1];
        }
        if (args.length > 2)
        {
            System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUsage: java client.RMIClient [server_hostname [server_rmiobject]]");
            System.exit(1);
        }

        // Set the security policy
        if (System.getSecurityManager() == null)
        {
            System.setSecurityManager(new SecurityManager());
        }

        // Get a reference to the RMIRegister
        try {
            TCPClient client = new TCPClient();
            client.connectServer();
            client.start();
        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public TCPClient()
    {
        super();
    }

    @Override
    public void connectServer() {
        try {
            boolean first = true;
            while (true) {
                try {
                    Socket socket = new Socket(s_serverHost, 9090);
                    m_resourceManager = new ResourceStub(socket, s_serverName);
                    System.out.println("Connected to '" + s_serverName + "' server [" + s_serverHost + ":" + s_serverPort + "/" + s_rmiPrefix + s_serverName + "]");
                    break;
                }
                catch (RemoteException e) {
                    if (first) {
                        System.out.println("Waiting for '" + s_serverName + "' server [" + s_serverHost + ":" + s_serverPort + "/" + s_rmiPrefix + s_serverName + "]");
                        first = false;
                    }
                }
                Thread.sleep(500);
            }
        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
