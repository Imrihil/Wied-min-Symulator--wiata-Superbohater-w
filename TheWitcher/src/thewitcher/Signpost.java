/*
 *
 * THE WITCHER
 *
 * Symulator swiata Superbohaterow
 * stworzony na przedmiot Programowanie Obiektowe.
 * 
 * 2014 (c) Mateusz Ledzianowski INF117226
 *
 */
package thewitcher;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;

/**
 * Konstruktor skrzyżowania.
 *
 * @author Imrihil
 */
public class Signpost extends Point {

    /**
     * Spis istniejących dróg.
     */
    private final Map<Cities, Road> roads = new EnumMap<>(Cities.class);
    /**
     * Semafor pilnujący, żeby na skrzyżowaniu mógł być tylko jeden cywil.
     */
    private volatile Semaphore sem;
    /**
     * Informacja, czy skrzyżowanie jest miastem.
     */
    private boolean city;

    /**
     * Konstruktor skrzyżowania.
     *
     * @param x
     * @param y
     * @param city
     * @throws IOException
     */
    public Signpost(int x, int y, boolean city) throws IOException {
        super(x, y);
        super.setIcon(ImageIO.read(new File("image/map/Signpost.png")));
        this.sem = new Semaphore(1);
        this.city = city;
    }

    /**
     * @param whatCity Miasto, które jest celem.
     * @return drogę, którą powinna wybrać Creature, by dostać się do whatCity.
     */
    public Road getRoads(Cities whatCity) {
        return roads.get(whatCity);
    }

    /**
     * @param city które jest celem.
     * @param road ktora powinna wybrac Creature kiedy idzie do wskazanego city.
     */
    public void addRoads(Cities city, Road road) {
        this.roads.put(city, road);
    }

    /**
     * Metoda pozwalająca narysować City na mapie.
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(super.getIcon(), super.getX() - super.getIcon().getWidth() / 2 - 1, super.getY() - super.getIcon().getHeight(), null);
    }

    /**
     * @return the city
     */
    public boolean isCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(boolean city) {
        this.city = city;
    }

    /**
     * @return the sem
     */
    public Semaphore getSem() {
        return sem;
    }

    /**
     * @param sem the sem to set
     */
    public void setSem(Semaphore sem) {
        this.sem = sem;
    }
}
