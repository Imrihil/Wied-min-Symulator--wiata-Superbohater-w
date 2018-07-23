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

/**
 * Klasa zawierająca wynik rozgrywki.
 *
 * @author Imrihil
 */
public class Wynik implements Comparable<Wynik> {

    /**
     * Nazwa gracza.
     */
    private String name;
    /**
     * Czas gry.
     */
    private long time;
    /**
     * Liczba zabitych przeciwników.
     */
    private int kills;

    /**
     * Konstruktor bezargumentowy wymagany do serializacji.
     */
    public Wynik() {
    }

    /**
     * Konstruktor wyniku.
     *
     * @param name
     * @param time
     * @param kills
     */
    public Wynik(String name, long time, int kills) {
        this.name = name;
        this.time = time;
        this.kills = kills;
    }

    @Override
    public int compareTo(Wynik o) {
        return Long.compare(o.getTime(), this.getTime());
    }

    @Override
    public String toString() {
        String norm = super.toString();
        return (norm + "(" + this.getName() + ", " + Long.toString(this.getTime()) + ", " + Integer.toString(this.getKills()) + ")");
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return the kills
     */
    public int getKills() {
        return kills;
    }

    /**
     * @param kills the kills to set
     */
    public void setKills(int kills) {
        this.kills = kills;
    }
}
