package Server.TCP;

import Server.Interface.IResourceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReaderThread extends Thread {

    Socket socket;
    ResourceStub resourceStub;
    BufferedReader inFromServer;

    public MessageReaderThread(Socket socket, ResourceStub resourceStub) throws IOException {
        this.socket = socket;
        this.resourceStub = resourceStub;
        this.inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run()
    {
        while (true) {
            String res = null;
            try {
                res = inFromServer.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Message reader: " + res);
            resourceStub.requestToResponse.put(res.split(",")[0], res);
        }
    }
}
