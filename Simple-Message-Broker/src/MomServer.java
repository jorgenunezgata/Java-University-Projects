import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MomServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor MOM iniciado en el puerto 5000...");

        ChannelManager channelManager = new ChannelManager();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new MomServerThread(clientSocket, channelManager)).start();
        }
    