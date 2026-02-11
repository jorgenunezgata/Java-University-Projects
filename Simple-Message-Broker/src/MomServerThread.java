import java.io.*;
import java.net.*;

public class MomServerThread implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ChannelManager manager;

    public MomServerThread(Socket socket, ChannelManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ", 3);
                String cmd = parts[0];

                switch (cmd) {
                    case "OPEN":
                        out.println("OK");
                        break;
                    case "CLOSE":
                        socket.close();
                        return;
                    case "MKCHAN":
                        manager.mkChannel(parts[1]);
                        break;
                    case "RMCHAN":
                        manager.rmChannel(parts[1]);
                        break;
                    case "WRITE":
                        String[] split = parts[2].split(" ", 2);
                        manager.writeChannel(split[0], split[1]);
                        break;
                    case "READ":
                        String[] args = parts[2].split(" ");
                        String msg = manager.readChannel(args[0], Boolean.parseBoolean(args[1]));
                        out.println(msg != null ? msg : "");
                        break;
                    default:
                        out.println("ERROR");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
