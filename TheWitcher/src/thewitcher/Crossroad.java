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

import java.io.IOException;

/**
 * Klasa Crossroad to skrzyżowania łączące poszczególne Road. Na każdym
 * Crossroad może być tylko jeden Civilian.
 *
 * @author Imrihil
 */
public class Crossroad extends Signpost {

    /**
     * Konstruktor Crossroad ustawiający jego współrzędne na mapie świata.
     *
     * @param x współrzędna X.
     * @param y współrzędna Y.
     * @throws java.io.IOException
     */
    public Crossroad(int x, int y) throws IOException {
        super(x, y, false);
        System.out.printf("Stworzyłem skrzyżowanie o współrzędnych: (%d, %d).\n", super.getX(), super.getY());
    }
}
