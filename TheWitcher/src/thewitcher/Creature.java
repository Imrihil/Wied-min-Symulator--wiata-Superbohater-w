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

import static java.lang.Math.sqrt;

/**
 * Klasa abstrakcyjna Creature przechowująca informacje o nazwie, celu i
 * kierunku podróży istoty (Creature).
 *
 * @author Imrihil
 */
public abstract class Creature extends Point implements Runnable {

    /**
     * Imię (i nazwisko w przypadku Civilian) Creature.
     */
    private final String name;
    /**
     * City, do którego porusza się Creature. Jeśli ma pozostać w mieście, w
     * którym się znajduje, ono będzie target.
     */
    private City target;
    /**
     * Signpost, na ktorym ostatnio bylo Creature.
     */
    private Signpost signpostFrom;
    /**
     * Signpost, na ktorym nastepnie bedzie Creature.
     */
    private Signpost signpostTo;
    /**
     * Odległość, którą pokonuje Creature podczas jednego ruchu w osi X z
     * prędkością 1 - obliczana jest na skrzyżowaniach i w momencie wychodzenia
     * z miasta. Dopóki istota znajduje się w mieście, jest równa 0.
     */
    private double directionX;
    /**
     * Odległość, którą pokonuje Creature podczas jednego ruchu w osi Y z
     * prędkością 1 - obliczana jest na skrzyżowaniach i w momencie wychodzenia
     * z miasta. Dopóki istota znajduje się w mieście, jest równa 0.
     */
    private double directionY;
    /**
     * Pole określające, czy Creature porusza się, czy stoi w miejscu.
     */
    private boolean isgoing;
    /**
     * Informacja o tym, czy Creature wciąż żyje.
     */
    private boolean alive;

    /**
     * Konstruktor Creature ustawia początkowe wartości pól obiektu.
     *
     * @param x współrzędna X pozycji początkowej.
     * @param y współrzędna Y pozycji początkowej.
     * @param name unikatowe imię (i nazwisko).
     * @param target nazwa miasta docelowego.
     */
    public Creature(int x, int y, String name, City target) {
        super(x, y);
        this.name = name;
        this.target = target;
        this.directionX = 0.0;
        this.directionY = 0.0;
        this.signpostFrom = target;
        this.signpostTo = target;
        this.isgoing = false;
        this.alive = true;
    }

    /**
     * Metoda służąca do aktualizowania położenia postaci. Będzie wywoływana co
     * pewien określony czas. Jeśli Creature ma możliwość ruchu, jej położenie
     * zostanie zaktualizowane w zależności od directionX i directionY. Jeśli
     * wejdzie na Crossroad, zostanie wyznaczona dalsza droga i odpowiednio
     * zmienione direction.
     *
     * @param speed prędkość Creature.
     */
    public void go(double speed) {
        super.setX(super.getDX() + directionX);
        super.setY(super.getDY() + directionY);
        if (super.getX() >= signpostTo.getDX() - 1.0 && super.getX() <= signpostTo.getDX() + 1.0) {
            if (super.getY() >= signpostTo.getDY() - 1.0 && super.getY() <= signpostTo.getDY() + 1.0) {
                super.setX(signpostTo.getDX());
                super.setY(signpostTo.getDY());
                signpostFrom = signpostTo;
                if (signpostTo == target) {
                    isgoing = false;
                    directionX = 0.0;
                    directionY = 0.0;
                } else {
                    if (signpostTo.getRoads(target.getCityName()).getA() == signpostFrom) {
                        signpostTo = signpostTo.getRoads(target.getCityName()).getB();
                    } else {
                        signpostTo = signpostTo.getRoads(target.getCityName()).getA();
                    }
                    if (signpostTo.getX() == signpostFrom.getX()) {
                        directionX = 0.0;
                        directionY = speed;
                    } else if (signpostTo.getY() == signpostFrom.getY()) {
                        directionX = speed;
                        directionY = 0.0;
                    } else {
                        double tga = (signpostTo.getDY() - signpostFrom.getDY()) / (signpostTo.getX() - signpostFrom.getX());
                        directionX = sqrt(speed * speed / (1.0 + tga * tga));
                        directionY = sqrt(speed * speed - directionX * directionX);
                    }
                    if (signpostTo.getX() < signpostFrom.getX()) {
                        directionX = -directionX;
                    }
                    if (signpostTo.getY() < signpostFrom.getY()) {
                        directionY = -directionY;
                    }
                }
            }
        }
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the target
     */
    public City getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(City target) {
        this.target = target;
    }

    /**
     * @return the directionX
     */
    public double getDirectionX() {
        return directionX;
    }

    /**
     * @param directionX the directionX to set
     */
    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    /**
     * @return the directionY
     */
    public double getDirectionY() {
        return directionY;
    }

    /**
     * @param directionY the directionY to set
     */
    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    /**
     * @return the signpostFrom
     */
    public Signpost getSignpostFrom() {
        return signpostFrom;
    }

    /**
     * @param signpostFrom the signpostFrom to set
     */
    public void setSignpostFrom(Signpost signpostFrom) {
        this.signpostFrom = signpostFrom;
    }

    /**
     * @return the signpostTo
     */
    public Signpost getSignpostTo() {
        return signpostTo;
    }

    /**
     * @param signpostTo the signpostTo to set
     */
    public void setSignpostTo(Signpost signpostTo) {
        this.signpostTo = signpostTo;
    }

    /**
     * @return the isgoing
     */
    public boolean isIsgoing() {
        return isgoing;
    }

    /**
     * @param isgoing the isgoing to set
     */
    public void setIsgoing(boolean isgoing) {
        this.isgoing = isgoing;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
