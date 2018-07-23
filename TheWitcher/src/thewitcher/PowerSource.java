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
 * Klasa PowerSource to źródła mocy, z których potential czerpią Monster
 * atakujące City. Ich potential rośnie stopniowo, a szybkość wzrostu jest
 * uzależniona od population danego City.
 *
 * @author Imrihil
 */
public class PowerSource {

    /**
     * Unikatowa wartość identyfikatora.
     */
    private final int id;
    /**
     * Typ informuje, jaką umiejętność wzmacnia.
     */
    private final Skills type;
    /**
     * Potencjał.
     */
    private double potential;
    /**
     * Ilość stworzonych źródeł mocy.
     */
    private static int numberOfPowerSources = 0;

    /**
     * Konstruktor klasy PowerSource inicjujący odpowiednią wartość id.
     *
     * @param type0 określa typ źródła mocy.
     * @param potential początkowy potencjał.
     */
    public PowerSource(Skills type0, int potential) {
        this.id = numberOfPowerSources++;
        this.type = type0;
        this.potential = (double) potential;
    }

    /**
     * Metoda obliczająca potencjał wszystkich istniejących źródeł mocy.
     */
    public static void countPotential() {

    }

    /**
     * Metoda zmieniająca potencjał źródła o podaną wartość.
     *
     * @param potential
     */
    public void changePotential(double potential) {
        this.potential += potential;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the type
     */
    public Skills getType() {
        return type;
    }

    /**
     * @return the potential
     */
    public int getPotential() {
        return (int) potential;
    }

    /**
     * @param potential the potential to set
     */
    public void setPotential(double potential) {
        this.potential = potential;
    }
}
