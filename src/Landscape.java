import javax.swing.*;

public class Landscape extends JPanel {
    int windowHeight;
    int windowWidth;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    boolean[][] landscape;
    int randomX;
    int randomY;
    int randomR;
    double distance;

    public Landscape(int windowWidth, int windowHeight) {
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        landscape = new boolean[windowWidth][windowWidth];
        for (int i = 0; i < windowWidth; i++) {
            for (int j = 0; j < windowHeight; j++) {
                landscape[i][j] = false;
            }
        }
        for (int i = 100; i < WIDTH + 100; i++) {
            for (int j = windowHeight; j > windowHeight - HEIGHT; j = j - 1) {
                landscape[i][j] = true;
            }
        }
        for (int k = 0; k < 20; k++) {
            randomR = (int) (Math.random() * 100);
            randomX = (int) (Math.random() * (windowWidth - 200) + randomR);
            randomY = (int) (Math.random() * (windowHeight - 200) + randomR);
            for (int i = randomX - randomR; i < randomX + randomR; i++) {
                for (int j = randomY - randomR; j < randomY + randomR; j++) {
                    distance = Math.sqrt(Math.pow((randomX - i), 2) + Math.pow((randomY - j), 2));
                    if ((distance < randomR) && (landscape[i][j])) {
                        landscape[i][j] = false;
                    }
                }
            }
        }
    }
}
