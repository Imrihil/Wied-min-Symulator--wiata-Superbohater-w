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
import java.util.Map;
import javax.imageio.ImageIO;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa Civilian to cywile, którzy mieszkają w City, dzięki czemu zwiększają
 * potential w ich PowerSource, a co jakiś czas podrużują do innych City. Gracz
 * ma nad nimi pewną kontrolę - może im nakazać zatrzymać się, ruszyć lub
 * zmienić cel ich drogi. Ponadto może ich tworzyć i usuwać.
 *
 * @author Imrihil
 */
public class Civilian extends Creature {

    /**
     * Miasto rodzinne postaci, do którego zawsze będzie wracało po podróży.
     */
    private City familyTown;
    /**
     * Nazwa ostatnio odwiedzonego miasta.
     */
    private City lastCity;
    /**
     * Informacja, czy dany Cywil podróżuje, czy ma postój.
     */
    private boolean start;
    /**
     * Informacja o skrzyżowaniu, na którym znajduje się cywil.
     */
    private Signpost onSignpost;
    /**
     * Spis istniejących cywili.
     */
    private static volatile Map<Civilian, Thread> civilians;
    /**
     * Poświata cywila, na postoju.
     */
    private final BufferedImage image2;

    /**
     * Konstruktor Civilian ustawia wartości wszystkich pól obiektu.
     *
     * @param x współrzędna X pozycji początkowej.
     * @param y współrzędna Y pozycji początkowej.
     * @param name unikatowe imię i nazwisko.
     * @param familyTown nazwa familyTown.
     * @param civilians slownik wszystkich Civilian.
     * @throws java.io.IOException
     */
    public Civilian(int x, int y, String name, City familyTown, Map<Civilian, Thread> civilians) throws IOException {
        super(x, y, name, familyTown);
        this.familyTown = familyTown;
        this.lastCity = familyTown;
        this.onSignpost = null;
        super.setIcon(ImageIO.read(new File("image/civil/Civil.png")));
        super.setImage(ImageIO.read(new File("image/civil/img" + Integer.toString(new Random().nextInt(33)) + ".png")));
        Civilian.civilians = civilians;
        this.image2 = ImageIO.read(new File("image/civil/Civil_stop.png"));
        System.out.printf("Stworzyłem cywila o imieniu i nazwisku: %s w %s.\n", super.getName(), this.familyTown);
    }

    @Override
    public void go(double speed) {
        Random generator = new Random();
        if (start) {
            boolean freeway = true;
            synchronized (TheGame.creatureGuard) {
                for (Civilian civ : civilians.keySet()) {
                    if (!civ.equals(this) && civ.isIsgoing() && civ.getSignpostTo() == super.getSignpostTo() && civ.getSignpostFrom() == super.getSignpostFrom()) {
                        if (super.getDX() + super.getDirectionX() >= civ.getDX() - 5.0 && super.getDX() + super.getDirectionX() <= civ.getDX() + 5.0) {
                            if (super.getDY() + super.getDirectionY() >= civ.getDY() - 5.0 && super.getDY() + super.getDirectionY() <= civ.getDY() + 5.0) {
                                freeway = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (onSignpost == null && super.getX() + super.getDirectionX() >= super.getSignpostTo().getDX() - 5.0 && super.getX() + super.getDirectionX() <= super.getSignpostTo().getDX() + 5.0 && super.getY() + super.getDirectionY() >= super.getSignpostTo().getDY() - 5.0 && super.getY() + super.getDirectionY() <= super.getSignpostTo().getDY() + 5.0) {
                super.getSignpostTo().getSem().acquireUninterruptibly();
                onSignpost = super.getSignpostTo();
            }
            if (freeway) {
                synchronized (TheGame.globalGuard) {
                    super.go(1.0);
                    if (onSignpost != null && onSignpost == super.getTarget()) {
                        if (!super.getTarget().isDestroyed()) {
                            super.getTarget().updatePopulation();
                            lastCity = super.getTarget();
                            onSignpost.getSem().release();
                            onSignpost = null;
                        } else {
                            super.setTarget(familyTown);
                            super.setIsgoing(true);
                            start = true;
                        }
                    }
                }
            }
            if (onSignpost != null && (super.getX() + super.getDirectionX() < onSignpost.getDX() - 5.0 || super.getX() + super.getDirectionX() > onSignpost.getDX() + 5.0 || super.getY() + super.getDirectionY() < onSignpost.getDY() - 5.0 || super.getY() + super.getDirectionY() > onSignpost.getDY() + 5.0)) {
                onSignpost.getSem().release();
                onSignpost = null;
            }
            if (super.isIsgoing() && generator.nextInt(1000) < 1) {
                start = false;
            }
        } else {
            if (generator.nextInt(500) < 1) {
                start = true;
            }
        }
    }

    /**
     * @return the familyTown
     */
    public City getFamilyTown() {
        return familyTown;
    }

    /**
     * Metoda zmieniająca familyTown cywila na lastCity. Wywoływana, gdy
     * familyTown zostanie zniszczone, a cywila w nim nie było (w przeciwnym
     * wypadku by zginął).
     */
    public void setFamilyTown() {
        if (!this.getLastCity().isDestroyed()) {
            this.familyTown = this.getLastCity();
        } else {
            int random = new Random().nextInt(this.getFamilyTown().getCities().size() - TheGame.destroyedCities);
            for (Cities city : this.getFamilyTown().getCities().keySet()) {
                if (!this.getFamilyTown().getCities().get(city).isDestroyed()) {
                    if (random-- == 0) {
                        this.familyTown = this.getFamilyTown().getCities().get(city);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @return the lastCity
     */
    public City getLastCity() {
        return lastCity;
    }

    /**
     * @param lastCity the lastCity to set
     */
    public void setLastCity(City lastCity) {
        this.lastCity = lastCity;
    }

    /**
     * Metoda pozwalająca narysować Civilian na mapie.
     */
    @Override
    public void paintComponent(Graphics g) {
        int shiftX = 0;
        int shiftY = 0;
        if (super.getDirectionX() < 0) {
            shiftY = -3;
        }
        if (super.getDirectionX() > 0) {
            shiftY = 3;
        }
        if (super.getDirectionY() < 0) {
            shiftX = 3;
        }
        if (super.getDirectionY() > 0) {
            shiftX = -3;
        }
        if (!this.isStart()) {
            g.drawImage(image2, super.getX() - image2.getWidth() / 2 + shiftX, super.getY() - image2.getHeight() / 2 + shiftY, null);
        }
        if (super.isHighlighted()) {
            g.drawImage(super.getIndicator(), super.getX() - super.getIndicator().getWidth() / 2 + shiftX, super.getY() - super.getIndicator().getHeight() / 2 + shiftY, null);
        }
        g.drawImage(super.getIcon(), super.getX() - super.getIcon().getWidth() / 2 + shiftX, super.getY() - super.getIcon().getHeight() / 2 + shiftY, null);
    }

    @Override
    public void run() {
        Random generator = new Random();
        while (TheGame.playing && super.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Civilian.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (super.isIsgoing()) {
                go(1.0);
            } else {
                if (generator.nextInt(1000) < 1) {
                    lastCity.getSem().acquireUninterruptibly();
                    onSignpost = lastCity;
                    super.setIsgoing(true);
                    start = true;
                    lastCity.updatePopulation();
                    if (lastCity == familyTown) {
                        synchronized (TheGame.globalGuard) {
                            int random = generator.nextInt(this.getFamilyTown().getCities().size() - TheGame.destroyedCities);
                            for (Cities city : familyTown.getCities().keySet()) {
                                if (!this.getFamilyTown().getCities().get(city).isDestroyed()) {
                                    if (random-- == 0) {
                                        super.setTarget(familyTown.getCities().get(city));
                                        break;
                                    }
                                }
                            }
                            if (super.getTarget().equals(lastCity)) {
                                super.setIsgoing(false);
                                lastCity.updatePopulation();
                                onSignpost.getSem().release();
                                onSignpost = null;
                            }
                        }
                    } else {
                        super.setTarget(familyTown);
                    }
                    super.setSignpostFrom(lastCity);
                    super.setSignpostTo(lastCity);
                }
            }
        }
    }

    /**
     * @return the start
     */
    public boolean isStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * Metoda usuwająca cywila z gry.
     */
    public void delete() {
        if (onSignpost != null) {
            onSignpost.getSem().release();
            onSignpost = null;
        }
        super.setAlive(false);
        City city = null;
        if (!super.isIsgoing()) {
            city = this.getLastCity();
        }
        if (TheGame.info.getInfo1() != null && TheGame.info.getInfo1().equals(this)) {
            if (TheGame.info.getShowInfo() == 1) {
                TheGame.info.setShowInfo(0);
            }
            TheGame.info.setInfo1(null);
        }
        synchronized (TheGame.creatureGuard) {
            civilians.remove(this);
        }
        if (city != null) {
            city.updatePopulation();
        }
    }
}
