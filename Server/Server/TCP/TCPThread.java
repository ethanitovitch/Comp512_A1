package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPThread extends Thread {
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
                System.out.println("message:"+message);
                String[] params =  message.split(",");

                IResourceManager resourceManager = resourceManagers.get("Flights");
                Boolean result = resourceManager.addFlight(
                        Integer.parseInt(params[1]),
                        Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4]));
                outToClient.println(result);
            }
            socket.close();
        }
        catch (IOException e) {}
    }
}
