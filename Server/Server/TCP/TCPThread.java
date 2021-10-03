package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Arrays;

public class TCPThread extends Thread {
    
    private static final String FLIGHTS = "Flights";
    private static final String CARS = "Cars";
    private static final String ROOMS = "Rooms";

    public Socket socket;
    public BufferedReader inFromClient;
    public PrintWriter outToClient;
    public String message = null;
    public Map<String, IResourceManager> resourceManagers = new HashMap<>();

    public TCPThread (Socket socket, Map<String, IResourceManager> resourceManagers) throws IOException {
        this.socket = socket;
        this.resourceManagers = resourceManagers;
        this.inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outToClient = new PrintWriter(socket.getOutputStream(), true);
    }

    public TCPThread (Socket socket, Map<String, IResourceManager> resourceManagers, String message) throws IOException {
        this.socket = socket;
        this.message = message;
        this.resourceManagers = resourceManagers;
        this.outToClient = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run()
    {
        try
        {
            if (message != null) {
                callMethod(message);
            } else {
                while ((message = inFromClient.readLine())!=null)
                {
                    callMethod(message);
                }
                socket.close();
            }
        }
        catch (IOException e) {}
    }

    public void callMethod(String message) throws RemoteException {
        String[] params =  message.split(",");
        System.out.println("Command: " + params[0]);
        switch (params[0]) {
            case "addFlight": {
                System.out.println("params: " + params);
                IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                Boolean result = resourceManager.addFlight(
                        Integer.parseInt(params[1]),
                        Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4]));
                outToClient.println(params[5]+","+result);
                break;
            }
            case "addCars": {
                IResourceManager resourceManager = resourceManagers.get(CARS);
                Boolean result = resourceManager.addCars(
                        Integer.parseInt(params[1]),
                        params[2],
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4]));
                outToClient.println(result);
                break;
            }
            case "addRooms": {
                IResourceManager resourceManager = resourceManagers.get(ROOMS);
                Boolean result = resourceManager.addRooms(
                        Integer.parseInt(params[1]),
                        params[2],
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4]));
                outToClient.println(result);
                break;
            }
            case "newCustomer": {
                int cid = Integer.parseInt(params[1]
                        + String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND))
                        + String.valueOf(Math.round(Math.random() * 100 + 1)));

                resourceManagers.get(FLIGHTS).newCustomer(Integer.parseInt(params[1]), cid);
                resourceManagers.get(CARS).newCustomer(Integer.parseInt(params[1]), cid);
                resourceManagers.get(ROOMS).newCustomer(Integer.parseInt(params[1]), cid);

                outToClient.println(cid);
                break;
            }
            case "newCustomerCID": {
                boolean flight = resourceManagers.get(FLIGHTS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                boolean car = resourceManagers.get(CARS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                boolean room = resourceManagers.get(ROOMS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));

                outToClient.println(flight && car && room);
                break;
            }
            case "deleteFlight": {
                IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                boolean result = resourceManager.deleteFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                outToClient.println(result);
                break;
            }
            case "deleteCars": {
                IResourceManager resourceManager = resourceManagers.get(CARS);
                boolean result = resourceManager.deleteCars(Integer.parseInt(params[1]), params[2]);
                outToClient.println(result);
                break;
            }
            case "deleteRooms": {
                IResourceManager resourceManager = resourceManagers.get(ROOMS);
                boolean result = resourceManager.deleteRooms(Integer.parseInt(params[1]), params[2]);
                outToClient.println(result);
                break;
            }
            case "deleteCustomer": {
                boolean flight = resourceManagers.get(FLIGHTS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                boolean car = resourceManagers.get(CARS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                boolean room = resourceManagers.get(ROOMS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));

                outToClient.println(flight && car && room);
                break;
            }
            case "queryFlight": {
                IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                System.out.println("get result..");
                Integer result = resourceManager.queryFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                System.out.println("result: " + result);
                result = (result != null) ? result : 1;
                String res = params[3] + "," + result;
                outToClient.println(res);
                break;
            }
            case "queryCars": {
                IResourceManager resourceManager = resourceManagers.get(CARS);
                Integer result = resourceManager.queryCars(Integer.parseInt(params[1]), params[2]);
                outToClient.println((result != null) ? result : 1);
                break;
            }
            case "queryRooms": {
                IResourceManager resourceManager = resourceManagers.get(ROOMS);
                Integer result = resourceManager.queryRooms(Integer.parseInt(params[1]), params[2]);
                outToClient.println((result != null) ? result : 1);
                break;
            }
            case "queryCustomerInfo": {
                String flight = resourceManagers.get(FLIGHTS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                String car = resourceManagers.get(CARS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                String room = resourceManagers.get(ROOMS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));

                car = car.substring(car.indexOf('\n')+1);
                room = room.substring(room.indexOf('\n')+1);
                outToClient.println(flight + car + room);
                break;
            }
            case "queryFlightPrice": {
                IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                Integer result = resourceManager.queryFlightPrice(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                outToClient.println((result != null) ? result : 1);
                break;
            }
            case "queryCarsPrice": {
                IResourceManager resourceManager = resourceManagers.get(CARS);
                Integer result = resourceManager.queryCarsPrice(Integer.parseInt(params[1]), params[2]);
                outToClient.println((result != null) ? result : 1);
                break;
            }
            case "queryRoomsPrice": {
                IResourceManager resourceManager = resourceManagers.get(ROOMS);
                Integer result = resourceManager.queryRoomsPrice(Integer.parseInt(params[1]), params[2]);
                outToClient.println((result != null) ? result : 1);
                break;
            }
            case "reserveFlight": {
                IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                boolean result = resourceManager.reserveFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                outToClient.println(result);
                break;
            }
            case "reserveCar": {
                IResourceManager resourceManager = resourceManagers.get(CARS);
                boolean result = resourceManager.reserveCar(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[3]);
                outToClient.println(result);
                break;
            }
            case "reserveRoom": {
                IResourceManager resourceManager = resourceManagers.get(ROOMS);
                boolean result = resourceManager.reserveRoom(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[3]);
                outToClient.println(result);
                break;
            }
            case "bundle": {
                boolean result = true;
                System.out.println("Bundling");
                String flightsString = params[3];
                System.out.println("flights string: " + flightsString);
                flightsString = flightsString.replace("[", "");
                flightsString = flightsString.replace("]", "");
                String[] flights = flightsString.split("# ");
                Vector<String> flightNumbers = new Vector<String>(Arrays.asList(flights));

                for (String flightNumber : flightNumbers) {
                    IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                    result &= resourceManager.reserveFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(flightNumber));
                }
                System.out.println("location: " + params[4]);
                System.out.println("cars: " + params[5]);
                if (Boolean.valueOf(params[5])) {
                    IResourceManager resourceManager = resourceManagers.get(CARS);
                    result &= resourceManager.reserveCar(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[4]);
                }
                System.out.println("rooms: " + params[6]);
                if (Boolean.valueOf(params[6])) {
                    IResourceManager resourceManager = resourceManagers.get(ROOMS);
                    result &= resourceManager.reserveRoom(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[4]);
                }
                outToClient.println(result);
                break;
            }
            case "queryAnalytics": {
                System.out.println("Querying analytics for: " + params[1]);
                String flight = resourceManagers.get(FLIGHTS).queryAnalytics(params[1]);
                String car = resourceManagers.get(CARS).queryAnalytics(params[1]);
                String room = resourceManagers.get(ROOMS).queryAnalytics(params[1]);
                outToClient.println(flight + car + room);
                break;
            }
        }
    }
}
