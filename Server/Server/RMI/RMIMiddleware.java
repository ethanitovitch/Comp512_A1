package Server.RMI;

import Server.Interface.*;
import Server.Middleware.ResourceMiddleware;

import java.rmi.NotBoundException;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIMiddleware extends ResourceMiddleware {
	private static int s_serverPort = 2000;
  	private static String s_serverName = "Server";
  	private static String s_rmiPrefix = "group_23_";
  	private static String[] serverNames = {"Flights", "Cars", "Rooms"};

  	public static void main(String args[]) {
		// Create the RMI server entry
		try {
			// Create a new Server object
			RMIMiddleware server = new RMIMiddleware(s_serverName);

			// Connect server to resource managers
			for (int i = 0; i < 3; i++) {
				server.connectResourceManager(args[i], serverNames[i]);
			}

			// Dynamically generate the stub (client proxy)
			IResourceManager resourceManager = (IResourceManager)UnicastRemoteObject.exportObject(server, 0);

			// Bind the remote object's stub in the registry
			Registry l_registry;
			try {
				l_registry = LocateRegistry.createRegistry(s_serverPort);
			} catch (RemoteException e) {
				l_registry = LocateRegistry.getRegistry(s_serverPort);
			}
			final Registry registry = l_registry;
			registry.rebind(s_rmiPrefix + s_serverName, resourceManager);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						registry.unbind(s_rmiPrefix + s_serverName);
						System.out.println("'" + s_serverName + "' resource manager unbound");
					}
					catch(Exception e) {
						System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
						e.printStackTrace();
					}
				}
			});
			System.out.println("'" + s_serverName + "' resource manager server ready and bound to '" + s_rmiPrefix + s_serverName + "'");
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

	public RMIMiddleware(String name)
	{
		super(name);
	}

	public void connectResourceManager(String machine, String serverName) {
		try {
			boolean first = true;
			while (true) {
				try {
					Registry registry = LocateRegistry.getRegistry(machine, s_serverPort);
					IResourceManager m_resourceManager = (IResourceManager)registry.lookup(s_rmiPrefix + serverName);
					resourceManagers.put(serverName, m_resourceManager);
					System.out.println("Connected to '" + serverName + "' server [" + machine + ":" + s_serverPort + "/" + s_rmiPrefix + serverName + "]");
					break;
				}
				catch (NotBoundException|RemoteException e) {
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
