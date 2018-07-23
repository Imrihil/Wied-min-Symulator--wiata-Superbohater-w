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

import static java.lang.Math.max;
import java.util.EnumMap;
import java.util.Map;

/**
 * Klasa abstrakcyjna SuperCreature przechowująca informacje o współczynnikach
 * postaci.
 *
 * @author Imrihil
 */
public abstract class SuperCreature extends Creature {

    /**
     * @return the monsters
     */
    public static Map<Monster, Thread> getMonsters() {
        return monsters;
    }

    /**
     * @return the heroes
     */
    public static Map<Hero, Thread> getHeroes() {
        return heroes;
    }

    /**
     * @return the civilians
     */
    public static Map<Civilian, Thread> getCivilians() {
        return civilians;
    }

    /**
     * Zdrowie, to ilość pozostałych punktów życia.
     */
    private double health;
    /**
     * Umiejętności postaci. Wytrzymałość, to wartość, o którą są zmniejszane
     * obrażenia, które otrzymuje w walce. Szybkość postaci ważna przy
     * rozstrzygnięciu, kto rozpoczyna walkę. Inteligencja postaci jest jedną z
     * trzech cech, które mogą posłużyć do ataku. Siła postaci jest drugą cechą,
     * która może posłużyć do ataku. Energia postaci jest trzecią cechą, która
     * może posłużyć do ataku. Umiejętność walki postaci jest najważniejszą
     * cechą przy ataku - jest to mnożnik zadawanych obrażeń.
     */
    private Map<Skills, Double> skills = new EnumMap<>(Skills.class);
    /**
     * Spis istniejących cywili.
     */
    private static volatile Map<Civilian, Thread> civilians;
    /**
     * Spis istniejących potworów.
     */
    private static volatile Map<Monster, Thread> monsters;
    /**
     * Spis istniejących bohaterów.
     */
    private static volatile Map<Hero, Thread> heroes;
    /**
     * Czas do następnego ataku SuperCreature.
     */
    private int fighting;
    /**
     * Przeciwnik SuperCreature.
     */
    private SuperCreature enemy;

    /**
     * Konstruktor klasy losuje wartości wszystkich pól obiektu.
     *
     * @param x współrzędna X pozycji początkowej.
     * @param y współrzędna Y pozycji początkowej.
     * @param name unikatowe imię/nazwa stworzenia
     * @param startCity nazwa miasta startowego.
     * @param health początkowa wartość zdrowia.
     * @param skills
     * @param civilians
     * @param monsters
     * @param heroes
     */
    public SuperCreature(int x, int y, String name, City startCity, double health, Map<Skills, Double> skills, Map<Civilian, Thread> civilians, Map<Monster, Thread> monsters, Map<Hero, Thread> heroes) {
        super(x, y, name, startCity);
        this.health = health;
        this.skills = skills;
        SuperCreature.civilians = civilians;
        SuperCreature.monsters = monsters;
        SuperCreature.heroes = heroes;
        this.fighting = 0;
        this.enemy = null;
    }

    /**
     * Metoda wywoływana, gdy postać zostanie zaatakowana.
     *
     * @param dmg ilość obrażeń, które zostały zadane postaci.
     */
    public void fight(double dmg) {
        this.health -= max(0.0, dmg - this.getSkill(Skills.resistance));
    }

    /**
     * Metoda abstrakcyjna wywoływana, gdy SuperCreature umrze (health spadnie
     * do 0 lub mniej). W zależności od podklasy, śmierć wygląda nieco inaczej.
     */
    public abstract void kill();

    /**
     * @return the health
     */
    public double getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * @param health the health to change
     */
    public void changeHealth(double health) {
        this.health += health;
    }

    /**
     * @param what Skill to get
     * @return the resistance
     */
    public double getSkill(Skills what) {
        return skills.get(what);
    }

    /**
     * @param what Skill to change
     * @param value the resistance to change
     */
    public void changeSkill(Skills what, double value) {
        this.skills.put(what, this.skills.get(what) + value);
    }

    @Override
    public void run() {

    }

    /**
     * @return the fighting
     */
    public int getFighting() {
        return fighting;
    }

    /**
     * @param fighting the fighting to set
     */
    public void setFighting(int fighting) {
        this.fighting = fighting;
    }

    /**
     * @param fighting the fighting to change
     */
    public void changeFighting(int fighting) {
        this.fighting += fighting;
    }

    /**
     * @return the enemy
     */
    public SuperCreature getEnemy() {
        return enemy;
    }

    /**
     * @param enemy the enemy to set
     */
    public void setEnemy(SuperCreature enemy) {
        this.enemy = enemy;
    }
}
