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
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Klasa wyświetlająca informacje na ekranie o konkretnym obiekcie.
 *
 * @author Imrihil
 */
public class InformPanel {

    /**
     * Informacja, czy wyświetlać informację i jeśli tak, to którą. 0 - nie
     * wyświetlaj. 1 - wyświatl informacje o Civilian (info1). 2 - wyświetl
     * informacje o City (info2). 3 - wyświatl informacje o Monster (info3). 4 -
     * wyświetl informacje o Hero (info4). 5 - wyświetl panel kontrolny City. 6
     * - wyświetle panel kontrolny Civilian. 7 - wyświetl panel kontrolny Hero.
     * 8 - wyświetle panel kontrolny Monster.
     */
    private int showInfo;
    /**
     * Przechowuje informacje o ostatnio wskazanym Civilian.
     */
    private Civilian info1;
    /**
     * Przechowuje informacje o ostatnio wskazanym Creature.
     */
    private City info2;
    /**
     * Przechowuje informacje o ostatnio wskazanym Monster.
     */
    private Monster info3;
    /**
     * Przechowuje informacje o ostatnio wskazanym Hero.
     */
    private Hero info4;
    /**
     * Informacja, na którym poziomie jest wybrane okno (jeśli jest
     * wielopoziomowe).
     */
    private int step;
    /**
     * Informacja o wyświetleniu ekranu zmiany celu dla Civilian.
     */
    protected static boolean civilTargetChanging = false;
    /**
     * Informacja o wyświetleniu ekranu zmiany celu dla Hero.
     */
    protected static boolean heroTargetChanging = false;
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
     * Spis istniejących miast.
     */
    private static volatile Map<Cities, City> cities;
    /**
     * Lista wyników.
     */
    private static List<Wynik> results;

    /**
     * Obrazek zwoju wyników.
     */
    private final BufferedImage scroll;
    /**
     * Obrazek karty Miast, Cywili, Bohaterów i Potworów.
     */
    private final BufferedImage card;
    /**
     * Ikona zaatakowanego miasta.
     */
    private final BufferedImage attacked;
    /**
     * Ikona stolicy.
     */
    private final BufferedImage capital;
    /**
     * Ikona zdrowia.
     */
    private final BufferedImage health;
    /**
     * Ikona wytrzymałości.
     */
    private final BufferedImage resistance;
    /**
     * Ikona szybkości.
     */
    private final BufferedImage speed;
    /**
     * Ikona inteligencji.
     */
    private final BufferedImage inteligence;
    /**
     * Ikona siły.
     */
    private final BufferedImage strength;
    /**
     * Ikona energii.
     */
    private final BufferedImage energy;
    /**
     * Ikona walki.
     */
    private final BufferedImage fight;
    /**
     * Przycisk wznowienia ruchu cywila.
     */
    private final BufferedImage start;
    /**
     * Przycisk zatrzymania cywila.
     */
    private final BufferedImage stop;
    /**
     * Przycisk usunięcia cywila.
     */
    private final BufferedImage delete;
    /**
     * Przycisk cywila.
     */
    private final BufferedImage civilImage;
    /**
     * Przycisk bohatera.
     */
    private final BufferedImage heroImage;
    /**
     * Przycisk miasta.
     */
    private final BufferedImage cityImage;
    /**
     * Przycisk potwora.
     */
    private final BufferedImage monsterImage;
    /**
     * Przycisk stworzenia cywila.
     */
    private final BufferedImage civilCreation;
    /**
     * Przycisk stworzenia bohatera.
     */
    private final BufferedImage heroCreation;
    /**
     * Ikona klepsydry odmierzającej czas.
     */
    private final BufferedImage hourglass;
    /**
     * Ikona zabitych potworów.
     */
    private final BufferedImage killedMonsters;
    /**
     * Ikona potencjału.
     */
    private final BufferedImage potentialImage;
    /**
     * Ikona cienia przycisku.
     */
    private final BufferedImage button;
    /**
     * Ikona przycisku 100px szerokości.
     */
    private final BufferedImage button100;
    /**
     * Ikona przycisku 160px szerokości.
     */
    private final BufferedImage button160;
    /**
     * Ikona przycisku rozpoczęcia gry na ekranie powitalnym.
     */
    private final BufferedImage welcomeStart;
    /**
     * Ikona przycisku tutoriala na ekranie powitalnym.
     */
    private final BufferedImage welcomeHowToPlay;
    /**
     * Ikona przycisku wyników na ekranie powitalnym.
     */
    private final BufferedImage welcomeResult;
    /**
     * Ikona przycisku powrotu w tutorialu.
     */
    private final BufferedImage welcomeBack;
    /**
     * Tablica obrazów tutoriali do wyświetlenia.
     */
    private final List<BufferedImage> tutorialScreens;

    /**
     * Konstruktor Panelu Informacyjnego.
     *
     * @param civilians
     * @param monsters
     * @param heroes
     * @param cities
     * @param results
     * @param scroll
     * @param card
     * @param attacked
     * @param capital
     * @param health
     * @param resistance
     * @param speed
     * @param inteligence
     * @param strength
     * @param energy
     * @param fight
     * @param start
     * @param stop
     * @param delete
     * @param civilImage
     * @param heroImage
     * @param cityImage
     * @param monsterImage
     * @param civilCreation
     * @param heroCreation
     * @param hourglass
     * @param killedMonsters
     * @param potentialImage
     * @param button
     * @param button80
     * @param button160
     * @param welcomeStart
     * @param welcomeHowToPlay
     * @param welcomeResult
     * @param welcomeBack
     * @param tutorialScreens
     */
    public InformPanel(Map<Civilian, Thread> civilians, Map<Monster, Thread> monsters, Map<Hero, Thread> heroes, Map<Cities, City> cities, List<Wynik> results, BufferedImage scroll, BufferedImage card, BufferedImage attacked, BufferedImage capital, BufferedImage health, BufferedImage resistance, BufferedImage speed, BufferedImage inteligence, BufferedImage strength, BufferedImage energy, BufferedImage fight, BufferedImage start, BufferedImage stop, BufferedImage delete, BufferedImage civilImage, BufferedImage heroImage, BufferedImage cityImage, BufferedImage monsterImage, BufferedImage civilCreation, BufferedImage heroCreation, BufferedImage hourglass, BufferedImage killedMonsters, BufferedImage potentialImage, BufferedImage button, BufferedImage button80, BufferedImage button160, BufferedImage welcomeStart, BufferedImage welcomeHowToPlay, BufferedImage welcomeResult, BufferedImage welcomeBack, List<BufferedImage> tutorialScreens) {
        InformPanel.civilians = civilians;
        InformPanel.monsters = monsters;
        InformPanel.heroes = heroes;
        InformPanel.cities = cities;
        InformPanel.results = results;
        this.showInfo = 0;
        this.info1 = null;
        this.info2 = null;
        this.info3 = null;
        this.info4 = null;
        this.scroll = scroll;
        this.card = card;
        this.attacked = attacked;
        this.capital = capital;
        this.health = health;
        this.resistance = resistance;
        this.speed = speed;
        this.inteligence = inteligence;
        this.strength = strength;
        this.energy = energy;
        this.fight = fight;
        this.start = start;
        this.stop = stop;
        this.delete = delete;
        this.civilImage = civilImage;
        this.heroImage = heroImage;
        this.cityImage = cityImage;
        this.monsterImage = monsterImage;
        this.civilCreation = civilCreation;
        this.heroCreation = heroCreation;
        this.hourglass = hourglass;
        this.killedMonsters = killedMonsters;
        this.potentialImage = potentialImage;
        this.step = 0;
        this.button = button;
        this.button100 = button80;
        this.button160 = button160;
        this.welcomeStart = welcomeStart;
        this.welcomeHowToPlay = welcomeHowToPlay;
        this.welcomeResult = welcomeResult;
        this.welcomeBack = welcomeBack;
        this.tutorialScreens = tutorialScreens;
    }

    /**
     * Wyświetla na ekranie informację o wybranym obiekcie, a także podstawowe
     * informacje o grze.
     *
     * @param graphics
     */
    public void draw(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        if (TheGame.welcome) {
            if (TheGame.tutorial > 0) {
                graphics.drawImage(tutorialScreens.get(TheGame.tutorial - 1), 0, 0, null);
                graphics.drawImage(welcomeBack, 370, 400, null);
            } else if (TheGame.showResults) {
                graphics.drawImage(scroll, 195, 50, null);
                graphics.setColor(Color.WHITE);
                int wspY = 75;
                for (int i = 0; i < results.size(); i++) {
                    graphics.drawString(Integer.toString(i + 1) + ". ", 215, 110 + i * 20);
                    graphics.drawString(results.get(i).getName(), 233, 110 + i * 20);
                    graphics.drawString(Long.toString(results.get(i).getTime()), 360, 110 + i * 20);
                    graphics.drawString(Integer.toString(results.get(i).getKills()), 400, 110 + i * 20);
                }
                graphics.setColor(Color.BLACK);
            } else {
                graphics.drawImage(welcomeStart, 195, 175, null);
                graphics.drawImage(welcomeHowToPlay, 195, 245, null);
                graphics.drawImage(welcomeResult, 195, 315, null);
            }
        } else {
            String string;
            synchronized (TheGame.globalGuard) {
                TheGame.allPotential = 0;
                TheGame.destroyedCities = 0;
                for (Cities city : cities.keySet()) {
                    for (Skills skill : cities.get(city).getPowerSource().keySet()) {
                        TheGame.allPotential += cities.get(city).getPowerSourcePotential(skill);
                    }
                    if (cities.get(city).isDestroyed()) {
                        TheGame.destroyedCities++;
                    }
                }
            }
            graphics.drawImage(hourglass, 20, 435, null);
            graphics.drawImage(killedMonsters, 60, 435, null);
            graphics.drawImage(button, 99, 434, null);
            graphics.drawImage(cityImage, 100, 435, null);
            graphics.drawImage(button, 139, 434, null);
            graphics.drawImage(civilImage, 140, 435, null);
            graphics.drawImage(button, 179, 434, null);
            graphics.drawImage(heroImage, 180, 435, null);
            graphics.drawImage(button, 219, 434, null);
            graphics.drawImage(monsterImage, 220, 435, null);
            graphics.drawImage(potentialImage, 260, 435, null);
            string = Long.toString(TheGame.resultTime);
            graphics.drawString(string, 33 - string.length() * 3, 455);
            string = Integer.toString(TheGame.kills);
            graphics.drawString(string, 73 - string.length() * 3, 455);
            string = Integer.toString(cities.size() - TheGame.destroyedCities);
            graphics.drawString(string, 113 - string.length() * 3, 455);
            string = Integer.toString(civilians.size());
            graphics.drawString(string, 153 - string.length() * 3, 455);
            string = Integer.toString(heroes.size());
            graphics.drawString(string, 193 - string.length() * 3, 455);
            string = Integer.toString(monsters.size());
            graphics.drawString(string, 233 - string.length() * 3, 455);
            string = Integer.toString(TheGame.allPotential);
            graphics.drawString(string, 287 - string.length() * 3, 455);
            if (showInfo > 0 && showInfo <= 4) {
                graphics.drawImage(card, 400, 50, null);
            }
            if (showInfo >= 5) {
                graphics.drawImage(scroll, 380, 50, null);
            }
            if (showInfo == 1) {
                graphics.drawString(getInfo1().getName(), 502 - getInfo1().getName().length() * 3, 95);
                graphics.drawImage(getInfo1().getImage(), 420, 115, null);
                graphics.drawString("Rodzinne: " + getInfo1().getFamilyTown().getCityName().name(), 435, 330);
                graphics.drawString("Ostatnie: " + getInfo1().getLastCity().getCityName().name(), 435, 345);
                graphics.drawString("Cel: " + getInfo1().getTarget().getCityName().name(), 435, 360);
                if (getInfo1().isIsgoing()) {
                    graphics.drawString("Podróżuje", 435, 375);
                } else {
                    graphics.drawString("W mieście", 435, 375);
                }
                graphics.drawImage(button, 449, 389, null);
                if (info1.isStart()) {
                    graphics.drawImage(stop, 450, 390, null);
                } else {
                    graphics.drawImage(start, 450, 390, null);
                }
                graphics.drawImage(button, 529, 389, null);
                graphics.drawImage(delete, 530, 390, null);
                graphics.drawImage(button, 489, 389, null);
                graphics.drawImage(cityImage, 490, 390, null);
                if (civilTargetChanging) {
                    int wspX = 410;
                    int wspY = 150;
                    int ile = 0;
                    for (Cities city : cities.keySet()) {
                        if (!cities.get(city).isDestroyed()) {
                            graphics.drawImage(button100, wspX + (ile / 7) * 100 - 1, wspY + (ile % 7) * 20 - 13, null);
                            graphics.drawString(city.toString(), wspX + 4 + (ile / 7) * 100, wspY + (ile % 7) * 20);
                            ile++;
                        }
                    }
                }
            } else if (showInfo == 2) {
                graphics.drawString(getInfo2().getCityName().name(), 502 - getInfo2().getCityName().name().length() * 3, 95);
                if (!info2.isDestroyed()) {
                    graphics.drawImage(info2.getImage(), 435, 115, null);
                    graphics.drawString("Populacja: " + Integer.toString(getInfo2().getPopulation()), 435, 330);
                }
                if (info2.isCapital()) {
                    graphics.drawImage(capital, 445, 83, null);
                }
                if (info2.isUnderAttack()) {
                    graphics.drawImage(attacked, 560, 83, null);
                }
                int wspX = 435;
                int wspY = 340;
                int ile = 0;
                synchronized (TheGame.globalGuard) {
                    for (Skills currentKey : info2.getPowerSource().keySet()) {
                        switch (currentKey) {
                            case resistance: {
                                graphics.drawImage(resistance, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.resistance)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            case speed: {
                                graphics.drawImage(speed, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.speed)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            case inteligence: {
                                graphics.drawImage(inteligence, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.inteligence)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            case strength: {
                                graphics.drawImage(strength, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.strength)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            case energy: {
                                graphics.drawImage(energy, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.energy)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            case fight: {
                                graphics.drawImage(fight, wspX + ile / 2 * 75, wspY + ile % 2 * 20, null);
                                graphics.drawString(Integer.toString(info2.getPowerSourcePotential(Skills.fight)), wspX + 20 + ile / 2 * 75, 12 + wspY + ile % 2 * 20);
                                break;
                            }
                            default: {
                                System.out.printf("Blad.\n");
                            }
                        }
                        ile++;
                    }
                }
            } else if (showInfo == 3) {
                graphics.drawString(getInfo3().getName(), 502 - getInfo3().getName().length() * 3, 95);
                graphics.drawImage(getInfo3().getImage(), 420, 115, null);
                graphics.drawImage(health, 470, 320, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getHealth()), 490, 330);
                graphics.drawImage(strength, 435, 340, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.strength)), 455, 350);
                graphics.drawImage(inteligence, 435, 360, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.inteligence)), 455, 370);
                graphics.drawImage(energy, 435, 380, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.energy)), 455, 390);
                graphics.drawImage(resistance, 520, 340, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.resistance)), 540, 350);
                graphics.drawImage(speed, 520, 360, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.speed)), 540, 370);
                graphics.drawImage(fight, 520, 380, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo3().getSkill(Skills.fight)), 540, 390);
            } else if (showInfo == 4) {
                graphics.drawString(getInfo4().getName(), 502 - getInfo4().getName().length() * 3, 95);
                graphics.drawImage(getInfo4().getImage(), 420, 115, null);
                graphics.drawImage(health, 470, 320, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getHealth()), 490, 330);
                graphics.drawImage(strength, 435, 340, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.strength)), 455, 350);
                graphics.drawImage(inteligence, 435, 360, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.inteligence)), 455, 370);
                graphics.drawImage(energy, 435, 380, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.energy)), 455, 390);
                graphics.drawImage(resistance, 520, 340, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.resistance)), 540, 350);
                graphics.drawImage(speed, 520, 360, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.speed)), 540, 370);
                graphics.drawImage(fight, 520, 380, null);
                graphics.drawString(new DecimalFormat("#.##").format(getInfo4().getSkill(Skills.fight)), 540, 390);
                graphics.drawImage(button, 489, 394, null);
                graphics.drawImage(monsterImage, 490, 395, null);
                if (heroTargetChanging) {
                    int wspX = 430;
                    int wspY = 120;
                    int ile = 0;
                    for (Monster monster : monsters.keySet()) {
                        graphics.drawImage(button160, wspX - 1, wspY + ile * 20 - 13, null);
                        graphics.drawString(monster.getName(), wspX + 4, wspY + ile * 20);
                        ile++;
                    }
                }
            } else if (showInfo >= 5) {
                int wspX = 405;
                int wspY = 160;
                int ile = 0;
                if (showInfo == 5) {
                    graphics.drawImage(cityImage, 490, 80, null);
                    for (Cities city : cities.keySet()) {
                        if (!cities.get(city).isDestroyed()) {
                            graphics.drawImage(button100, wspX + (ile / 7) * 100 - 1, wspY + (ile % 7) * 20 - 13, null);
                            graphics.drawString(city.toString(), wspX + 4 + (ile / 7) * 100, wspY + (ile % 7) * 20);
                            ile++;
                        }
                    }
                } else if (showInfo == 6 && getStep() == 0) {
                    graphics.drawImage(civilImage, 490, 80, null);
                    graphics.drawImage(button100, wspX + (ile / 7) * 100 - 1, wspY + (ile % 7) * 20 - 13, null);
                    graphics.drawString("Stwórz cywila", wspX + 4, wspY);
                } else if (showInfo == 6 && getStep() == 1) {
                    graphics.drawImage(civilImage, 490, 80, null);
                    graphics.drawString("Wybierz miasto:", wspX, wspY);
                    wspY += 20;
                    for (Cities city : cities.keySet()) {
                        if (!cities.get(city).isDestroyed()) {
                            graphics.drawImage(button100, wspX + (ile / 7) * 100 - 1, wspY + (ile % 7) * 20 - 13, null);
                            graphics.drawString(city.toString(), wspX + 4 + (ile / 7) * 100, wspY + (ile % 7) * 20);
                            ile++;
                        }
                    }
                } else if (showInfo == 7) {
                    graphics.drawImage(heroImage, 490, 80, null);
                    for (Hero hero : heroes.keySet()) {
                        graphics.drawImage(button160, wspX - 1, wspY + ile * 20 - 13, null);
                        graphics.drawString(hero.getName(), wspX + 4, wspY + ile * 20);
                        if (hero.getEnemy() != null && !hero.isIsgoing()) {
                            graphics.drawImage(strength, wspX + 170, wspY + ile * 20 - 13, null);
                        }
                        ile++;
                    }
                } else if (showInfo == 8) {
                    graphics.drawImage(monsterImage, 490, 80, null);
                    for (Monster monster : monsters.keySet()) {
                        graphics.drawImage(button160, wspX - 1, wspY + ile * 20 - 13, null);
                        graphics.drawString(monster.getName(), wspX + 4, wspY + ile * 20);
                        if (monster.getEnemy() != null && !monster.isIsgoing()) {
                            graphics.drawImage(strength, wspX + 170, wspY + ile * 20 - 13, null);
                        }
                        ile++;
                    }
                }
            }
        }
    }

    /**
     * @return the showInfo
     */
    public int getShowInfo() {
        return showInfo;
    }

    /**
     * @param showInfo the showInfo to set
     *
     * 0 - nie wyświetlaj info. 1 - wyświatl informacje o Civilian (info1). 2 -
     * wyświetl informacje o City (info2). 3 - wyświatl informacje o Monster
     * (info3). 4 - wyświetl informacje o Hero (info4). 5 - wyświetl panel
     * kontrolny City. 6 - wyświetle panel kontrolny Civilian. 7 - wyświetl
     * panel kontrolny Hero. 8 - wyświetle panel kontrolny Monster.
     */
    public void setShowInfo(int showInfo) {
        if (showInfo == 5) {
            clear();
        }
        this.showInfo = showInfo;
    }

    /**
     * @return the info1
     */
    public Civilian getInfo1() {
        return info1;
    }

    /**
     * @param info1 the info1 to set
     */
    public void setInfo1(Civilian info1) {
        clear();
        this.info1 = info1;
    }

    /**
     * @return the info2
     */
    public City getInfo2() {
        return info2;
    }

    /**
     * @param info2 the info2 to set
     */
    public void setInfo2(City info2) {
        clear();
        this.info2 = info2;
    }

    /**
     * @return the info3
     */
    public Monster getInfo3() {
        return info3;
    }

    /**
     * @param info3 the info3 to set
     */
    public void setInfo3(Monster info3) {
        clear();
        this.info3 = info3;
    }

    /**
     * @return the info4
     */
    public Hero getInfo4() {
        return info4;
    }

    /**
     * @param info4 the info4 to set
     */
    public void setInfo4(Hero info4) {
        clear();
        this.info4 = info4;
    }

    /**
     * @return the step
     */
    public int getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Metoda usuwająca wszystkie podświetlenia obiektów.
     */
    public void clear() {
        if (info1 != null) {
            info1.setHighlighted(false);
        }
        if (info2 != null) {
            info2.setHighlighted(false);
        }
        if (info3 != null) {
            info3.setHighlighted(false);
        }
        if (info4 != null) {
            info4.setHighlighted(false);
        }
    }
}
