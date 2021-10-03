package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Vector;

public class ResourceStub implements IResourceManager {

    static final int DELAY = 100;

    Socket socket;
    PrintWriter outToServer;
    BufferedReader inFromServer;
    protected String m_name = "";

    public ResourceStub(Socket socket, String p_name) throws IOException {
        this.socket = socket;
        this.outToServer = new PrintWriter(socket.getOutputStream(),true);
        this.inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        m_name = p_name;
    }

    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        outToServer.println(String.format(
                "addFlight,%d,%d,%d,%d",
                id, flightNum, flightSeats, flightPrice));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        outToServer.println(String.format(
                "addCars,%d,%s,%d,%d",
                id, location, numCars, price));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        outToServer.println(String.format(
                "addRooms,%d,%s,%d,%d",
                id, location, numRooms, price));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public int newCustomer(int id) throws RemoteException {
        outToServer.println(String.format("newCustomer,%d",id));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException {
        outToServer.println(String.format("newCustomerCID,%d,%d",id, cid));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        outToServer.println(String.format(
                "deleteFlight,%d,%d",id, flightNum));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException {
        outToServer.println(String.format(
                "deleteCars,%d,%s",
                id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException {
        outToServer.println(String.format(
                "deleteRooms,%d,%s",
                id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        outToServer.println(String.format(
                "deleteCustomer,%d,%d",
                id, customerID));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException {
        outToServer.println(String.format("queryFlight,%d,%d",id, flightNumber));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException {
        outToServer.println(String.format("queryCars,%d,%s",id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException {
        outToServer.println(String.format("queryRooms,%d,%s",id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException {
        outToServer.println(String.format("queryCustomerInfo,%d,%d",id, customerID));
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String res = "";
        String line = null;
        try {
            line = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            if (line.isEmpty()) {
                break;
            }
            res += line + "\n";
            try {
                line = inFromServer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        outToServer.println(String.format("queryFlightPrice,%d,%d", id, flightNumber));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException {
        outToServer.println(String.format("queryCarsPrice,%d,%s", id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException {
        outToServer.println(String.format("queryRoomsPrice,%d,%s", id, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(res);
    }

    @Override
    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {
        outToServer.println(String.format(
                "reserveFlight,%d,%d,%d",
                id, customerID, flightNumber));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
        outToServer.println(String.format(
                "reserveCar,%d,%d,%s",
                id, customerID, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
        outToServer.println(String.format(
                "reserveRoom,%d,%d,%s",
                id, customerID, location));
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {
        String str = "bundle," 
            + id + "," 
            + customerID + "," 
            + flightNumbers.toString().replace(",", "#") + ","
            + location + ","
            + car + ","
            + room;
        System.out.println(str);
        
        outToServer.println(str);
        String res = null;
        try {
            res = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.valueOf(res);
    }

    @Override
    public String queryAnalytics(String location) throws RemoteException {
        outToServer.println(String.format("queryAnalytics,%s",location));
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String res = "";
        String line = null;
        try {
            line = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) {
            if (line.isEmpty()) {
                break;
            }
            res += line + "\n";
            try {
                line = inFromServer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public String getName() throws RemoteException {
        return m_name;
    }
}
