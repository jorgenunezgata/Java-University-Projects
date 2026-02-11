import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ServicioDibujo extends Servicio {

    private Map<Integer, Draw> dibujos = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private static AtomicInteger idDraws = new AtomicInteger(0);

    public ServicioDibujo() {}

    @Override
    public Msg procesarMensaje(Msg msg) {
        int tag = msg.tag;
        switch (msg.kind) {
            case Msg.LOGIN:
                String username = ((Msg.Login) msg).str;
                users.put(username, new User(username));
                return Msg.rlogin(username, tag);
            case Msg.CREAR_DIBUJO:
                final int idDraw = idDraws.incrementAndGet();
                String username1 = ((Msg.CrearDibujo) msg).nombreuser;
                Figures figures = new Figures();
                User user = users.get(username1);
                dibujos.put(idDraw, new Draw(figures, user, idDraw));

                return Msg.rcreardibujo(idDraw, tag);
            case Msg.BORRAR_DIBUJO:
                int idDelete = ((Msg.BorrarDibujo) msg).id;
                dibujos.remove(idDelete);

                return Msg.rborrarDibujo(idDelete, tag);
            case Msg.CREAR_FIGURA:
                String figura = ((Msg.CrearFigura) msg).str;
                int id = ((Msg.CrearFigura) msg).number;
                Figure f = dibujos.get(id).parse(figura);
                Draw draw = dibujos.get(id);
                if (draw == null) {
                    throw new RuntimeException("El dibujo no existe");
                }
                dibujos.get(id).addfigureindraw(f);
                return Msg.rcrearFigura(f.id, tag);
            case Msg.BORRAR_FIGURA:
                int idfiguredeleted = ((Msg.BorrarFigura) msg).id;
                int idDibujoBorrar = ((Msg.BorrarFigura) msg).idDraw;
                Draw Dibujo = dibujos.get(idDibujoBorrar);
                Figure fd = Dibujo.figs.getFigureById(idfiguredeleted);
                Dibujo.removefigureindraw(fd);
                dibujos.get(idDibujoBorrar).removefigureindraw(fd);

                return Msg.rborrarFigura(fd.toString(), tag);
            case Msg.MOVER_FIGURA:
                int idMove = ((Msg.MoverFigura) msg).id;
                int dx = ((Msg.MoverFigura) msg).dx;
                int dy = ((Msg.MoverFigura) msg).dy;
                int idDibujoMove = ((Msg.MoverFigura) msg).id;

                Draw dibujo =dibujos.get(idDibujoMove);
                dibujo.figs.moverfigura(idMove, dx, dy);
                Figures figuras = dibujo.figs;
                String figuramove = figuras.getFigureById(idMove).toString();
                return Msg.rmoverFigura(figuramove, tag);
            case Msg.OBTENER_FIGURAS:
                int idObtener = ((Msg.ObtenerFiguras) msg).id;
                String figurasDraw = dibujos.get(idObtener).figs.toString();
                return Msg.robtenerfigura(figurasDraw, tag);
            default:
                return Msg.error("El mensaje enviado no es valido", tag);
        }
    }
}
