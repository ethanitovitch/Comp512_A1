package Server.Middleware;

import Server.Interface.IResourceManager;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ResourceMiddleware implements IResourceManager {

    private static final String FLIGHTS = "Flights";
    private static final String CARS = "Cars";
    private static final String ROOMS = "Rooms";

    protected String m_name = "";
    protected Map<String, IResourceManager> resourceManagers = new HashMap<>();

    public ResourceMiddleware(String p_name)
    {
        m_name = p_name;
    }

    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        return resourceManager.addFlight(id, flightNum, flightSeats, flightPrice);
    }

    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        return resourceManager.addCars(id, location, numCars, price);
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        return resourceManager.addRooms(id, location, numRooms, price);
    }

    @Override
    public int newCustomer(int id) throws RemoteException {
        int cid = Integer.parseInt(String.valueOf(id) +
                String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
                String.valueOf(Math.round(Math.random() * 100 + 1)));
        newCustomer(id, cid);
        return cid;
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException {
        boolean flight = resourceManagers.get(FLIGHTS).newCustomer(id, cid);
        boolean car = resourceManagers.get(CARS).newCustomer(id, cid);
        boolean room = resourceManagers.get(ROOMS).newCustomer(id, cid);
        return flight && car && room;
    }

    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        return resourceManager.deleteFlight(id, flightNum);
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        return resourceManager.deleteCars(id, location);
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        return resourceManager.deleteRooms(id, location);
    }

    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        boolean result = true;
        result &= resourceManagers.get(FLIGHTS).deleteCustomer(id, customerID);
        result &= resourceManagers.get(CARS).deleteCustomer(id, customerID);
        result &= resourceManagers.get(ROOMS).deleteCustomer(id, customerID);
        return result;
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        return resourceManager.queryFlight(id, flightNumber);
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        return resourceManager.queryCars(id, location);
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        return resourceManager.queryRooms(id, location);
    }

    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException {
        String flight = resourceManagers.get(FLIGHTS).queryCustomerInfo(id, customerID);
        String car = resourceManagers.get(CARS).queryCustomerInfo(id, customerID);
        String room = resourceManagers.get(ROOMS).queryCustomerInfo(id, customerID);
        return flight + car + room;
    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        return resourceManager.queryFlightPrice(id, flightNumber);
    }

    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        return resourceManager.queryCarsPrice(id, location);
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        return resourceManager.queryRoomsPrice(id, location);
    }

    @Override
    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        return resourceManager.reserveFlight(id, customerID, flightNumber);
    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        return resourceManager.reserveCar(id, customerID, location);
    }

    @Override
    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        return resourceManager.reserveRoom(id, customerID, location);
    }

    @Override
    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {
        boolean result = true;

        for (String flightNumber : flightNumbers) {
            result &= reserveFlight(id, customerID, Integer.parseInt(flightNumber));
        }
        if (car) {
            result &= reserveCar(id, customerID, location);
        }
        if (room) {
            result &= reserveRoom(id, customerID, location);
        }
        return result;
    }

    @Override
    public String queryAnalytics(String location) throws RemoteException {
        String flight = resourceManagers.get(FLIGHTS).queryAnalytics(location);
        String car = resourceManagers.get(CARS).queryAnalytics(location);
        String room = resourceManagers.get(ROOMS).queryAnalytics(location);

        car = car.substring(car.indexOf('\n')+1);
        room = room.substring(room.indexOf('\n')+1);
        return flight + car + room;
    }

    @Override
    public String getName() throws RemoteException {
        return m_name;
    }
}
