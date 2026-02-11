
public class Circulo extends Figure{
    public int r;
    public Pos c;
    public Circulo(int r, Pos c) {
        this.r = r;
        this.c = c;
    }

    @Override
    public void mover(int dx, int dy) {
        c.x += dx;
        c.y += dy;
    }

    @Override
    public boolean contienePos(Pos pos) {
        int dx = pos.x - c.x;
        int dy = pos.y - c.y;
        return dx * dx + dy * dy <= r * r; // Fórmula de un círculo
    }

    @Override
    public String toString() {
        return "Circulo"+ " " + c.x + " "+ c.y + " " + r + "\n";
    }

    public static Figure parse(String... s) {
        if (s.length != 3) {
            throw new RuntimeException("need 3 args for circle");
        }
        var x = Integer.parseInt(s[0]);
        var y = Integer.parseInt(s[1]);
        var r = Integer.parseInt(s[2]);
        var pos = new Pos(x, y);
        return new Circulo(r, pos);
    }

}
