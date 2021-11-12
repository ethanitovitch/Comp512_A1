
package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Vector;

import Server.Interface.*;


abstract class ClientTest extends Client implements Runnable{
    public int clients = 1;
    public double throughput = 1.0;
    public int startTime = 0;
    public int [] all_times = new int [80];
    public ClientTest(){
        super();
    }
    public void run(){
        int waitTime = (int)((clients) / throughput);
        long variation = 50;

        while (System.currentTimeMillis() < startTime){}

        for (int i = (int)Thread.currentThread().getId(); i < (int)Thread.currentThread().getId(); i++) {
            double l = Math.random();
            int var;
            if (l < 0.5)
                var = waitTime - ((int)(variation*Math.random()));
            else
                var = waitTime + ((int)(variation*Math.random()));
            try {
                int rt = multipleResourceManagerTransaction();
                if (i >= (int)Thread.currentThread().getId()){
                    all_times[i - ((int)Thread.currentThread().getId())] = rt;
                }

                if ((int)(var - rt) < 0){
                    continue;
                }

                Thread.sleep((int) (var - 10));
            } catch(Exception e){
                System.out.println(e);
            }
        }

    }

    private int multipleResourceManagerTransaction() throws Exception{
        long startTime = System.currentTimeMillis();


        int x = m_resourceManager.start();
        int xid = x;
        m_resourceManager.reserveFlight(xid, xid, xid);
        m_resourceManager.reserveCar(xid,xid,"Montreal" );
        m_resourceManager.reserveRoom(xid, xid,"Montreal" );
        m_resourceManager.commit(xid);
        int responseTime = (int) (System.currentTimeMillis() - startTime);
        return responseTime;
    }

    public void createEnvironment() {

        try {
            int x = m_resourceManager.start();

            int xid = x;
            for (int i = 1; i <= 100; i++) {
                m_resourceManager.addFlight(xid, xid, 10000, 500 + i*2);
                m_resourceManager.addCars(xid, "Montreal" + i, 50, 10 + i*2);
                m_resourceManager.addRooms(xid, "Montreal" + i, 50, 10 + i*2);
            }
            for (int i = 1; i <= 100; i++) {
                m_resourceManager.newCustomer(xid, i);
            }
            m_resourceManager.commit(xid);
        } catch(Exception e){
            System.out.println(e);
        }
    }

}


public class RMIClientPerformance extends ClientTest{
    private static String s_serverHost = "localhost";
	private static int s_serverPort = 2000;
	private static String s_serverName = "Server";
	private static String s_rmiPrefix = "group_23_";

    public static void main(String args[]) {

        if (args.length == 2) {
            //will decide if we are going to run single client or multiple clients 1 = 1 client 2 = multiple clients
            if (args[0].equalsIgnoreCase("1")) {
                System.out.println("inside the 1 client scenario");
                s_serverHost = args[1];
                try {
                    RMIClient client = new RMIClient();
                    client.connectServer(s_serverHost, s_serverPort, s_serverName);
                    Command cmdOne = Command.fromString("AddCustomer");
                    Command cmdTwo = Command.fromString("AddFlight");
                    Command cmdThree = Command.fromString("QueryFlight");
                    Command cmdFour = Command.fromString("ReserveFlight");
                    Command cmdFive = Command.fromString("Commit");
                    String[] customer = {"1"};
                    String[] flight = {"1", "1", "100", "1000"};
                    String[] query_flight = {"1", "1"};
                    String[] reserve_flight = {"1", "1", "1"};
                    String[] commit = {"1"};

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

                } catch (Exception e) {
                    System.err.println((char) 27 + "[31;1mClient exception: " + (char) 27 + "[0mUncaught exception");
                    e.printStackTrace();
                    System.exit(1);
                }

            }
            if (args[0].equalsIgnoreCase("2")) {
                s_serverHost = args[1];

                try {
                    RMIClientPerformance[] c = new RMIClientPerformance[100];
                    Thread[] thread = new Thread[100];
                    for (int i = 0; i < 100; i++) {
                        c[i] = new RMIClientPerformance();
                        c[i].clients = 100;
                        c[i].throughput = 0.3;
                        c[i].startTime = (int) System.currentTimeMillis();
                        c[i].connectServer();
                        if (i == 0) {
                            c[i].createEnvironment();
                        }
                        thread[i] = new Thread(c[i]);
                        thread[i].start();
                    }
                    for (int i = 0; i < 100; i++) {
                        thread[i].join();
                    }

                    for (int i = 0; i < 100; i++) {
                        for (int j = 0; j < c[i].times.length; j++) {
                            System.out.print(c[i].times[j]);
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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