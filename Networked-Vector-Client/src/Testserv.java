import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import static org.junit.Assert.*;

public class Testserv {

    static Server server;
    static int PUERTO = 6000;

    @BeforeClass
    public static void iniciarServidor() {
        // Iniciamos el servidor con el ServicioBinario
        server = new Server(PUERTO, new ServicioDibujo());
        server.start();
    }

    @AfterClass
    public static void pararServidor() {
        server.close();
    }

    @Test
    public void testLoginYCrearDibujo() throws Exception {
        Client cliente = new Client("localhost", PUERTO);
        // 1. LOGIN
        cliente.login("Jorge");
        cliente.login("Jorge");

        // 2. CREAR DIBUJO
        int id = cliente.creardibujo("Dibujo1 kjgjgg", "Jorge");
        // CREAR FIGURA
        int id1 =cliente.crearFigura(id, "Circulo 4 4 5");
        int id12 =cliente.crearFigura(id, "Circulo 4 4 5");
        System.out.println(id12);
        int id2 = cliente.crearFigura(1, "Punto 4 4");
        System.out.println(id2);
        cliente.obtenerFigura(1);
        // MOVER FIGURA
        cliente.moverFigura(id, id1, 1, 1);
        cliente.obtenerFigura(1);
        // 3. OBTENER FIGURAS
        int idd2 = cliente.creardibujo("Dibujow1 kjgjgg", "Jorge");
        cliente.borrarFigura(1, id1);

        cliente.obtenerFigura(1);
        // 4. BORRAR DIBUJO
        // 5. CERRAR
        cliente.close();
    }
}
