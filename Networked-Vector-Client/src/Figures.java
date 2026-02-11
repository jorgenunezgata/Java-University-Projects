import java.util.ArrayList;

public class Figures extends Figure{
    ArrayList<Figure> figures;
    public Figures() {
        this.figures = new ArrayList<Figure>();
    }
    public void addfigure(Figure figure){figures.add(figure);
    }

    public void removefigure(Figure figure){figures.remove(figure);}

    public Figure getFigureById(int id) {
        for (Figure f : figures) {
            if (f.id == id) {
                return f;
            }
        }
        return null;
    }
    public void moverfigura(int id, int dx, int dy){
        Figure figura = getFigureById(id);
        figura.mover(dx, dy);
    }


    @Override
    public void mover(int dx, int dy) {
        for (Figure figure : figures) {
            figure.mover(dx, dy);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Figure f : figures) {
            sb.append(f.toString()).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public boolean contienePos(Pos pos) {
        for (Figure figure : figures) {
            if (figure.contienePos(pos)) {
                return true;
            }
        }
        return false;
    }
}
