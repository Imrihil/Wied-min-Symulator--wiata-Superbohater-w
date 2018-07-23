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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Klasa City to miasta istniejące na świecie. Zamieszkują je cywile, potwory je
 * niszczą, a bohaterowie strzegą przed potworami.
 *
 * @author Imrihil
 */
public class City extends Signpost {

    /**
     * Nazwa City.
     */
    private final Cities name;
    /**
     * Ilość Civilian zamieszkujących w danej chwili City.
     */
    private volatile int population;
    /**
     * każde PowerSource będzie stworzone dla każdego City, ale nie z każdego
     * będzie można korzystać (tylko jeśli powerSourceExist[i]==true).
     */
    private Map<Skills, PowerSource> powerSource = new EnumMap<>(Skills.class);
    /**
     * Informacja o tym, czy City jest stolicą.
     */
    private boolean capital;
    /**
     * Informacja o tym, czy City jest w danej chwili atakowane przez Monster.
     */
    private boolean underAttack;
    /**
     * Informacja o tym, czy City jest zniszczone.
     */
    private boolean destroyed;
    /**
     * Spis istniejących miast.
     */
    private static volatile Map<Cities, City> cities = new EnumMap<>(Cities.class);
    /**
     * Spis istniejących cywili.
     */
    private static volatile Map<Civilian, Thread> civilians = new HashMap<>();
    /**
     * Obrazek zaatakowanego miasta.
     */
    private final BufferedImage attacked;

    /**
     * Konstruktor Miasta.
     *
     * @param x współrzędna x.
     * @param y współrzędna y.
     * @param name - nazwa.
     * @param population - populacja.
     * @param powerSource - źródła mocy w mieście
     * @param capital - czy miasto jest stolicą świata.
     * @param cities
     * @param civilians
     * @param icon
     * @param image
     * @throws IOException
     */
    City(int x, int y, Cities name, int population, Map<Skills, PowerSource> powerSource, boolean capital, Map<Cities, City> cities, Map<Civilian, Thread> civilians, BufferedImage icon, BufferedImage image) throws IOException {
        super(x, y, true);
        this.name = name;
        this.population = population;
        this.powerSource = powerSource;
        this.capital = capital;
        this.underAttack = false;
        this.destroyed = false;
        City.cities = cities;
        City.civilians = civilians;
        super.setIcon(icon);
        super.setImage(image);
        System.out.printf("Stworzylem miasto o nazwie %s i wspolrzednych (%d, %d). W miescie sa:\n", this.name, super.getX(), super.getY());
        if (capital) {
            System.out.println("Miasto jest stolica!");
        }
        for (Skills currentKey : powerSource.keySet()) {
            switch (currentKey) {
                case resistance: {
                    System.out.printf(" Zbrojownia o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                case speed: {
                    System.out.printf(" Stajnia o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                case inteligence: {
                    System.out.printf(" Akademia o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                case strength: {
                    System.out.printf(" Koszary o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                case energy: {
                    System.out.printf(" Siedziba maga o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                case fight: {
                    System.out.printf(" Arena o potencjale: %d\n", powerSource.get(currentKey).getPotential());
                    break;
                }
                default: {
                    System.out.printf("Blad.\n");
                }
            }
        }
        if (capital) {
            this.attacked = ImageIO.read(new File("image/city/Capital_Attacked.png"));
        } else {
            this.attacked = ImageIO.read(new File("image/city/City_Attacked.png"));
        }
    }

    /**
     * Metoda wywoływana, gdy City zostanie zniszczone przez Monster - City jest
     * niszczone, gdy nie ma żadnego PowerSource oraz Civilian.
     */
    public void destroyCity() {
        this.destroyed = true;
        synchronized (TheGame.creatureGuard) {
            for (Civilian civ : civilians.keySet()) {
                if (civ.getFamilyTown().equals(this)) {
                    civ.setFamilyTown();
                }
                if (civ.getTarget().equals(this)) {
                    civ.setTarget(civ.getFamilyTown());
                }
            }
        }
        try {
            super.setIcon(ImageIO.read(new File("image/city/City_Destroyed.png")));
            this.setUnderAttack(false);
        } catch (IOException ex) {
            Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
        }
        synchronized (TheGame.creatureGuard) {
            for (Civilian civ : civilians.keySet()) {
                if (civ.getFamilyTown() == this) {
                    civ.setFamilyTown();
                }
            }
        }
    }

    /**
     * @return the name
     */
    public Cities getCityName() {
        return name;
    }

    /**
     * @return the population
     */
    public synchronized int getPopulation() {
        return population;
    }

    /**
     * Metoda aktualizująca populację w mieście.
     */
    public synchronized void updatePopulation() {
        synchronized (TheGame.creatureGuard) {
            population = 0;
            for (Civilian civ : civilians.keySet()) {
                if (!civ.isIsgoing() && civ.getSignpostFrom().equals(this)) {
                    population++;
                }
            }
        }
    }

    /**
     * @return the powerSource
     */
    public Map<Skills, PowerSource> getPowerSource() {
        return powerSource;
    }

    /**
     * Metoda zwracająca potencjał konkretnego źródła mocy.
     *
     * @param what PowerSource
     * @return the powerSource
     */
    public int getPowerSourcePotential(Skills what) {
        return getPowerSource().get(what).getPotential();
    }

    /**
     * @param what PowerSource
     * @param value to change the Potential
     */
    public void changePowerSourcePotential(Skills what, int value) {
        synchronized (TheGame.globalGuard) {
            this.getPowerSource().get(what).changePotential(value);
            if (this.getPowerSourcePotential(what) < 0) {
                this.powerSource.remove(what);
            }
        }
    }

    /**
     * Metoda aktualizująca wszystkie potencjały w mieście.
     */
    void updatePotential() {
        int ile = 0;
        for (Skills currentKey : powerSource.keySet()) {
            ile += powerSource.get(currentKey).getPotential();
        }
        for (Skills currentKey : powerSource.keySet()) {
            synchronized (this) {
                powerSource.get(currentKey).changePotential((double) population * powerSource.get(currentKey).getPotential() / ile);
            }
        }
    }

    /**
     * @return the capital
     */
    public boolean isCapital() {
        return capital;
    }

    /**
     * Funkcja wywoływana tylko wtedy, gdy aktualna stolica została zniszczona -
     * należy ustawić false dla starej stolicy, a true dla nowej.
     *
     * @param capital the capital to set
     */
    public void setCapital(boolean capital) {
        this.capital = capital;
    }

    /**
     * @return the underattack
     */
    public boolean isUnderAttack() {
        return underAttack;
    }

    /**
     * @param underattack the underattack to set 0 gdy miasto zostało ocalone. 1
     * gdy miasto zostało zaatakowane.
     */
    public void setUnderAttack(boolean underattack) {
        this.underAttack = underattack;
    }

    /**
     * @return the destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Metoda pozwalająca narysować City na mapie
     */
    @Override
    public void paintComponent(Graphics g) {
        if (underAttack) {
            g.drawImage(attacked, super.getX() - super.getIcon().getWidth() / 2 - 3, super.getY() - super.getIcon().getHeight(), null);
        }
        if (super.isHighlighted()) {
            g.drawImage(super.getIndicator(), super.getX() - super.getIndicator().getWidth() / 2, super.getY() - super.getIndicator().getHeight() + 6, null);
        }
        g.drawImage(super.getIcon(), super.getX() - super.getIcon().getWidth() / 2, super.getY() - super.getIcon().getHeight() + 3, null);
    }

    /**
     * @return the cities
     */
    public Map<Cities, City> getCities() {
        return cities;
    }
}
