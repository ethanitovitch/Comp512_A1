package Server.TCP;

import Server.Middleware.ResourceMiddleware;

import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class TCPMiddleware extends ResourceMiddleware {
    private static int s_serverPort = 9090;
    private static String s_serverName = "Server";
    private static String s_rmiPrefix = "group_23_";
    private static String[] serverNames = {"Flights", "Cars", "Rooms"};

    public TCPMiddleware(String p_name) {
        super(p_name);
    }

    public static void main(String args[]) {
        // Create the RMI server entry
        try {
            // Create a new Server object
            TCPMiddleware server = new TCPMiddleware(s_serverName);

            // Connect server to resource managers
            for (int i = 0; i < 3; i++) {
                server.connectResourceManager(args[i], serverNames[i]);
            }
            System.out.println("'" + s_serverName + "' resource manager server ready and bound to '" + s_rmiPrefix + s_serverName + "'");

            ServerSocket serverSocket = new ServerSocket(s_serverPort);
            while (true) {
                Socket socket = serverSocket.accept();
                new TCPThread(socket, server.resourceManagers).start();
            }
        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }

        // Create and install a security manager
        if (System.getSecurityManager() == null)
        {
            System.setSecurityManager(new SecurityManager());
        }
    }

    public void connectResourceManager(String machine, String serverName) {
        try {
            boolean first = true;
            while (true) {
                try {
                    Socket socket = new Socket(machine, s_serverPort);
                    ResourceStub m_resourceManager = new ResourceStub(socket);
                    resourceManagers.put(serverName, m_resourceManager);
                    System.out.println("Connected to '" + serverName + "' server [" + machine + ":" + s_serverPort + "/" + s_rmiPrefix + serverName + "]");
                    break;
                }
                catch (RemoteException e) {
                    if (first) {
                        System.out.println("Error connecting to '" + serverName + "' server [" + machine + ":" + s_serverPort + "/" + s_rmiPrefix + serverName + "]");
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
