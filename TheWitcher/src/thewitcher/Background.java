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
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Klasa czyszcząca ekran i rysująca na nim tło.
 *
 * @author Imrihil
 */
public class Background extends JPanel {

    /**
     * Obrazek zawierający tło.
     */
    private BufferedImage background;

    /**
     * Konstruktor klasy wczytujący bazowy obrazek tła.
     *
     * @throws IOException
     */
    public Background() throws IOException {
        background = ImageIO.read(new File("image/map/StartingScreen.png"));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(background, 0, 0, null);
    }

    /**
     * @param background the background to set
     */
    public void setBackground(BufferedImage background) {
        this.background = background;
    }
}
