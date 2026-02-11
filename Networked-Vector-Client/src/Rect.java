public class Rect extends Figure {
    public Pos Posmin;
    public Pos Posmax;


    public Rect(Pos pmin, Pos pmax) {
        this.Posmin = pmin;
        this.Posmax = pmax;
    }

    @Override
    public void mover(int dx, int dy) {
        Posmax.x += dx;
        Posmax.y += dy;
        Posmin.x += dx;
        Posmin.y += dy;
    }

    @Override
    public boolean contienePos(Pos pos) {
        return pos.x <= Posmax.x &&
                pos.x >= Posmin.x &&
                pos.y <= Posmax.y &&
                pos.y >= Posmin.y;
    }

    @Override
    public String toString() {
        return "Rectangulo" + " " + Posmax.x + " " + Posmax.y + " " + Posmin.x + " " + Posmin.y + "\n";
    }

    public static Figure parse(String... s) {
        if (s.length != 4) {
            throw new RuntimeException("need 4 args for circle");
        }
        var xmin = Integer.parseInt(s[0]);
        var ymin = Integer.parseInt(s[1]);
        var xmax = Integer.parseInt(s[2]);
        var ymax = Integer.parseInt(s[3]);
        var posmin = new Pos(xmin, ymin);
        var posmax = new Pos(xmax, ymax);

        return new Rect(posmin, posmax);
    }
}


