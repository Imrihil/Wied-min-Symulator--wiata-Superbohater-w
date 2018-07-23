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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Główna klasa, która zarządza całą rozgrywką.
 *
 * @author Imrihil
 */
public class TheGame {

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
     * Spis istniejących skrzyżowań.
     */
    private static List<Signpost> crossroads;
    /**
     * Spis istniejących dróg.
     */
    private static List<Road> roads;
    /**
     * Lista dróg wychodzących z poszczególnych skrzyżowań.
     */
    private static Map<Signpost, List<Road>> roadsOfSignpost;
    /**
     * Suparyczny potencjał wszystkich źródeł mocy na świecie.
     */
    protected static volatile int allPotential;
    /**
     * Ilość zniszczonych miast.
     */
    protected static volatile int destroyedCities;
    /**
     * Ilość zabitych potworów podczas gry.
     */
    protected static volatile int kills;
    /**
     * Ogólny Monitor świata.
     */
    protected static Object globalGuard;
    /**
     * Monitor stworzeń.
     */
    protected static Object creatureGuard;
    /**
     * Informuje o tym, czy gra jest w fazie przywitania.
     */
    protected static boolean welcome;
    /**
     * Informuje o tym, czy gra jest w fazie rozgrywki.
     */
    protected static boolean playing;
    /**
     * Informuje o tym, czy gracz podał już swoje dane.
     */
    protected static boolean podanoDane;
    /**
     * Informuje o tym, czy gra jest w fazie zakończenia.
     */
    protected static boolean zakoncz;
    /**
     * Informuje o tym, czy wyświetlać wyniki.
     */
    protected static boolean showResults;
    /**
     * Nazwa gracza.
     */
    protected static String playerName;
    /**
     * Czas gry.
     */
    protected static long resultTime;
    /**
     * Poziom zaawansowania tutorialu (0 - tutorial wyłączony).
     */
    protected static int tutorial;
    /**
     * Poziom trudności gry (im wyższy, tym częściej pojawiają się potwory i
     * jest ich więcej).
     */
    protected static int hardlvl;
    /**
     * Panel informacyjny - jest odpowiedzialny za rysowanie grafik.
     */
    protected static volatile InformPanel info;
    /**
     * Okno, na którym wyświetlane są wszystkie grafiki.
     */
    private static WindowJFrame window;
    /**
     * Licznik, jak dużo czasu pozostało do możliwości stworzenia następnego
     * Civilian lub Hero.
     */
    private static int timeToNextCreation;
    /**
     * Lista możliwych imion dla Civilian.
     */
    private static List<String> firstNames;
    /**
     * Lista możliwych nazwisk dla Civilian.
     */
    private static List<String> secondNames;
    /**
     * Lista możliwych nazw Hero.
     */
    private static List<String> heroNames;
    /**
     * Lista możliwych nazw Monster.
     */
    private static List<String> monsterNames;
    /**
     * Wskaźnik listy imion dla Civilian.
     */
    private static int firstNamesIndicator;
    /**
     * Wskaźnik listy nazwisk dla Civilian.
     */
    private static int secondNamesIndicator;
    /**
     * Wskaźnik listy nazw dla Hero.
     */
    private static int heroNamesIndicator;
    /**
     * Wskaźnik listy nazw dla Monster.
     */
    private static int monsterNamesIndicator;
    /**
     * Lista wyników.
     */
    private static List<Wynik> results;
    /**
     * Licznik wyświetlania kreski zachęty podczas podawania imienia.
     */
    private int line;

    /**
     * Konstruktor klasy TheGame.
     *
     * @param results lista wyników.
     */
    public TheGame(List<Wynik> results) {
        civilians = new HashMap<>();
        monsters = new HashMap<>();
        heroes = new HashMap<>();
        cities = new EnumMap<>(Cities.class);
        crossroads = new ArrayList<>();
        roads = new ArrayList<>();
        roadsOfSignpost = new HashMap<>();
        allPotential = 0;
        destroyedCities = 0;
        kills = 0;
        globalGuard = new Object();
        creatureGuard = new Object();
        welcome = true;
        playing = false;
        podanoDane = false;
        zakoncz = false;
        playerName = "";
        resultTime = 0;
        tutorial = 0;
        showResults = false;
        hardlvl = 1;
        timeToNextCreation = 5;
        firstNames = new ArrayList<>();
        secondNames = new ArrayList<>();
        heroNames = new ArrayList<>();
        monsterNames = new ArrayList<>();
        firstNamesIndicator = 0;
        secondNamesIndicator = 0;
        heroNamesIndicator = 0;
        monsterNamesIndicator = 0;
        TheGame.results = results;
        line = 0;

        try {
            playing = true;
            back = new Background();

            buffer = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            graphics = buffer.createGraphics();
            graphics.setFont(new Font("Verdana", Font.BOLD, 12));

            List<BufferedImage> tutorialScreens = new ArrayList<>();
            for(int i = 1; i < 13; i++) {
                tutorialScreens.add(ImageIO.read(new File("image/tutorial/Screen" + Integer.toString(i) + ".png")));
            }
            
            info = new InformPanel(civilians, monsters, heroes, cities, results, ImageIO.read(new File("image/map/Info.png")), ImageIO.read(new File("image/map/Card.png")), ImageIO.read(new File("image/city/Attacked.png")), ImageIO.read(new File("image/city/Capital.png")), ImageIO.read(new File("image/skill/Health.png")), ImageIO.read(new File("image/skill/Resistance.png")), ImageIO.read(new File("image/skill/Speed.png")), ImageIO.read(new File("image/skill/Inteligence.png")), ImageIO.read(new File("image/skill/Strength.png")), ImageIO.read(new File("image/skill/Energy.png")), ImageIO.read(new File("image/skill/Fight.png")), ImageIO.read(new File("image/civil/Start.png")), ImageIO.read(new File("image/civil/Stop.png")), ImageIO.read(new File("image/civil/Delete.png")), ImageIO.read(new File("image/icon/CivilIcon.png")), ImageIO.read(new File("image/icon/HeroIcon.png")), ImageIO.read(new File("image/icon/City.png")), ImageIO.read(new File("image/icon/MonsterIcon.png")), ImageIO.read(new File("image/civil/CivilCreation.png")), ImageIO.read(new File("image/hero/HeroCreation.png")), ImageIO.read(new File("image/icon/Hourglass.png")), ImageIO.read(new File("image/icon/Killed_Monster.png")), ImageIO.read(new File("image/icon/Crystal.png")), ImageIO.read(new File("image/ButtonShadow.png")), ImageIO.read(new File("image/map/RectangleButton100.png")), ImageIO.read(new File("image/map/RectangleButton160.png")), ImageIO.read(new File("image/map/WelcomePlay.png")), ImageIO.read(new File("image/map/WelcomeHowToPlay.png")), ImageIO.read(new File("image/map/WelcomeResults.png")), ImageIO.read(new File("image/map/WelcomeBack.png")), tutorialScreens);

            window = new WindowJFrame(civilians, monsters, heroes, cities, info);
            window.setTitle("The Witcher");

            firstNames = readFile("file/ManNames.xml");
            secondNames = readFile("file/SecondNames.xml");
            heroNames = readFile("file/HeroNames.xml");
            monsterNames = readFile("file/MonsterNames.xml");
            Collections.shuffle(firstNames);
            Collections.shuffle(secondNames);
            Collections.shuffle(heroNames);
            Collections.shuffle(monsterNames);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda tworząca nowego potwora.
     *
     * @throws IOException
     */
    public static void newMonster() throws IOException {
        Random generator = new Random();
        if (monsters.size() < 10 * hardlvl) {
            if (generator.nextInt(monsters.size() + 4) < hardlvl) {
                if (generator.nextInt(monsters.size() + 4) < hardlvl) {
                    int random = generator.nextInt(cities.size() - 1);
                    City toAttack = cities.get(Cities.Blaviken);
                    for (Cities city : cities.keySet()) {
                        if (city != Cities.Vizima && random-- == 0) {
                            toAttack = cities.get(city);
                            break;
                        }
                    }
                    boolean isnew = false;
                    String name = "";
                    while (!isnew) {
                        name = monsterNames.get(monsterNamesIndicator++);
                        isnew = true;
                        for (Monster mon : monsters.keySet()) {
                            if (mon.getName().equals(name)) {
                                isnew = false;
                                break;
                            }
                        }
                        if (monsterNamesIndicator == monsterNames.size()) {
                            monsterNamesIndicator = 0;
                            Collections.shuffle(monsterNames);
                        }
                    }
                    BufferedImage icon;
                    BufferedImage image;
                    try {
                        icon = ImageIO.read(new File("image/monster/" + name + "/icon.png"));
                    } catch (IOException ex) {
                        icon = null;
                    }
                    try {
                        image = ImageIO.read(new File("image/monster/" + name + "/image.png"));
                    } catch (IOException ex) {
                        image = null;
                    }
                    double health = (double) (generator.nextInt(40000) + 20000);
                    Map<Skills, Double> skills = new EnumMap<>(Skills.class);
                    skills.put(Skills.resistance, (double) (generator.nextInt(4000) + 2000) / 50);
                    skills.put(Skills.speed, (double) (generator.nextInt(4000) + 2000) / 50);
                    skills.put(Skills.inteligence, (double) (generator.nextInt(4000) + 2000) / 50);
                    skills.put(Skills.strength, (double) (generator.nextInt(4000) + 2000) / 50);
                    skills.put(Skills.energy, (double) (generator.nextInt(4000) + 2000) / 50);
                    skills.put(Skills.fight, (double) (generator.nextInt(4000) + 2000) / 500);
                    Monster monster = new Monster(toAttack.getX(), toAttack.getY(), name, toAttack, health, skills, civilians, monsters, heroes, icon, image);
                    monsters.put(monster, new Thread(monster));
                    monsters.get(monster).start();
                    if (!toAttack.isDestroyed()) {
                        toAttack.setUnderAttack(true);
                    }
                }
            }
        }
    }

    /**
     * Metoda tworząca nowego Bohatera.
     *
     * @param toChase Monster, którego ma ścigać Hero.
     * @throws IOException
     */
    public static void newHero(Monster toChase) throws IOException {
        synchronized (creatureGuard) {
            if (heroes.size() < cities.size() - TheGame.destroyedCities && timeToNextCreation == 0) {
                timeToNextCreation = 10;
                Random generator = new Random();
                int random;
                City capital = cities.get(Cities.Vizima);
                for (Cities city : cities.keySet()) {
                    if (cities.get(city).isCapital()) {
                        capital = cities.get(city);
                        if (capital.isDestroyed()) {
                            return;
                        }
                        break;
                    }
                }
                boolean isnew = false;
                String name = "";
                while (!isnew) {
                    name = heroNames.get(heroNamesIndicator++);
                    isnew = true;
                    for (Hero hero : heroes.keySet()) {
                        if (hero.getName().equals(name)) {
                            isnew = false;
                            break;
                        }
                    }
                    if (heroNamesIndicator == heroNames.size()) {
                        heroNamesIndicator = 0;
                        Collections.shuffle(heroNames);
                    }
                }
                BufferedImage icon = ImageIO.read(new File("image/hero/Hero.png"));
                BufferedImage image;
                try {
                    image = ImageIO.read(new File("image/hero/" + name.substring(0, 4) + "/" + name.substring(5, name.length()) + ".png"));
                } catch (IOException ex) {
                    image = ImageIO.read(new File("image/hero/" + name.substring(0, 4) + "/img" + Integer.toString(new Random().nextInt(4)) + ".png"));
                }
                name = name.substring(5, name.length());
                double health = (double) (generator.nextInt(40000) + 20000);
                Map<Skills, Double> skills = new EnumMap<>(Skills.class);
                skills.put(Skills.resistance, (double) (generator.nextInt(4000) + 2000) / 50);
                skills.put(Skills.speed, (double) (generator.nextInt(4000) + 2000) / 50);
                skills.put(Skills.inteligence, (double) (generator.nextInt(4000) + 2000) / 50);
                skills.put(Skills.strength, (double) (generator.nextInt(4000) + 2000) / 50);
                skills.put(Skills.energy, (double) (generator.nextInt(4000) + 2000) / 50);
                skills.put(Skills.fight, (double) (generator.nextInt(4000) + 2000) / 500);
                Hero hero = new Hero(capital.getX(), capital.getY(), name, capital, health, skills, civilians, monsters, heroes, toChase, icon, image);
                hero.setIsgoing(true);
                heroes.put(hero, new Thread(hero));
                heroes.get(hero).start();
            } else {
                timeToNextCreation = 10;
            }
        }
    }

    /**
     * Metoda tworząca nowego Cywila.
     *
     * @param where miasto rodzinne cywila.
     */
    public static void newCivilian(City where) {
        synchronized (creatureGuard) {
            if (!where.isDestroyed() && timeToNextCreation == 0) {
                timeToNextCreation = 10;
                try {
                    String name = firstNames.get(firstNamesIndicator++) + " " + secondNames.get(secondNamesIndicator++);
                    if (firstNamesIndicator == firstNames.size()) {
                        firstNamesIndicator = 0;
                    }
                    if (secondNamesIndicator == secondNames.size()) {
                        secondNamesIndicator = 0;
                    }
                    Civilian civil = new Civilian(where.getX(), where.getY(), name, where, civilians);
                    civilians.put(civil, new Thread(civil));
                    civilians.get(civil).start();
                    where.updatePopulation();
                } catch (IOException ex) {
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                timeToNextCreation = 10;
            }
        }
    }

    /**
     * Metoda znajdująca stolicę świata.
     *
     * @return stolica.
     */
    public static City capital() {
        City capital = cities.get(Cities.Vizima);
        for (Cities city : cities.keySet()) {
            if (cities.get(city).isCapital()) {
                return cities.get(city);
            }
        }
        return capital;
    }

    /**
     * Podstawa do rysowania (mapa lub tło).
     */
    private Background back;
    /**
     * Obraz, na którym wszystko będzie rysowane, by zapewnić podwójne
     * buforowanie.
     */
    private BufferedImage buffer;
    /**
     * Grafika, na której będzie pamiętany narysowany obraz.
     */
    private Graphics graphics;

    /**
     * Metoda sterująca całą grą.
     *
     * @return wynik gry.
     */
    public Wynik play() {

        try {
            /*
             * Wyswietlanie i gra.
             */
            while (welcome) {
                Thread.sleep(100);
                back.paintComponent(graphics);
                info.draw(graphics);
                window.getGraphics().drawImage(buffer, 0, 0, null);
            }
            initializeWorld();

            stworzSystemDrogowskazow(cities, roadsOfSignpost);
            back.setBackground(ImageIO.read(new File("image/map/Map.png")));
            int upTime = 0;
            while (playing) {
                Thread.sleep(100);
                if (timeToNextCreation > 0) {
                    timeToNextCreation--;
                }

                newMonster();

                draw();
                window.getGraphics().drawImage(buffer, 0, 0, null);

                playing = alive();
                upTime++;
                if (upTime == 10) {
                    resultTime++;
                    upTime = 0;
                }
            }
            info.setShowInfo(0);
            playerName = "";
            while (!podanoDane) {
                Thread.sleep(100);
                draw();
                drawResults(results, playerName);
                window.getGraphics().drawImage(buffer, 0, 0, null);
            }
            back.setBackground(ImageIO.read(new File("image/map/GoodBye.png")));
            while (!zakoncz) {
                Thread.sleep(100);
                drawEnd();
                window.getGraphics().drawImage(buffer, 0, 0, null);
            }
            window.dispose();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Wynik(playerName, resultTime, kills);
    }

    /**
     * Metoda inicjalizująca cały świat gry.
     */
    private void initializeWorld() {
        try {
            synchronized (creatureGuard) {
                int x, y, population;
                Map<Skills, PowerSource> powerSource;
                Cities cityName;
                /*
                 * Inicjalizacja miast i cywilów.
                 */
                //Vizima - stolica Temerii i swiata gry.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.resistance, new PowerSource(Skills.resistance, 1000));
                powerSource.put(Skills.speed, new PowerSource(Skills.speed, 2000));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 3000));
                City city = new City(x = 150, y = 275, cityName = Cities.Vizima, population = 10, powerSource, true, cities, civilians, ImageIO.read(new File("image/city/City_Capital.png")), ImageIO.read(new File("image/city/HerbTemeria.png")));
                cities.put(Cities.Vizima, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Novigrad - najwieksze miasto portowe na swiecie.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.inteligence, new PowerSource(Skills.inteligence, 2000));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1500));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 1500));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1000));
                city = new City(x = 75, y = 200, cityName = Cities.Novigrad, population = 10, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Redania.png")), ImageIO.read(new File("image/city/HerbRedania.png")));
                cities.put(Cities.Novigrad, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Cintra - stolica Cintry, miasto portowe.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.resistance, new PowerSource(Skills.resistance, 1800));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1000));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 800));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1800));
                city = new City(x = 25, y = 425, cityName = Cities.Cintra, population = 9, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Cintra.png")), ImageIO.read(new File("image/city/HerbCintra.png")));
                cities.put(Cities.Cintra, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Tretogor - stolica Redanii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.inteligence, new PowerSource(Skills.inteligence, 1600));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1600));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1600));
                city = new City(x = 150, y = 200, cityName = Cities.Tretogor, population = 8, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Redania.png")), ImageIO.read(new File("image/city/HerbRedania.png")));
                cities.put(Cities.Tretogor, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Ard Carraigh - stolica Kaedwen.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.speed, new PowerSource(Skills.speed, 1800));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 3000));
                city = new City(x = 325, y = 100, cityName = Cities.Ard_Carraigh, population = 8, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Kaedwen.png")), ImageIO.read(new File("image/city/HerbKaedwen.png")));
                cities.put(Cities.Ard_Carraigh, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Vengerberg - stolica Aedirn.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.resistance, new PowerSource(Skills.resistance, 2400));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 2400));
                city = new City(x = 325, y = 275, cityName = Cities.Vengerberg, population = 8, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Aedirn.png")), ImageIO.read(new File("image/city/HerbAedirn.png")));
                cities.put(Cities.Vengerberg, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Maribor - drugie pod wzgledem wielkosci miasto w Temerii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.resistance, new PowerSource(Skills.resistance, 3000));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1200));
                city = new City(x = 175, y = 325, cityName = Cities.Maribor, population = 7, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Temeria.png")), ImageIO.read(new File("image/city/HerbTemeria.png")));
                cities.put(Cities.Maribor, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Lyria - stolica Lyrii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.inteligence, new PowerSource(Skills.inteligence, 1200));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1500));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1500));
                city = new City(x = 375, y = 350, cityName = Cities.Lyria, population = 7, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Lyria.png")), ImageIO.read(new File("image/city/HerbLyria.png")));
                cities.put(Cities.Lyria, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Gors Valen - miasto portowe w Temerii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.inteligence, new PowerSource(Skills.inteligence, 1800));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 1800));
                city = new City(x = 75, y = 250, cityName = Cities.Gors_Valen, population = 6, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Temeria.png")), ImageIO.read(new File("image/city/HerbTemeria.png")));
                cities.put(Cities.Gors_Valen, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Rivia - miasto w Lyrii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.resistance, new PowerSource(Skills.resistance, 900));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 900));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1800));
                city = new City(x = 300, y = 375, cityName = Cities.Rivia, population = 6, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Lyria.png")), ImageIO.read(new File("image/city/HerbLyria.png")));
                cities.put(Cities.Rivia, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Brugge - stolica Brugge - lenna Temerii.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.speed, new PowerSource(Skills.speed, 1200));
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1200));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 1200));
                city = new City(x = 125, y = 375, cityName = Cities.Brugge, population = 6, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Temeria.png")), ImageIO.read(new File("image/city/HerbTemeria.png")));
                cities.put(Cities.Brugge, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Vergen - miasto lezace w Dolinie Pontaru w Gornym Aedirn.
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1000));
                powerSource.put(Skills.energy, new PowerSource(Skills.energy, 2000));
                city = new City(x = 325, y = 200, cityName = Cities.Vergen, population = 5, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Aedirn.png")), ImageIO.read(new File("image/city/HerbAedirn.png")));
                cities.put(Cities.Vergen, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();
                //Blaviken - miasto portowe w Redanii
                powerSource = new EnumMap<>(Skills.class);
                powerSource.put(Skills.strength, new PowerSource(Skills.strength, 1200));
                powerSource.put(Skills.fight, new PowerSource(Skills.fight, 1200));
                city = new City(x = 100, y = 100, cityName = Cities.Blaviken, population = 4, powerSource, false, cities, civilians, ImageIO.read(new File("image/city/City_Redania.png")), ImageIO.read(new File("image/city/HerbRedania.png")));
                cities.put(Cities.Blaviken, city);
                for (int i = 0; i < population; i++) {
                    newCivilian(city);
                    timeToNextCreation = 0;
                }
                System.out.println();

                /*
                 * Inicjalizacja skrzyzowan.
                 */
                crossroads.add(new Crossroad(100, 125));
                crossroads.add(new Crossroad(300, 125));
                crossroads.add(new Crossroad(100, 150));
                crossroads.add(new Crossroad(25, 200));
                crossroads.add(new Crossroad(175, 225));
                crossroads.add(new Crossroad(225, 200));
                crossroads.add(new Crossroad(300, 200));
                crossroads.add(new Crossroad(75, 225));
                crossroads.add(new Crossroad(25, 250));
                crossroads.add(new Crossroad(175, 250));
                crossroads.add(new Crossroad(225, 250));
                crossroads.add(new Crossroad(325, 250));
                crossroads.add(new Crossroad(150, 325));
                crossroads.add(new Crossroad(200, 275));
                crossroads.add(new Crossroad(250, 300));
                crossroads.add(new Crossroad(325, 300));
                crossroads.add(new Crossroad(175, 375));
                crossroads.add(new Crossroad(325, 350));
                crossroads.add(new Crossroad(100, 425));
                crossroads.add(new Crossroad(300, 425));
                System.out.println();

                /*
                 * Inicjalizacja drog.
                 */
                roads.add(new Road(cities.get(Cities.Blaviken), crossroads.get(0)));
                roads.add(new Road(cities.get(Cities.Blaviken), crossroads.get(3)));
                roads.add(new Road(crossroads.get(0), crossroads.get(2)));
                roads.add(new Road(cities.get(Cities.Ard_Carraigh), crossroads.get(0)));
                roads.add(new Road(cities.get(Cities.Ard_Carraigh), crossroads.get(1)));
                roads.add(new Road(crossroads.get(1), crossroads.get(5)));
                roads.add(new Road(crossroads.get(1), crossroads.get(6)));
                roads.add(new Road(cities.get(Cities.Novigrad), crossroads.get(2)));
                roads.add(new Road(cities.get(Cities.Novigrad), crossroads.get(3)));
                roads.add(new Road(cities.get(Cities.Novigrad), crossroads.get(7)));
                roads.add(new Road(cities.get(Cities.Tretogor), crossroads.get(2)));
                roads.add(new Road(cities.get(Cities.Tretogor), crossroads.get(4)));
                roads.add(new Road(crossroads.get(4), crossroads.get(5)));
                roads.add(new Road(crossroads.get(4), crossroads.get(9)));
                roads.add(new Road(crossroads.get(5), crossroads.get(6)));
                roads.add(new Road(cities.get(Cities.Vergen), crossroads.get(6)));
                roads.add(new Road(cities.get(Cities.Vergen), crossroads.get(11)));
                roads.add(new Road(crossroads.get(9), crossroads.get(10)));
                roads.add(new Road(crossroads.get(10), crossroads.get(11)));
                roads.add(new Road(crossroads.get(3), crossroads.get(8)));
                roads.add(new Road(cities.get(Cities.Gors_Valen), crossroads.get(7)));
                roads.add(new Road(cities.get(Cities.Gors_Valen), crossroads.get(8)));
                roads.add(new Road(cities.get(Cities.Gors_Valen), crossroads.get(12)));
                roads.add(new Road(cities.get(Cities.Vizima), crossroads.get(9)));
                roads.add(new Road(cities.get(Cities.Vizima), crossroads.get(12)));
                roads.add(new Road(cities.get(Cities.Vizima), crossroads.get(13)));
                roads.add(new Road(crossroads.get(10), crossroads.get(13)));
                roads.add(new Road(crossroads.get(13), crossroads.get(14)));
                roads.add(new Road(cities.get(Cities.Vengerberg), crossroads.get(11)));
                roads.add(new Road(cities.get(Cities.Vengerberg), crossroads.get(15)));
                roads.add(new Road(crossroads.get(14), crossroads.get(15)));
                roads.add(new Road(cities.get(Cities.Maribor), crossroads.get(12)));
                roads.add(new Road(cities.get(Cities.Maribor), crossroads.get(13)));
                roads.add(new Road(cities.get(Cities.Maribor), crossroads.get(16)));
                roads.add(new Road(crossroads.get(14), crossroads.get(16)));
                roads.add(new Road(cities.get(Cities.Rivia), crossroads.get(14)));
                roads.add(new Road(cities.get(Cities.Rivia), crossroads.get(17)));
                roads.add(new Road(crossroads.get(15), crossroads.get(17)));
                roads.add(new Road(cities.get(Cities.Lyria), crossroads.get(17)));
                roads.add(new Road(cities.get(Cities.Lyria), crossroads.get(19)));
                roads.add(new Road(cities.get(Cities.Brugge), crossroads.get(16)));
                roads.add(new Road(cities.get(Cities.Brugge), crossroads.get(18)));
                roads.add(new Road(cities.get(Cities.Cintra), crossroads.get(8)));
                roads.add(new Road(cities.get(Cities.Cintra), crossroads.get(18)));
                roads.add(new Road(crossroads.get(4), crossroads.get(7)));
                roads.add(new Road(crossroads.get(18), crossroads.get(19)));
                roads.add(new Road(cities.get(Cities.Rivia), crossroads.get(19)));

                System.out.printf("Istnieje %d cywilów.\n", civilians.size());

                /*
                 * Tworzenie systemu drogowskazow.
                 */
                for (Road road : roads) {
                    roadsOfSignpost.put(road.getA(), new ArrayList<Road>());
                    roadsOfSignpost.put(road.getB(), new ArrayList<Road>());
                }
                for (Road road : roads) {
                    roadsOfSignpost.get(road.getA()).add(road);
                    roadsOfSignpost.get(road.getB()).add(road);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metoda, która za pomocą algorytmu Dijkstry wyznacza najkrótsze drogi z
     * każdego miasta do każdego innego i dodaje je do Signposts.
     *
     * @param cities slownik miast.
     * @param roadsOfSignpost slownik list drog wychodzacych z miast
     */
    private void stworzSystemDrogowskazow(Map<Cities, City> cities, Map<Signpost, List<Road>> roadsOfSignpost) {
        for (Signpost currentSignpost : roadsOfSignpost.keySet()) {
            Signpost pom, signpostFrom, signpostTo;
            Map<Signpost, Double> distances = new HashMap<>();
            Map<Signpost, Signpost> from = new HashMap<>();
            Map<Signpost, Boolean> checked = new HashMap<>();
            for (Signpost it : roadsOfSignpost.keySet()) {
                distances.put(it, Double.MAX_VALUE);
                from.put(it, null);
                checked.put(it, false);
            }
            distances.put(currentSignpost, 0.0);
            from.put(currentSignpost, currentSignpost);

            for (Signpost it : roadsOfSignpost.keySet()) {
                //Znalezienie wierzcholka o najmniejszej odleglosci do wierzcholka poczatkowego.
                pom = null;
                for (Signpost jt : roadsOfSignpost.keySet()) {
                    if (!checked.get(jt) && (pom == null || distances.get(jt) < distances.get(pom))) {
                        pom = jt;
                    }
                }
                checked.put(pom, true);
                //Modyfikacja wszystkich sasiadow wierzcholka pom
                for (Road jt : roadsOfSignpost.get(pom)) {
                    signpostFrom = pom;
                    if (jt.getA() == pom) {
                        signpostTo = jt.getB();
                    } else {
                        signpostTo = jt.getA();
                    }
                    if (!checked.get(signpostTo) && distances.get(pom) + dist(jt) < distances.get(signpostTo)) {
                        distances.put(signpostTo, distances.get(pom) + dist(jt));
                        from.put(signpostTo, signpostFrom);
                    }
                }
            }
            for (Cities currentKey2 : cities.keySet()) {
                City targetCity = cities.get(currentKey2);
                Signpost from1 = from.get(targetCity);
                Signpost from2 = targetCity;
                Road roadBetweenC1C2 = null;
                if (from1.equals(currentSignpost)) {
                    for (Road roadC1 : roadsOfSignpost.get(from1)) {
                        if (roadC1.getA() == from2 || roadC1.getB() == from2) {
                            roadBetweenC1C2 = roadC1;
                        }
                    }
                }
                while (!from1.equals(currentSignpost)) {
                    from2 = from.get(from1);
                    for (Road roadC1 : roadsOfSignpost.get(from1)) {
                        if (roadC1.getA() == from2 || roadC1.getB() == from2) {
                            roadBetweenC1C2 = roadC1;
                        }
                    }
                    from1 = from2;
                }

                currentSignpost.addRoads(targetCity.getCityName(), roadBetweenC1C2);
            }
        }
    }

    /**
     * Metoda zwracająca długość drogi.
     *
     * @param road droga.
     * @return długość drogi.
     */
    private double dist(Road road) {
        return road.getA().dist(road.getB());
    }

    /**
     * Rysowanie obiektów na mapie.
     */
    private void draw() {
        back.paintComponent(graphics);
        for (Road road : roads) {
            road.draw(graphics);
        }
        for (Signpost crossroad : crossroads) {
            crossroad.paintComponent(graphics);
        }

        for (Cities currentKey : cities.keySet()) {
            if (cities.get(currentKey).isUnderAttack() == false) {
                cities.get(currentKey).updatePotential();
            }
            cities.get(currentKey).paintComponent(graphics);
        }
        for (Civilian civ : civilians.keySet()) {
            if (civ.isIsgoing()) {
                civ.paintComponent(graphics);
            }
        }
        for (Monster mon : monsters.keySet()) {
            mon.paintComponent(graphics);
        }
        for (Hero hero : heroes.keySet()) {
            hero.paintComponent(graphics);
        }
        info.draw(graphics);
    }

    /**
     * Metoda określająca, czy gra wciąż trwa.
     *
     * @return true, jeżeli gra ma trwać dalej.
     */
    private boolean alive() {
        return destroyedCities != cities.size();
    }

    /**
     * Metoda rysująca wyniki.
     *
     * @param results wyniki do narysowania.
     * @param name imię podane przez gracza.
     */
    public void drawResults(List<Wynik> results, String name) {
        BufferedImage scroll = null;
        try {
            scroll = ImageIO.read(new File("image/map/Info.png"));
        } catch (IOException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        graphics.drawImage(scroll, 195, 50, null);
        graphics.setColor(Color.WHITE);
        int wspY = 100;
        for (int i = 0; i < results.size(); i++) {
            graphics.drawString(Integer.toString(i + 1) + ". ", 215, 110 + i * 20);
            graphics.drawString(results.get(i).getName(), 233, 110 + i * 20);
            graphics.drawString(Long.toString(results.get(i).getTime()), 360, 110 + i * 20);
            graphics.drawString(Integer.toString(results.get(i).getKills()), 400, 110 + i * 20);
        }
        if (!podanoDane) {
            graphics.drawString("Podaj imię:", 233, 120 + results.size() * 20);
            if (line < 5) {
                graphics.drawString(name + "_", 233, 140 + results.size() * 20);
            } else {
                graphics.drawString(name, 233, 140 + results.size() * 20);
            }
            line = (line + 1) % 10;
        }
        graphics.setColor(Color.BLACK);
    }

    /**
     * Metoda rysująca ekran końcowy.
     */
    private void drawEnd() {
        back.paintComponent(graphics);
        graphics.setFont(new Font("Verdana", Font.BOLD, 32));
        graphics.drawString("KONIEC GRY!", 50, 100);
        graphics.setFont(new Font("Verdana", Font.BOLD, 24));
        graphics.drawString("PO KONIUNKCJI SFER NA ŚWIECIE", 50, 140);
        graphics.drawString("BYŁO CORAZ WIĘCEJ POTWORÓW", 50, 170);
        graphics.drawString("AŻ OSTATECZNIE ZATRYUMFOWAŁY.", 50, 200);
        graphics.drawString("WIEDŹMINI WALCZYLI DŁUGO", 50, 230);
        graphics.drawString("JEDNAK CZAS LUDZKOŚCI", 50, 260);
        graphics.drawString("NIEUBŁAGANIE PRZEMINĄŁ...", 50, 290);
        String thisName = "PRZETRWAŁEŚ " + Long.toString(resultTime) + " DNI,";
        graphics.drawString(thisName, 50, 330);
        thisName = "ZABIJAJĄC " + Integer.toString(kills) + " POTWORÓW!";
        graphics.drawString(thisName, 50, 360);
    }

    /**
     * Metoda wczytująca listę z pliku.
     *
     * @param nazwaPliku do wczytania.
     * @return wczytana lista.
     * @throws FileNotFoundException
     */
    private static List<String> readFile(String nazwaPliku) throws FileNotFoundException {
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(nazwaPliku)));
        List<String> names = (List<String>) d.readObject();
        d.close();
        return names;
    }
}
