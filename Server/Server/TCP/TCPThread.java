package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    public Map<String, IResourceManager> resourceManagers = new HashMap<>();

    public TCPThread (Socket socket, Map<String, IResourceManager> resourceManagers) {
        this.socket=socket;
        this.resourceManagers = resourceManagers;
    }

    public void run()
    {
        try
        {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(socket.getOutputStream(), true);
            String message = null;
            while ((message = inFromClient.readLine())!=null)
            {
                String[] params =  message.split(",");
                switch (params[0]) {
                    case "addFlight": {
                        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                        Boolean result = resourceManager.addFlight(
                                Integer.parseInt(params[1]),
                                Integer.parseInt(params[2]),
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]));
                        outToClient.println(result);

                    }
                    case "addCars": {
                        IResourceManager resourceManager = resourceManagers.get(CARS);
                        Boolean result = resourceManager.addCars(
                                Integer.parseInt(params[1]),
                                params[2],
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]));
                        outToClient.println(result);
                    }
                    case "addRooms": {
                        IResourceManager resourceManager = resourceManagers.get(ROOMS);
                        Boolean result = resourceManager.addCars(
                                Integer.parseInt(params[1]),
                                params[2],
                                Integer.parseInt(params[3]),
                                Integer.parseInt(params[4]));
                        outToClient.println(result);
                    }
                    case "newCustomer": {
                        int cid = Integer.parseInt(params[1]
                            + String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND))
                            + String.valueOf(Math.round(Math.random() * 100 + 1)));
                        
                        resourceManagers.get(FLIGHTS).newCustomer(Integer.parseInt(params[1]), cid);
                        resourceManagers.get(CARS).newCustomer(Integer.parseInt(params[1]), cid);
                        resourceManagers.get(ROOMS).newCustomer(Integer.parseInt(params[1]), cid);
                        
                        outToClient.println(cid);
                    }
                    case "newCustomerCID": {
                        boolean flight = resourceManagers.get(FLIGHTS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        boolean car = resourceManagers.get(CARS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        boolean room = resourceManagers.get(ROOMS).newCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        
                        outToClient.println(flight && car && room);
                    }
                    case "deleteFlight": {
                        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                        boolean result = resourceManager.deleteFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        outToClient.println(result);
                    }
                    case "deleteCars": {
                        IResourceManager resourceManager = resourceManagers.get(CARS);
                        boolean result = resourceManager.deleteCars(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "deleteRooms": {
                        IResourceManager resourceManager = resourceManagers.get(ROOMS);
                        boolean result = resourceManager.deleteRooms(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "deleteCustomer": {
                        boolean flight = resourceManagers.get(FLIGHTS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        boolean car = resourceManagers.get(CARS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        boolean room = resourceManagers.get(ROOMS).deleteCustomer(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        
                        outToClient.println(flight && car && room);
                    }
                    case "queryFlight": {
                        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                        int result = resourceManager.queryFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        outToClient.println(result);
                    }
                    case "queryCars": {
                        IResourceManager resourceManager = resourceManagers.get(CARS);
                        int result = resourceManager.queryCars(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "queryRooms": {
                        IResourceManager resourceManager = resourceManagers.get(ROOMS);
                        int result = resourceManager.queryRooms(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "queryCustomerInfo": {
                        String flight = resourceManagers.get(FLIGHTS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        String car = resourceManagers.get(CARS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        String room = resourceManagers.get(ROOMS).queryCustomerInfo(Integer.parseInt(params[1]), Integer.parseInt(params[2]));

                        car = car.substring(car.indexOf('\n')+1);
                        room = room.substring(room.indexOf('\n')+1);
                        outToClient.println(flight + car + room);
                    }
                    case "queryFlightPrice": {
                        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                        int result = resourceManager.queryFlightPrice(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        outToClient.println(result);
                    }
                    case "queryCarsPrice": {
                        IResourceManager resourceManager = resourceManagers.get(CARS);
                        int result = resourceManager.queryCarsPrice(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "queryRoomsPrice": {
                        IResourceManager resourceManager = resourceManagers.get(ROOMS);
                        int result = resourceManager.queryRoomsPrice(Integer.parseInt(params[1]), params[2]);
                        outToClient.println(result);
                    }
                    case "reserveFlight": {
                        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                        boolean result = resourceManager.reserveFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                        outToClient.println(result);
                    }
                    case "reserveCar": {
                        IResourceManager resourceManager = resourceManagers.get(CARS);
                        boolean result = resourceManager.reserveCar(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[3]);
                        outToClient.println(result);
                    }
                    case "reserveRoom": {
                        IResourceManager resourceManager = resourceManagers.get(ROOMS);
                        boolean result = resourceManager.reserveRoom(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[3]);
                        outToClient.println(result);
                    }
                    case "bundle": {
                        boolean result = true;

                        String flightsString = params[3];
                        flightsString = flightsString.replace("[", "");
                        flightsString = flightsString.replace("]", "");
                        String[] flights = flightsString.split(", ");
                        Vector<String> flightNumbers = new Vector<String>(Arrays.asList(flights));

                        for (String flightNumber : flightNumbers) {
                            IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
                            result &= resourceManager.reserveFlight(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(flightNumber));
                        }
                        if (Boolean.valueOf(params[5])) {
                            IResourceManager resourceManager = resourceManagers.get(CARS);
                            result &= resourceManager.reserveCar(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[4]);
                        }
                        if (Boolean.valueOf(params[6])) {
                            IResourceManager resourceManager = resourceManagers.get(ROOMS);
                            result &= resourceManager.reserveRoom(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[4]);
                        }
                        outToClient.println(result);
                    }
                }                
            }
            socket.close();
        }
        catch (IOException e) {}
    }
}
