public class Punto extends Figure {

    public Pos xy;
    public Punto(Pos pos){
        this.xy = new Pos(pos.x, pos.y);
    }

    @Override
    public void mover(int dx, int dy) {
        xy.x += dx;
        xy.y += dy;
    }
    @Override
    public String toString() {
        return "Punto" + " " + xy.x + ", " + xy.y + "\n";
    }

    @Override
    public boolean contienePos(Pos pos) {
        return pos.x == xy.x &&
                pos.y == xy.y;
    }

    public static Figure parse(String... s) {
        if (s.length != 2) {
            throw new RuntimeException("need 2 args for circle");
        }
        var x = Integer.parseInt(s[0]);
        var y = Integer.parseInt(s[1]);

        var pos = new Pos(x, y);

        return new Punto(pos);
    }
}

