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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa Hero to bohaterowie, których gracz może szkolić w Capital i wysyłać
 * przeciwko Monster atakującym City.
 *
 * @author Imrihil
 */
public class Hero extends SuperCreature {

    /**
     * Moster, którego będzie ścigał Hero.
     */
    private Monster toChase;

    /**
     * Informacja, czy Hero musi czekać na swoją kolej walki z potworem.
     */
    private boolean wait;

    /**
     * Konstruktor Hero inicjalizuje podstawowe wartosci pól obiektu.
     *
     * @param x współrzędna X pozycji początkowej.
     * @param y współrzędna Y pozycji początkowej.
     * @param name unikatowe imię bohatera
     * @param startCity stolica świata
     * @param health początkowa wartość zdrowia.
     * @param skills umiejętności bohatera: resistance - początkowa wartość
     * wytrzymałości, speed - początkowa wartość szybkości, inteligence -
     * początkowa wartość inteligencji, strength - początkowa wartość siły,
     * energy - początkowa wartość energii, fight - początkowa wartość
     * umiejętności walki.
     * @param civilians
     * @param monsters
     * @param heroes
     * @param toChase
     * @param icon
     * @param image
     *
     * @throws java.io.IOException
     */
    public Hero(int x, int y, String name, City startCity, double health, Map<Skills, Double> skills, Map<Civilian, Thread> civilians, Map<Monster, Thread> monsters, Map<Hero, Thread> heroes, Monster toChase, BufferedImage icon, BufferedImage image) throws IOException {
        super(x, y, name, startCity, health, skills, civilians, monsters, heroes);
        this.toChase = toChase;
        this.wait = false;
        super.setIcon(icon);
        super.setImage(image);
    }

    /**
     * Metoda zabijająca Hero, kiedy przegra walkę z Monster.
     */
    @Override
    public void kill() {
        synchronized (TheGame.creatureGuard) {
            super.setFighting(0);
            super.setEnemy(null);
            this.setToChase(null);
            super.setAlive(false);
            if (TheGame.info.getInfo4() != null && TheGame.info.getInfo4().equals(this)) {
                if (TheGame.info.getShowInfo() == 4) {
                    TheGame.info.setShowInfo(0);
                }
                TheGame.info.setInfo4(null);
            }
            SuperCreature.getHeroes().remove(this);
        }
    }

    @Override
    public void run() {
        while (TheGame.playing && super.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hero.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (super.isIsgoing()) {
                synchronized (TheGame.creatureGuard) {
                    for (Monster mon : SuperCreature.getMonsters().keySet()) {
                        if (super.getDX() + super.getDirectionX() >= mon.getDX() - 4.0 && super.getDX() + super.getDirectionX() <= mon.getDX() + 4.0) {
                            if (super.getDY() + super.getDirectionY() >= mon.getDY() - 4.0 && super.getDY() + super.getDirectionY() <= mon.getDY() + 4.0) {
                                if (mon.getEnemy() == null) {
                                    super.setIsgoing(false);
                                    mon.setIsgoing(false);
                                    super.setEnemy(mon);
                                    mon.setEnemy(this);
                                    if (super.getSkill(Skills.speed) > mon.getSkill(Skills.speed)) {
                                        super.setFighting(1);
                                        mon.setFighting(2);
                                    } else if (super.getSkill(Skills.speed) == mon.getSkill(Skills.speed)) {
                                        super.setFighting(1);
                                        mon.setFighting(1);
                                    } else {
                                        super.setFighting(2);
                                        mon.setFighting(1);
                                    }
                                    wait = false;
                                } else {
                                    wait = true;
                                }
                            }
                        }
                    }
                }
                if (this.getToChase() != null) {
                    super.setTarget(getToChase().getTarget());
                } else {
                    super.setTarget(TheGame.capital());
                }
                if (!wait) {
                    go(2);
                }
                wait = false;
            } else if (super.getFighting() > 0) {
                if (super.getFighting() == 1) {
                    double attack = super.getSkill(Skills.strength);
                    if (super.getSkill(Skills.inteligence) > attack) {
                        attack = super.getSkill(Skills.inteligence);
                    }
                    if (super.getSkill(Skills.energy) > attack) {
                        attack = super.getSkill(Skills.energy);
                    }
                    super.getEnemy().fight(attack * super.getSkill(Skills.fight));
                    if (super.getEnemy().getHealth() <= 0.0) {
                        super.getEnemy().kill();
                        super.setEnemy(null);
                        this.setToChase(null);
                        super.setFighting(0);
                        super.setIsgoing(true);
                        TheGame.kills++;
                    } else {
                        super.changeFighting(2);
                    }
                } else {
                    super.changeFighting(-1);
                }
            } else {
                super.setIsgoing(true);
                super.setTarget(TheGame.capital());
            }
        }
    }

    /**
     * @return the toChase
     */
    public Monster getToChase() {
        return toChase;
    }

    /**
     * @param toChase the toChase to set
     */
    public void setToChase(Monster toChase) {
        this.toChase = toChase;
    }
}
