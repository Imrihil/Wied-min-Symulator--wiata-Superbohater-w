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
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Klasa Monster to potwory, które będą pojawiać się na mapie co pewien czas,
 * atakując City, zabijając Civilian na Road oraz walcząc z Hero, gdy dojdzie do
 * konfrontacji.
 *
 * @author Imrihil
 */
public class Monster extends SuperCreature {

    private int waitToHit;

    /**
     * Konstruktor Monster inicjalizuje podstawowe wartości pól obiektu.
     *
     * @param x współrzędna X pozycji początkowej.
     * @param y współrzędna Y pozycji początkowej.
     * @param name unikatowa nazwa stworzenia
     * @param startCity miasto, które Monster atakuje.
     * @param health początkowa wartość zdrowia.
     * @param skills umiejętności potwora: resistance - początkowa wartość
     * wytrzymałości, speed - początkowa wartość szybkości, inteligence -
     * początkowa wartość inteligencji, strength - początkowa wartość siły,
     * energy - początkowa wartość energii, fight - początkowa wartość
     * umiejętności walki.
     * @param civilians
     * @param monsters
     * @param heroes
     * @param icon
     * @param image
     *
     * @throws java.io.IOException
     */
    public Monster(int x, int y, String name, City startCity, double health, Map<Skills, Double> skills, Map<Civilian, Thread> civilians, Map<Monster, Thread> monsters, Map<Hero, Thread> heroes, BufferedImage icon, BufferedImage image) throws IOException {
        super(x, y, name, startCity, health, skills, civilians, monsters, heroes);
        if (icon == null) {
            super.setIcon(ImageIO.read(new File("image/monster/Goodybag.png")));
        } else {
            super.setIcon(icon);
        }
        if (image == null) {
            super.setImage(ImageIO.read(new File("image/monster/Goodybag.png")));
        } else {
            super.setImage(image);
        }
        this.waitToHit = 0;
    }

    /**
     * Metoda wywoływana, kiedy Monster znajduje się w City - zwiększa Skill
     * odpowiadający typowi PowerSource, z którego czerpie potential, a ponadto
     * może zabić Civilian.
     */
    public void improveSkill() {

    }

    /**
     * Metoda zabijająca Monster, kiedy przegra walkę z Hero.
     */
    @Override
    public void kill() {
        synchronized (TheGame.creatureGuard) {
            boolean change = true;
            if (super.getTarget().isUnderAttack()) {
                for (Monster mon : SuperCreature.getMonsters().keySet()) {
                    if (!mon.equals(this) && mon.getSignpostFrom() == mon.getSignpostTo() && mon.getSignpostFrom() == super.getTarget()) {
                        change = false;
                    }
                }
            }
            if (change) {
                super.getTarget().setUnderAttack(false);
            }
            super.setFighting(0);
            super.setEnemy(null);
            super.setAlive(false);
            for (Hero hero : SuperCreature.getHeroes().keySet()) {
                if (hero.getToChase() != null && hero.getToChase().equals(this)) {
                    hero.setToChase(null);
                }
            }
            if (TheGame.info.getInfo3() != null && TheGame.info.getInfo3().equals(this)) {
                if (TheGame.info.getShowInfo() == 3) {
                    TheGame.info.setShowInfo(0);
                }
                TheGame.info.setInfo3(null);
            }
            SuperCreature.getMonsters().remove(this);
        }
    }

    /**
     * Metoda losująca następne City, do którego uda się Monster po zniszczeniu
     * poprzedniego lub po pojawieniu się na mapie.
     */
    public void setTarget() {
        //String target = randomcity() //jeszcze nie wiem jak to zaimplementować
        //super.setTarget(target);
    }

    @Override
    public Hero getEnemy() {
        return (Hero) super.getEnemy();
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
                if (waitToHit == 0) {
                    synchronized (TheGame.creatureGuard) {
                        for (Civilian civ : SuperCreature.getCivilians().keySet()) {
                            if (civ.isIsgoing()) {
                                if (super.getDX() + super.getDirectionX() >= civ.getDX() - 3.0 && super.getDX() + super.getDirectionX() <= civ.getDX() + 3.0) {
                                    if (super.getDY() + super.getDirectionY() >= civ.getDY() - 3.0 && super.getDY() + super.getDirectionY() <= civ.getDY() + 3.0) {
                                        civ.delete();
                                        waitToHit = generator.nextInt(4) + 2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    go(1.5);
                    if (super.getSignpostFrom() == super.getTarget() && !super.getTarget().isDestroyed()) {
                        super.getTarget().setUnderAttack(true);
                    }
                } else {
                    waitToHit--;
                }

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
                        super.setFighting(0);
                        super.setIsgoing(true);
                    } else {
                        super.changeFighting(2);
                    }
                } else {
                    super.changeFighting(-1);
                }
            } else {
                if (super.getTarget().isDestroyed()) {
                    synchronized (TheGame.globalGuard) {
                        super.setIsgoing(true);
                        City nextCity = null;
                        for (Cities city : super.getTarget().getCities().keySet()) {
                            if (!super.getTarget().getCities().get(city).isDestroyed() && super.getTarget() != super.getTarget().getCities().get(city) && (nextCity == null || super.getTarget().dist(super.getTarget().getCities().get(city)) < super.getTarget().dist(nextCity))) {
                                nextCity = super.getTarget().getCities().get(city);
                            }
                        }
                        if (nextCity == null) {
                            this.kill();
                        } else {
                            super.setSignpostFrom(super.getTarget());
                            super.setSignpostTo(super.getTarget());
                            super.setTarget(nextCity);
                        }
                    }
                } else {
                    if (waitToHit == 0) {
                        synchronized (TheGame.globalGuard) {
                            if (generator.nextInt(100) > 0 && super.getTarget().getPowerSource().size() > 0) {
                                int rand = generator.nextInt(super.getTarget().getPowerSource().size());
                                for (Skills skill : super.getTarget().getPowerSource().keySet()) {
                                    if (rand-- == 0) {
                                        rand = generator.nextInt(40);
                                        super.getTarget().changePowerSourcePotential(skill, -rand);
                                        if (skill == Skills.fight) {
                                            super.changeSkill(skill, (double) rand / 500);
                                        } else {
                                            super.changeSkill(skill, (double) rand / 50);
                                        }
                                        break;
                                    }
                                }
                            } else {
                                synchronized (TheGame.creatureGuard) {
                                    for (Civilian civil : SuperCreature.getCivilians().keySet()) {
                                        if (civil.getSignpostFrom() == super.getTarget() && civil.getTarget() == super.getTarget() && !civil.isIsgoing()) {
                                            civil.delete();
                                            waitToHit = generator.nextInt(4) + 2;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (super.getTarget().getPowerSource().isEmpty()) {
                            int ile = 0;
                            synchronized (TheGame.creatureGuard) {
                                for (Civilian civil : SuperCreature.getCivilians().keySet()) {
                                    if (civil.getLastCity() == super.getTarget() && !civil.isIsgoing()) {
                                        ile++;
                                        break;
                                    }
                                }
                            }
                            if (ile == 0) {
                                this.getTarget().destroyCity();
                            }
                        }
                    } else {
                        waitToHit--;
                    }
                }
            }
        }
    }
}
