import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;


public class Msg {

    public static final int DSZOFF = 0;
    public static final int KINDOFF = DSZOFF+4;
    public static final int TAGOFF = KINDOFF+1;
    public static final int HDRSZ = TAGOFF+4;

    // Tipos de mensajes donde cada uno es un byte
    public static final byte LOGIN = 1;
    public static final byte CREAR_DIBUJO = 2;
    public static final byte BORRAR_DIBUJO = 3;
    public static final byte CREAR_FIGURA = 4;
    public static final byte BORRAR_FIGURA = 5;
    public static final byte MOVER_FIGURA = 6;
    public static final byte OBTENER_FIGURAS = 7;
    public static final byte ERROR = 8;

    private static int atomicTag = 0;

    ByteBuffer buf;	// La principal estructura del mensaje es un buffer donde tiene una cabecera con el kind, el tag y el tipo de mensaje

    public int dsz;
    public byte kind;
    public int tag;


    private static ByteBuffer mkbuf() {
        var buf = ByteBuffer.allocate(4096);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf;
    }
    protected void setDsz(int dsz) {
        this.dsz = dsz;
        buf.putInt(DSZOFF, dsz);
    }

    protected void setKind(byte kind) {
        this.kind = kind;
        buf.put(KINDOFF, kind);
    }

    protected void setTag(int tag) {
        this.tag = tag;
        buf.putInt(TAGOFF, tag);
    }

    static void creartag() {
        atomicTag = atomicTag + 1;
    }

    /** buf pos/limit to std places */
    protected void setStdLimits() {
        buf.position(0);
        buf.limit(HDRSZ+dsz);
    }

    protected void setAddLimits() {
        buf.position(HDRSZ+dsz);
        buf.limit(buf.capacity());
    }

    static void putStr(ByteBuffer buf, String str) {
        var bytes = str.getBytes();
        var n = bytes.length;
        buf.putInt(n);
        buf.put(bytes);

    }
    static String getStr(ByteBuffer buf) {
        var n = buf.getInt();
        if (n == 0) {
            return "";
        }
        var b = new byte[n];
        buf.get(b);
        return new String(b);

    }

    static int getag(ByteBuffer buf) {
        return buf.getInt(TAGOFF);
    }

    public Msg(byte kind, int tag) {
        this.buf = mkbuf();
        setKind(kind);
        setTag(tag);
    }


    public Msg(ByteBuffer buf) {
        this.buf = buf;
        kind = buf.get(KINDOFF);
        tag = buf.getInt(TAGOFF);
        dsz = buf.getInt(DSZOFF);
        setStdLimits();
    }
    public static Msg login(String user) {
        creartag();
        return new Login(user, atomicTag);
    }

    public static Msg rlogin(String userId, int tag) {
        return new Respuestastr(userId, tag, LOGIN);
    }


    public static Msg creardibujo(String NombreDibujo,  String user) {
        creartag();
        return new CrearDibujo(NombreDibujo, user, atomicTag);
    }

    public static Msg rcreardibujo(int idDibujo, int tag) {
        return new Respuestaint(idDibujo, tag, CREAR_DIBUJO);
    }
    public static Msg borrarrdibujo(int iddibujo) {
        creartag();
        return new BorrarDibujo(iddibujo, atomicTag);    }

    public static Msg rborrarDibujo(int iddibujo, int tag) {
        return new Respuestaint(iddibujo, tag, BORRAR_DIBUJO);
    }

    public static Msg crearFigura(int idDibujo, String Figura) {
        creartag();
        return new CrearFigura(Figura, idDibujo, atomicTag);
    }
    public static Msg rcrearFigura(int idFigura, int tag) {
        return new Respuestaint(idFigura, tag, CREAR_FIGURA);
    }

    public static Msg borrarFigura(int idDibujo, int idFigura) {
        creartag();
        return new BorrarFigura(idFigura, idDibujo, atomicTag);
    }
    public static Msg rborrarFigura(String idFigura, int tag) {
        return new Respuestastr(idFigura, tag, BORRAR_FIGURA);

    }
    public static Msg moverFigura(int idDibujo, int idFigura, int dx, int dy) {
        creartag();
        return new MoverFigura(idDibujo, idFigura, dx, dy, atomicTag);
    }

    public static Msg rmoverFigura(String Figura, int tag) {
        return new Respuestastr(Figura,tag, MOVER_FIGURA);
    }
    public static Msg obtenerFigura(int idDibujo) {
        creartag();
        return new ObtenerFiguras(idDibujo, atomicTag);
    }
    public static Msg robtenerfigura (String figuras, int tag) {
        return new Respuestastr(figuras,tag, OBTENER_FIGURAS);
    }
    public static Msg error(String error, int tag) {
        creartag();
        return new Error(error, tag);
    }


    static int readn(ReadableByteChannel sk, ByteBuffer buf, int sz)  {
        var tot = 0;
        var pos = buf.position();
        buf.limit(pos+sz);
        var nr = 0;
        while (tot < sz) {
            try {
                nr = sk.read(buf);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (nr < 0) {
                if (tot == 0) {
                    return nr;
                }
                throw new RuntimeException("short read in readn");
            }
            tot += nr;
        }
        return tot;
    }

    public static Msg readFrom(ReadableByteChannel sk) {
        var buf = mkbuf();
        var nr = readn(sk, buf, HDRSZ);
        if (nr < 0) {
            return null;
        }
        buf.flip();
        int dsz = buf.getInt(DSZOFF);
        byte kind = buf.get(KINDOFF);
        int tag = buf.getInt(TAGOFF);
        buf.position(HDRSZ);
        buf.limit(HDRSZ+dsz);
        nr = readn(sk, buf, dsz);
        if (nr < 0) {
            throw new RuntimeException("short msg");
        }

        switch (kind) {
            case LOGIN:
                return new Login(buf);
            case CREAR_DIBUJO:
                return new CrearDibujo(buf);
            case BORRAR_DIBUJO:
                return new BorrarDibujo(buf);
            case CREAR_FIGURA:
                return new CrearFigura(buf);
            case BORRAR_FIGURA:
                return new BorrarFigura(buf);
            case MOVER_FIGURA:
                return new MoverFigura(buf);
            case OBTENER_FIGURAS:
                return new ObtenerFiguras(buf);
            case ERROR:
                return new Error(buf);
            default:
                throw new RuntimeException("bad msg kind "+kind);
        }
    }

    public static Msg readFromServer(ReadableByteChannel sk) {
        var buf = mkbuf();
        var nr = readn(sk, buf, HDRSZ);
        if (nr < 0) {
            return null;
        }
        buf.flip();
        int dsz = buf.getInt(DSZOFF);
        byte kind = buf.get(KINDOFF);
        int tag = buf.getInt(TAGOFF);
        buf.position(HDRSZ);
        buf.limit(HDRSZ+dsz);
        nr = readn(sk, buf, dsz);
        if (nr < 0) {
            throw new RuntimeException("short msg");
        }

        switch (kind) {
            case LOGIN:
                return new Respuestastr(buf);
            case CREAR_DIBUJO:
                return new Respuestaint(buf);
            case BORRAR_DIBUJO:
                return new Respuestaint(buf);
            case CREAR_FIGURA:
                return new Respuestaint(buf);
            case BORRAR_FIGURA:
                return new Respuestastr(buf);
            case MOVER_FIGURA:
                return new Respuestastr(buf);
            case OBTENER_FIGURAS:
                return new Respuestastr(buf);
            case ERROR:
                return new Error(buf);
            default:
                throw new RuntimeException("bad msg kind "+kind);
        }
    }

    public void writeTo(WritableByteChannel sk) {
        synchronized (sk) {
            var sz = buf.limit();

            try {
                var nw = sk.write(buf);
                if (nw != sz) {
                    throw new RuntimeException("short write");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                buf.position(0);
                buf.limit(sz);
            }
        }
    }



    private static class StrMsg extends Msg {
        public String str;

        public StrMsg(String v, int tag, byte kind) {
            super(kind, tag);
            var n = v.getBytes().length;

            setAddLimits();
            putStr(buf, v);
            buf.capacity();
            setDsz(buf.position() - HDRSZ);
            setStdLimits();
            this.str = v;
        }

        StrMsg(ByteBuffer buf) {
            super(buf);

            buf.position(HDRSZ);
            str = getStr(buf);
            setStdLimits();
        }



    }

    private static class IntMsg extends Msg {
        public int id;

        public IntMsg(int v, int tag, Byte kind) {
            super(kind, tag);
            setAddLimits();
            buf.putInt(v);
            setDsz(buf.position() - HDRSZ);
            setStdLimits();
            this.id = v;
        }

        IntMsg(ByteBuffer buf) {
            super(buf);
            buf.position(HDRSZ);
            id = buf.getInt();
            setStdLimits();
        }


    }
    private static class StrIntMsg extends Msg {
        public String str;
        public int number;

        public StrIntMsg(String s, int n, int tag, byte kind) {
            super(kind, tag);
            setAddLimits();
            putStr(buf, s);
            buf.putInt(n);
            setDsz(buf.position() - HDRSZ);
            setStdLimits();
            this.str = s;
            this.number = n;

        }

        StrIntMsg(ByteBuffer buf) {
            super(buf);
            buf.position(HDRSZ);
            this.str = getStr(buf);
            this.number = buf.getInt();

            setStdLimits();
        }
    }
    public static class CrearDibujo extends Msg {
        public String nombredibujo;
        public String nombreuser;

        public CrearDibujo(String nombredibujo, String nombreuser, int tag) {
            super(CREAR_DIBUJO, tag);
            setAddLimits();
            putStr(buf, nombredibujo);
            putStr(buf, nombreuser);
            setDsz(buf.position() - HDRSZ);
            setStdLimits();

            this.nombredibujo = nombredibujo;
            this.nombreuser = nombreuser;

        }

        CrearDibujo(ByteBuffer buf) {
            super(buf);
            buf.position(HDRSZ);
            this.nombredibujo = getStr(buf);
            this.nombreuser = getStr(buf);

            setStdLimits();
        }
    }
    public static class BorrarFigura extends Msg {
        public int idDraw;
        public int id;
        public BorrarFigura(int idDraw, int id, int tag) {
            super(BORRAR_FIGURA, tag);
            setAddLimits();
            buf.putInt(id);
            buf.putInt(idDraw);
            setDsz(buf.position() - HDRSZ);
            setStdLimits();

            this.idDraw = idDraw;

            this.id = id;
        }

        BorrarFigura(ByteBuffer buf) {
            super(buf);
            buf.position(HDRSZ);
            this.id = buf.getInt();
            this.idDraw = buf.getInt();

            setStdLimits();
        }
    }

    public static class MoverFigura extends Msg {
        public int idDraw;
        public int dx;
        public int dy;
        public int id;
        public MoverFigura(int idDraw,int id, int dx, int dy, int tag) {
            super(MOVER_FIGURA, tag);
            setAddLimits();
            buf.putInt(id);
            buf.putInt(dx);
            buf.putInt(dy);
            buf.putInt(idDraw);
            setDsz(buf.position() - HDRSZ);
            setStdLimits();

            this.idDraw = idDraw;
            this.dx = dx;
            this.dy = dy;
            this.id = id;
        }

        MoverFigura(ByteBuffer buf) {
            super(buf);
            buf.position(HDRSZ);
            this.id = buf.getInt();
            this.dx = buf.getInt();
            this.dy = buf.getInt();
            this.idDraw = buf.getInt();

            setStdLimits();
        }
    }


    public static class CrearFigura extends StrIntMsg {
        public CrearFigura(String s, int n, int tag) {
            super(s, n, tag, CREAR_FIGURA);
        }

        CrearFigura(ByteBuffer buf) {
            super(buf);
        }
    }

    public static class BorrarDibujo extends IntMsg{
        public BorrarDibujo(int n, int tag){
            super(n, tag, BORRAR_DIBUJO);
        }
        BorrarDibujo(ByteBuffer buf) {
            super(buf);
        }
    }
    public static class ObtenerFiguras extends IntMsg{
        public ObtenerFiguras(int n, int tag){
            super(n, tag, OBTENER_FIGURAS);
        }
        ObtenerFiguras(ByteBuffer buf) {
            super(buf);
        }
    }
    public static class Respuestaint extends IntMsg{
        public Respuestaint(int n, int tag, byte kind){
            super(n, tag, kind);
        }
        Respuestaint(ByteBuffer buf) {
            super(buf);
        }
    }
    public static class Respuestastr extends StrMsg{
        public Respuestastr(String s, int tag, byte kind){
            super(s, tag, kind);}
        Respuestastr(ByteBuffer buf) {
            super(buf);
        }
    }

    public static class Login extends StrMsg{
        public Login(String s, int tag){
            super(s, tag, LOGIN);
        }
        Login(ByteBuffer buf) {
            super(buf);
        }
    }
    public static class Error extends StrMsg{
        public Error(String s, int tag){
            super(s, tag, ERROR);
        }
        Error(ByteBuffer buf) {
            super(buf);
        }
    }

}
