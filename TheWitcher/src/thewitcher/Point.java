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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasa abstrakcyjna Point przechowująca informacje o współrzędnych obiektu na
 * mapie świata.
 *
 * @author Imrihil
 */
public abstract class Point extends JPanel {

    /**
     * Ikona obiektu.
     */
    private BufferedImage icon;
    /**
     * Obrazek obiektu.
     */
    private BufferedImage image;
    /**
     * Obrazek wskaźnika obiektu.
     */
    private BufferedImage indicator;
    /**
     * Informacja, czy obiekt ma być wskazywany.
     */
    private boolean highlighted;
    /**
     * Współrzędna x.
     */
    private double x;
    /**
     * Współrzędna y.
     */
    private double y;

    /**
     * Konstruktor punktu.
     *
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        indicator = null;
        try {
            this.indicator = ImageIO.read(new File("image/Highlight.png"));
        } catch (IOException ex) {
            Logger.getLogger(Point.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda pozwalająca narysować Point na mapie (każda podklasa będzie
     * wyglądała inaczej, więc musi mieć osobną implementację tej metody).
     */
    @Override
    public void paintComponent(Graphics g) {
        if (highlighted) {
            g.drawImage(this.getIndicator(), (int) x - this.getIndicator().getWidth() / 2, (int) y - this.getIndicator().getHeight() / 2, null);
        }
        g.drawImage(this.getIcon(), (int) x - this.getIcon().getWidth() / 2, (int) y - this.getIcon().getHeight() / 2, null);
    }

    /**
     * @return the x
     */
    @Override
    public int getX() {
        return (int) x;
    }

    /**
     * @return the x
     */
    public double getDX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = (double) x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    @Override
    public int getY() {
        return (int) y;
    }

    /**
     * @return the x
     */
    public double getDY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = (double) y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the image
     */
    public BufferedImage getIcon() {
        return icon;
    }

    /**
     * @param icon the image to set
     */
    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    /**
     * Metoda zwracająca odleglość między dwoma punktami.
     *
     * @param b
     * @return
     */
    public double dist(Point b) {
        double result;
        double xsize = b.x - this.x;
        double ysize = b.y - this.y;
        result = xsize * xsize + ysize * ysize;
        result = sqrt(result);
        return result;
    }

    /**
     * @return the highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @param highlighted the highlighted to set
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * @return the indicator
     */
    public BufferedImage getIndicator() {
        return indicator;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
