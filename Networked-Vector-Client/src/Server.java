import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private ServerSocketChannel ssk;
    private Servicio servicio;

    public Server(int port, Servicio servicio) {
        this.servicio = servicio;
        try {
            ssk = ServerSocketChannel.open();
            ssk.bind(new InetSocketAddress(port));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    SocketChannel sk = ssk.accept();
                    System.err.println("New client connected");
                    new Cli(sk, servicio).start();
                } catch (AsynchronousCloseException ex) {
                    break;
                } catch (Exception ex) {
                    System.err.println("Server exception: " + ex);
                    break;
                }
            }
        }).start();
    }

    public void close() {
        try {
            ssk.close();
        } catch (IOException ignored) {}
    }

    static class Cli extends Thread {
        private SocketChannel sk;
        private Servicio servicio;

        public Cli(SocketChannel sk, Servicio servicio) {
            this.sk = sk;
            this.servicio = servicio;
        }

        public void run() {
            while (true) {
                Msg msg = Msg.readFrom(sk);
                if (msg == null) break;
                Msg respuesta = servicio.procesarMensaje(msg);
                if (respuesta != null) {
                    respuesta.writeTo(sk);
                }
            }
        }
    }
}
