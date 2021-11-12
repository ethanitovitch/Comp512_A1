
package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Vector;

import Server.Interface.*;

public class RMIClientPerformance extends Client{
    private static String s_serverHost = "localhost";
	private static int s_serverPort = 2000;
	private static String s_serverName = "Server";
	private static String s_rmiPrefix = "group_23_";

    public static void main(String args[]){
        
        if(args.length == 2){
            //will decide if we are going to run single client or multiple clients 1 = 1 client 2 = multiple clients
            if(args[0].equalsIgnoreCase("1")){
                System.out.println("inside the 1 client scenario");
                s_serverHost = args[1];
                try{
                    RMIClient client = new RMIClient();
                    client.connectServer(s_serverHost,s_serverPort, s_serverName);
                    Command cmdOne = Command.fromString("AddCustomer");
                    Command cmdTwo = Command.fromString("AddFlight");
                    Command cmdThree = Command.fromString("QueryFlight");
                    Command cmdFour = Command.fromString("ReserveFlight");
                    Command cmdFive = Command.fromString("Commit");
                    String [] customer = {"1"};
                    String [] flight = {"1", "1", "100" , "1000"};
                    String [] query_flight = {"1", "1"};
                    String [] reserve_flight = {"1", "1", "1"};
                    String [] commit  = {"1"};

                    Vector<String> cust_vec = new Vector<String>(Arrays.asList(customer));
                    Vector<String> add_flight_vec = new Vector<String>(Arrays.asList(flight));
                    Vector<String> query_flight_vec = new Vector<String>(Arrays.asList(query_flight));
                    Vector<String> reserve_vec = new Vector<String>(Arrays.asList(reserve_flight));
                    Vector<String> commit_vec = new Vector<String>(Arrays.asList(commit));

                    //start timer
                    long startTime = System.currentTimeMillis();

                    client.execute(cmdOne, add_flight_vec);
                    client.execute(cmdTwo, query_flight_vec);
                    client.execute(cmdThree, reserve_vec);
                    client.execute(cmdFour, commit_vec);

                     //stop timer
                     long endTime = System.currentTimeMillis();


                    System.out.println(startTime - endTime);

                }	catch (Exception e) {    
                    System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUncaught exception");
                    e.printStackTrace();
                    System.exit(1);
                }

            }
            if(args[0].equalsIgnoreCase("2")){
                //do same as above just create threads per x amount of clients 
            }
        }
    }

    public RMIClientPerformance(){
        super(); 
    }

  
    public void connectServer() {
        connectServer(s_serverHost, s_serverPort, s_serverName);
        
    }
    public void connectServer(String server, int port, String name)
	{
		try {
			boolean first = true;
			while (true) {
				try {
					Registry registry = LocateRegistry.getRegistry(server, port);
					m_resourceManager = (IResourceManager)registry.lookup(s_rmiPrefix + name);
					System.out.println("Connected to '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
					break;
				}
				catch (NotBoundException|RemoteException e) {
					if (first) {
						System.out.println("Waiting for '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
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