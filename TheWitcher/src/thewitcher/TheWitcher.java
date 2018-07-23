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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Główna klasa projektu, która inicjalizuje grę oraz wczytuje i zapisuje
 * wyniki.
 *
 * @author Imrihil
 */
public class TheWitcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Wynik> results = new ArrayList<>();
        Wynik result;
        try {
            results = readResults();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TheWitcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            TheGame game = new TheGame(results);
            result = game.play();
            results.add(result);

            Collections.sort(results);

            if (results.size() == 6) {
                results.remove(5);
            }
            try {
                saveResults(results);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TheWitcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metoda wczytująca wyniki z pliku.
     *
     * @return wyniki.
     * @throws FileNotFoundException.
     */
    private static List<Wynik> readResults() throws FileNotFoundException {
        String nazwaPliku = "file/results.xml";
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(nazwaPliku)));
        List<Wynik> results = (List<Wynik>) d.readObject();
        d.close();
        return results;
    }

    /**
     * Metoda zapisująca wyniki do pliku.
     *
     * @param results wyniki.
     * @throws FileNotFoundException
     */
    private static void saveResults(List<Wynik> results) throws FileNotFoundException {
        String nazwaPliku = "file/results.xml";
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nazwaPliku)));
        e.writeObject(results);
        e.close();
    }
}
