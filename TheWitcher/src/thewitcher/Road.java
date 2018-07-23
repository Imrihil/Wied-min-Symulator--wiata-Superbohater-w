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

import java.awt.Color;
import java.awt.Graphics;

/**
 * Klasa Road to droga, która łączy City z Crossroad lub dwa różne Crossroad.
 * Road zawsze jest dwukierunkowa i poruszają się po niej Creature, aby
 * przemieszczać się między City.
 *
 * @author Imrihil
 */
public class Road {

    /**
     * Signpost, z ktorego prowadzi droga.
     */
    private final Signpost A;
    /**
     * Signpost, które znajduje się na końcu drogi.
     */
    private final Signpost B;

    /**
     * Konstruktor Road łączącej 2 Signposty.
     *
     * @param A Signpost na jednym końcu Road.
     * @param B Signpost na drugim końcu Road.
     */
    public Road(Signpost A, Signpost B) {
        this.A = A;
        this.B = B;

        System.out.printf("Stworzyłem drogę łączącą (%d, %d) z (%d, %d).\n", A.getX(), A.getY(), B.getX(), B.getY());
    }

    /**
     * Metoda rysująca drogę na mapie świata.
     *
     * @param graphics
     */
    public void draw(Graphics graphics) {
        /*
         * Robię to w ten sposób, bo jest to ładniejsze niż:
         * Graphics2D graph = (Graphics2D) graphics;
         * graph.setStroke(new BasicStroke(6));
         */
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawLine(getA().getX(), getA().getY(), getB().getX(), getB().getY());
        graphics.drawLine(getA().getX() - 1, getA().getY(), getB().getX() - 1, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 1, getB().getX(), getB().getY() - 1);
        graphics.drawLine(getA().getX() + 1, getA().getY(), getB().getX() + 1, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 1, getB().getX(), getB().getY() + 1);
        graphics.drawLine(getA().getX() - 1, getA().getY() - 1, getB().getX() - 1, getB().getY() - 1);
        graphics.drawLine(getA().getX() + 1, getA().getY() + 1, getB().getX() + 1, getB().getY() + 1);

        graphics.drawLine(getA().getX() - 1, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 1, getB().getX(), getB().getY() - 2);
        graphics.drawLine(getA().getX() + 1, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 1, getB().getX(), getB().getY() + 2);

        graphics.drawLine(getA().getX() - 1, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 1, getB().getX(), getB().getY() - 2);
        graphics.drawLine(getA().getX() + 1, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 1, getB().getX(), getB().getY() + 2);
        graphics.drawLine(getA().getX() - 1, getA().getY() - 2, getB().getX() - 1, getB().getY() - 2);
        graphics.drawLine(getA().getX() + 1, getA().getY() + 2, getB().getX() + 1, getB().getY() + 2);

        graphics.drawLine(getA().getX() - 2, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 2, getB().getX(), getB().getY() - 2);
        graphics.drawLine(getA().getX() + 2, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 2, getB().getX(), getB().getY() + 2);
        graphics.drawLine(getA().getX() - 2, getA().getY() - 2, getB().getX() - 2, getB().getY() - 2);
        graphics.drawLine(getA().getX() + 2, getA().getY() + 2, getB().getX() + 2, getB().getY() + 2);

        graphics.drawLine(getA().getX() - 1, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 1, getB().getX(), getB().getY() + 2);
        graphics.drawLine(getA().getX() + 1, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 1, getB().getX(), getB().getY() - 2);

        graphics.drawLine(getA().getX() - 1, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 1, getB().getX(), getB().getY() + 2);
        graphics.drawLine(getA().getX() + 1, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 1, getB().getX(), getB().getY() - 2);
        graphics.drawLine(getA().getX() - 1, getA().getY() + 2, getB().getX() - 1, getB().getY() + 2);
        graphics.drawLine(getA().getX() + 1, getA().getY() - 2, getB().getX() + 1, getB().getY() - 2);

        graphics.drawLine(getA().getX() - 2, getA().getY(), getB().getX() - 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() + 2, getB().getX(), getB().getY() + 2);
        graphics.drawLine(getA().getX() + 2, getA().getY(), getB().getX() + 2, getB().getY());
        graphics.drawLine(getA().getX(), getA().getY() - 2, getB().getX(), getB().getY() - 2);
        graphics.drawLine(getA().getX() - 2, getA().getY() + 2, getB().getX() - 2, getB().getY() + 2);
        graphics.drawLine(getA().getX() + 2, getA().getY() - 2, getB().getX() + 2, getB().getY() - 2);

        graphics.setColor(Color.GRAY);
        graphics.drawLine(getA().getX(), getA().getY(), getB().getX(), getB().getY());
        graphics.setColor(Color.WHITE);
    }

    /**
     * @return the A
     */
    public Signpost getA() {
        return A;
    }

    /**
     * @return the B
     */
    public Signpost getB() {
        return B;
    }
}
