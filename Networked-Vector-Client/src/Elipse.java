public class Elipse extends Figure{
    public int rx;
    public int ry;
    public Pos c;

    public Elipse(int rx, int ry, Pos c) {
        this.rx = rx;
        this.ry = ry;
        this.c = c;
    }

    @Override
    public void mover(int dx, int dy) {
        c.x += dx;
        c.y += dy;
    }

    @Override
    public boolean contienePos(Pos pos) {
        double dx = pos.x - c.x;
        double dy = pos.y - c.y;
        return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) <= 1; // Fórmula de un círculo
    }

    @Override
    public String toString() {
        return "Elipse" + " " + rx + " " + " " + ry + " " + c.x + " " + c.y + "\n";
    }

    public static Figure parse(String... s) {
        if (s.length != 4) {
            throw new RuntimeException("need 3 args for circle");
        }
        var rx = Integer.parseInt(s[0]);
        var ry = Integer.parseInt(s[1]);
        var x = Integer.parseInt(s[2]);
        var y = Integer.parseInt(s[3]);
        var pos = new Pos(x, y);
        return new Elipse(rx, ry, pos);
    }
}