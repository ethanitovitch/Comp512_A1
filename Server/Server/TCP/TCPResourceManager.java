package Server.TCP;

import Server.Common.ResourceManager;
import Server.Interface.IResourceManager;
import Server.RMI.RMIResourceManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class TCPResourceManager extends ResourceManager {
    private static int s_serverPort = 9090;
    private static String s_serverName = "Server";
    private static String s_rmiPrefix = "group_23_";

    public TCPResourceManager(String p_name) {
        super(p_name);
    }

    public static void main(String args[]) {
        if (args.length > 0)
        {
            s_serverName = args[0];
        }

        try {
            System.out.println("'" + s_serverName + "' resource manager server ready and bound to '" + s_rmiPrefix + s_serverName + "'");

            ServerSocket serverSocket = new ServerSocket(s_serverPort);
            Map<String, IResourceManager> resourceManagers = new HashMap<>();
            resourceManagers.put("Flights", new ResourceManager("Flights"));
            resourceManagers.put("Cars", new ResourceManager("Cars"));
            resourceManagers.put("Rooms", new ResourceManager("Rooms"));
            while (true) {
                Socket socket = serverSocket.accept();
                new TCPThread(socket, resourceManagers).start();
            }
        }
        catch (Exception e) {
            System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
