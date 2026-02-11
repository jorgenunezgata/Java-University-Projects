
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.*;

public class Client {
    SocketChannel socketChannel;
    ByteBuffer buffer;

    public Client(String host, int port) {
        try {
            // Establecemos la conexión con el servidor
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(true);
            // Inicializamos el buffer
            buffer = ByteBuffer.allocate(4096);
        } catch (IOException e) {
            throw new RuntimeException("Error al conectar con el servidor", e);
        }
    }

    public void writeMsg(Msg msg) {
        msg.writeTo(socketChannel);
    }


    public Msg readMsg() {
        return Msg.readFromServer(socketChannel);
    }


    public void login(String user){
        writeMsg(Msg.login(user));
        readMsg();


    }

    public int creardibujo(String nombreDibujo, String user) {
        writeMsg(Msg.creardibujo(nombreDibujo, user));
        Msg msg = readMsg();
        int id = ((Msg.Respuestaint) msg).id;
        return id;
    }
    public void moverFigura(int idDibujo, int idFigura, int dx, int dy) {
        writeMsg(Msg.moverFigura(idDibujo, idFigura, dx, dy));
        readMsg();
    }

    public void borrarFigura(int idDibujo, int idFigura) {
        writeMsg(Msg.borrarFigura(idDibujo, idFigura));
        readMsg();

    }

    public int crearFigura(int idDibujo, String Figura) {
        writeMsg(Msg.crearFigura(idDibujo, Figura));
        Msg msg = readMsg();
        int id = ((Msg.Respuestaint) msg).id;
        return id;
    }

    public String obtenerFigura(int idDibujo) {
        writeMsg(Msg.obtenerFigura(idDibujo));
        Msg msg =readMsg();
        String figuras = ((Msg.Respuestastr) msg).str;
        return figuras;
    }

    public void borrarDibujo(int iddibujo) {
        writeMsg(Msg.borrarrdibujo(iddibujo));
        readMsg();

    }


    public void close() {
        try {
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
