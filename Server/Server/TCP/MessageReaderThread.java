package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

public class MessageReaderThread extends Thread {

    Socket socket;
    Map<String, IResourceManager> resourceManagers;
    BufferedReader inFromServer;

    public MessageReaderThread(Socket socket, Map<String, IResourceManager> resourceManagers) throws IOException {
        System.out.println("Message Reader");
        this.socket = socket;
        this.resourceManagers = resourceManagers;
        this.inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run()
    {
        System.out.println("Running message reader");
        while (true) {
            String res = null;
            try {
                res = inFromServer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Message reader: " + res);
            ResourceStub resourceStub = (ResourceStub) resourceManagers.get(res.split("Thread")[0]);
            System.out.println("Req to resonse: " + resourceStub.requestToResponse);
            resourceStub.requestToResponse.put(res.split(",")[0], res);
        }
    }
}
