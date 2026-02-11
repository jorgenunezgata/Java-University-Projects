import java.io.*;
import java.net.*;

public class MomClient implements Mom {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void open(String cliID) {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("OPEN " + cliID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            out.println("CLOSE");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mkChannel(String name) {
        out.println("MKCHAN " + name);
    }

    @Override
    public void rmChannel(String name) {
        out.println("RMCHAN " + name);
    }

    @Override
    public void writeChannel(String chan, String msg) {
        out.println("WRITE " + chan + " " + msg);
    }

    @Override
    public String readChannel(String chan, boolean dontwait) {
        out.println("READ " + chan + " " + dontwait);
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
