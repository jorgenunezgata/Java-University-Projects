
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class Draw{

    public Figures figs;
    public User owner;
    public int id;
    private static AtomicInteger idfigurasprivate = new AtomicInteger(0);
    public List<User> editors = new ArrayList<User>();
    public List<User> viewers = new ArrayList<User>(); // <User>

    public Draw(Figures Figures, User user, int id){
        this.figs = Figures;
        this.owner = user;
        this.id = id;

    }
    public void addfigureindraw(Figure figure) {
        int idfigura = idfigurasprivate.incrementAndGet();
        figure.id = idfigura;
        figs.addfigure(figure);

    }

    public void removefigureindraw(Figure figure) {
        figs.removefigure(figure);
    }

    public void mover(String figura, int dx, int dy) {
        for (Figure figure : figs.figures) {
            if (figure.toString().equals(figura)) {
                figure.mover(dx, dy);

            }
        }
    }

    public Figure localizarFigura(Pos pos) {
        for (Figure figure : figs.figures) {
            if (figure.contienePos(pos)) {
                return figure;
            }
        }
        return null;
    }

    public void SaveTo(Path fichero){
        try (BufferedWriter writer = Files.newBufferedWriter(fichero)) {
            writer.write(figs.toString());
            System.out.println("Escritura realizada correctamente.");
            writer.flush();
        }
        catch (IOException e) {
            System.err.println("Error al escribir en el fichero: " + e.getMessage());
        }

    }

    public void LoadTo(Path fichero){
        try (BufferedReader reader = Files.newBufferedReader(fichero, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);


            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }


    @FunctionalInterface
    private interface Parser {
        Figure parse(String... args);
    }

    private static Map<String, Parser> parsers;


    public static Figure parse(String s) {
        int p = s.indexOf(' ');
        if (p < 0) {
            p = s.length();
        }
        var name = s.substring(0, p);
        Parser parse = parsers.get(name);
        if (parse == null) {
            throw new RuntimeException("bad figure: <"+s+">");
        }
        s = s.substring(p);
        s = s.trim();
        String[] els = s.split("\\s+");
        return parse.parse(els);
    }

    static {
        parsers = new HashMap<>();
        parsers.put("Circulo", Circulo::parse);
        parsers.put("Rectangulo", Rect::parse);
        parsers.put("Elipse", Elipse::parse);
        parsers.put("Punto", Punto::parse);

    }

}



