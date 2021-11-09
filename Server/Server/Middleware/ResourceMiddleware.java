package Server.Middleware;

import Server.Interface.IResourceManager;
import Server.Interface.InvalidTransactionException;
import Server.Interface.TransactionAbortedException;
import Server.Transaction.TransactionManager;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static Server.LockManager.TransactionLockObject.LockType.LOCK_READ;
import static Server.LockManager.TransactionLockObject.LockType.LOCK_WRITE;

public class ResourceMiddleware implements IResourceManager {

    private static final String FLIGHTS = "Flights";
    private static final String CARS = "Cars";
    private static final String ROOMS = "Rooms";

    protected String m_name = "";
    protected Map<String, IResourceManager> resourceManagers = new HashMap<>();

    private TransactionManager transactionManager;

    public ResourceMiddleware(String p_name)
    {
        m_name = p_name;
        transactionManager = new TransactionManager();
    }

    @Override
    public int start() throws RemoteException {
        int xid = transactionManager.start();
        resourceManagers.get(FLIGHTS).start(xid);
        resourceManagers.get(CARS).start(xid);
        resourceManagers.get(ROOMS).start(xid);
        return xid;
    }

    @Override
    public void start(int xid) throws RemoteException {

    }

    @Override
    public boolean commit(int xid) throws RemoteException, InvalidTransactionException {
        return transactionManager.commit(xid);
    }

    @Override
    public boolean abort(int xid) throws RemoteException, InvalidTransactionException {
        return transactionManager.abort(xid);
    }

    @Override
    public boolean shutdown() throws RemoteException {
        return false;
    }

    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        boolean hasLock = transactionManager.getLock(id, FLIGHTS + flightNum, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.addFlight(id, flightNum, flightSeats, flightPrice);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        boolean hasLock = transactionManager.getLock(id, CARS + location, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.addCars(id, location, numCars, price);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        boolean hasLock = transactionManager.getLock(id, ROOMS + location, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.addRooms(id, location, numRooms, price);
        } else {
            transactionManager.abort(id);
//            throw new TransactionAbortedException();
            return false;
        }
    }

    @Override
    public int newCustomer(int id) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        int cid = Integer.parseInt(String.valueOf(id) +
                String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
                String.valueOf(Math.round(Math.random() * 100 + 1)));
        newCustomer(id, cid);
        return cid;
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean flightLock = transactionManager.getLock(id, FLIGHTS + cid, resourceManagers.get(FLIGHTS), LOCK_WRITE);
        boolean carLock = transactionManager.getLock(id, CARS + cid, resourceManagers.get(CARS), LOCK_WRITE);
        boolean roomLock = transactionManager.getLock(id, ROOMS + cid, resourceManagers.get(ROOMS), LOCK_WRITE);
        if (flightLock && carLock && roomLock) {
            boolean flight = resourceManagers.get(FLIGHTS).newCustomer(id, cid);
            boolean car = resourceManagers.get(CARS).newCustomer(id, cid);
            boolean room = resourceManagers.get(ROOMS).newCustomer(id, cid);
            return flight && car && room;
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        boolean hasLock = transactionManager.getLock(id, FLIGHTS + flightNum, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.deleteFlight(id, flightNum);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        boolean hasLock = transactionManager.getLock(id, CARS + location, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.deleteCars(id, location);

        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        boolean hasLock = transactionManager.getLock(id, ROOMS + location, resourceManager, LOCK_WRITE);
        if (hasLock) {
            return resourceManager.deleteRooms(id, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean flightLock = transactionManager.getLock(id, FLIGHTS + customerID, resourceManagers.get(FLIGHTS), LOCK_WRITE);
        boolean carLock = transactionManager.getLock(id, CARS + customerID, resourceManagers.get(CARS), LOCK_WRITE);
        boolean roomLock = transactionManager.getLock(id, ROOMS + customerID, resourceManagers.get(ROOMS), LOCK_WRITE);
        if (flightLock && carLock && roomLock) {
            boolean result = true;
            result &= resourceManagers.get(FLIGHTS).deleteCustomer(id, customerID);
            result &= resourceManagers.get(CARS).deleteCustomer(id, customerID);
            result &= resourceManagers.get(ROOMS).deleteCustomer(id, customerID);
            return result;
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        boolean hasLock = transactionManager.getLock(id, FLIGHTS + flightNumber, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryFlight(id, flightNumber);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        boolean hasLock = transactionManager.getLock(id, CARS + location, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryCars(id, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        boolean hasLock = transactionManager.getLock(id, ROOMS + location, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryRooms(id, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean flightLock = transactionManager.getLock(id, FLIGHTS + customerID, resourceManagers.get(FLIGHTS), LOCK_READ);
        boolean carLock = transactionManager.getLock(id, CARS + customerID, resourceManagers.get(CARS), LOCK_READ);
        boolean roomLock = transactionManager.getLock(id, ROOMS + customerID, resourceManagers.get(ROOMS), LOCK_READ);
        if (flightLock && carLock && roomLock) {
            String flight = resourceManagers.get(FLIGHTS).queryCustomerInfo(id, customerID);
            String car = resourceManagers.get(CARS).queryCustomerInfo(id, customerID);
            String room = resourceManagers.get(ROOMS).queryCustomerInfo(id, customerID);
            return flight + car + room;
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
        boolean hasLock = transactionManager.getLock(id, FLIGHTS + flightNumber, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryFlightPrice(id, flightNumber);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(CARS);
        boolean hasLock = transactionManager.getLock(id, CARS + location, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryCarsPrice(id, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        IResourceManager resourceManager = resourceManagers.get(ROOMS);
        boolean hasLock = transactionManager.getLock(id, ROOMS + location, resourceManager, LOCK_READ);
        if (hasLock) {
            return resourceManager.queryRoomsPrice(id, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean customerLock = transactionManager.getLock(id, FLIGHTS + customerID, resourceManagers.get(FLIGHTS), LOCK_WRITE);
        boolean flightLock = transactionManager.getLock(id, FLIGHTS + flightNumber, resourceManagers.get(FLIGHTS), LOCK_WRITE);
        if (customerLock && flightLock) {
            IResourceManager resourceManager = resourceManagers.get(FLIGHTS);
            return resourceManager.reserveFlight(id, customerID, flightNumber);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean customerLock = transactionManager.getLock(id, CARS + customerID, resourceManagers.get(CARS), LOCK_WRITE);
        boolean carLock = transactionManager.getLock(id, CARS + location, resourceManagers.get(CARS), LOCK_WRITE);
        if (customerLock && carLock) {
            IResourceManager resourceManager = resourceManagers.get(CARS);
            return resourceManager.reserveCar(id, customerID, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean customerLock = transactionManager.getLock(id, ROOMS + customerID, resourceManagers.get(ROOMS), LOCK_WRITE);
        boolean carLock = transactionManager.getLock(id, ROOMS + location, resourceManagers.get(ROOMS), LOCK_WRITE);
        if (customerLock && carLock) {
            IResourceManager resourceManager = resourceManagers.get(ROOMS);
            return resourceManager.reserveRoom(id, customerID, location);
        } else {
            transactionManager.abort(id);
            throw new TransactionAbortedException();
        }
    }

    @Override
    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException, InvalidTransactionException, TransactionAbortedException {
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
