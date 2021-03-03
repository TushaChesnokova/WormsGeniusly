import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class World extends JPanel {
    int random;
    int dt = 0;
    int weaponX = 820;
    int weaponY = 30;
    int weaponWidth = 40;
    int weaponHeight = 50;
    int n = 6;
    boolean poisonR = false;
    boolean weapon = false;
    Target target = new Target();
    Bomb bomb = new Bomb(this);
    Teleport teleport = new Teleport(this);
    boolean bombB = false;
    boolean arrowB = false;
    boolean teleportB = false;
    boolean poisonB = false;
    boolean arrowR = false;
    Worm controllerWorm = new Worm(0, 0);
    Worm[] worms = new Worm[n];
    Poison poison = new Poison(this);
    Poison[] poisonM = new Poison[3];
    BufferedImage landscapeImage;
    BufferedImage arrowImage;
    Landscape landscape;
    BufferedImage wormGirlImage;
    BufferedImage wormBoyImage;
    BufferedImage bombImage;
    BufferedImage weaponImage;
    BufferedImage boardImage;
    BufferedImage targetImage;
    BufferedImage backgroundImage;
    BufferedImage girlImage;
    BufferedImage boyImage;
    BufferedImage girlChosenImage;
    BufferedImage boyChosenImage;
    BufferedImage teleportImage;
    BufferedImage poisonImage;
    int windowHeight;
    int windowWidth;
    int[] xPoints;
    int[] yPoints;
    int[] x2Points;
    int[] y2Points;
    double distance;
    Team move = Team.GIRL;//девочки ходят первые и выигрывают
    Controller c = new Controller(this);
    Arrow arrow = new Arrow(this);

    public World(int windowWidth, int windowHeight) throws IOException {
        this.landscapeImage = ImageIO.read(new File("Ландшафт.jpg"));
        this.wormGirlImage = ImageIO.read(new File("Girl.png"));
        this.wormBoyImage = ImageIO.read(new File("Boy.png"));
        this.bombImage = ImageIO.read(new File("бомба.png"));
        this.boardImage = ImageIO.read(new File("доска.png"));
        this.backgroundImage = ImageIO.read(new File("фон.jpg"));
        this.weaponImage = ImageIO.read(new File("оружие.jpg"));
        this.targetImage = ImageIO.read(new File("мишень.png"));
        this.girlImage = ImageIO.read(new File("Girls надпись.png"));
        this.boyImage = ImageIO.read(new File("Boys надпись.png"));
        this.girlChosenImage = ImageIO.read(new File("girlChosen.png"));
        this.boyChosenImage = ImageIO.read(new File("boyChosen.png"));
        this.teleportImage = ImageIO.read(new File("телепорт.png"));
        this.poisonImage = ImageIO.read(new File("яд.png"));
        this.arrowImage = ImageIO.read(new File("стрела.png"));
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        xPoints = new int[windowWidth / 2 + 2];
        yPoints = new int[windowWidth / 2 + 2];
        x2Points = new int[windowWidth / 2 + 2];
        y2Points = new int[windowWidth / 2 + 2];
        for (int i = 0; i < n; i++) {
            worms[i] = new Worm((Math.random() * (landscape.width - worms[i].height) + 100), windowHeight - landscape.height - worms[i].height - 100);
        }
        landscape = new Landscape(windowWidth, windowHeight);
        for (int i = 0; i < windowWidth / 2; i++) {
            xPoints[i] = i * 2;
            yPoints[i] = 650 + (int) (Math.sin(xPoints[i] / 5.0 / Math.PI) * 10);
            x2Points[i] = i * 2;
            y2Points[i] = 650 + (int) (Math.sin(xPoints[i] / 5.0 / Math.PI) * 10);
        }
        xPoints[windowWidth / 2] = windowWidth;
        xPoints[windowWidth / 2 + 1] = 0;
        yPoints[windowWidth / 2] = windowHeight;
        yPoints[windowWidth / 2 + 1] = windowHeight;
        x2Points[windowWidth / 2] = windowWidth;
        x2Points[windowWidth / 2 + 1] = 0;
        y2Points[windowWidth / 2] = windowHeight;
        y2Points[windowWidth / 2 + 1] = windowHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int i = 0; i < windowWidth / 2; i++) {
            xPoints[i] = i * 2;
            yPoints[i] = 650 + (int) (Math.sin(xPoints[i] / 5.0 / Math.PI + dt / 20.0) * 10);
            x2Points[i] = i * 2;
            y2Points[i] = (int) (650 + (Math.sin(xPoints[i] / 5.0 / Math.PI + dt / 10.0) * 10) + Math.sin(dt / 5 / Math.PI) * 20);
        }
        g.drawImage(backgroundImage, 0, 0, windowWidth, windowHeight, null);
        if (move == Team.GIRL) {
            g.drawImage(girlImage, 910, 580, 80, 40, null);
        } else {
            g.drawImage(boyImage, 910, 580, 80, 40, null);
        }
        g.drawImage(weaponImage, weaponX - 750, weaponY, weaponWidth, weaponWidth, null);
        for (int i = 0; i < windowWidth; i += 1) {
            for (int j = 0; j < windowHeight; j = j + 1) {
                if (landscape.bool[i][j]) {
                    g.drawImage(landscapeImage, i, j, 1, 1, null);
                }
            }
        }
        if ((controllerWorm.x == 0) && (controllerWorm.y == 0) && (c.controllerTry)) {
            if (move == Team.GIRL) {
                g.drawImage(girlChosenImage, 200, 50, 300, 60, null);
            } else {
                g.drawImage(boyChosenImage, 200, 50, 300, 60, null);
            }
        }
        for (int i = 0; i < n; i++) {
            if ((i % 2 == 0) && (worms[i].health > 0)) {
                if (worms[i].direction == Direction.LEFT) {
                    g.drawImage(wormBoyImage, (int) worms[i].x, (int) worms[i].y, worms[i].width, worms[i].height, null);
                } else {
                    g.drawImage(wormBoyImage, (int) worms[i].x + worms[i].width, (int) worms[i].y, -worms[i].width, worms[i].height, null);
                }
                worms[i].team = Team.BOY;
            }
            if ((i % 2 != 0) && (worms[i].health > 0)) {
                if (worms[i].direction == Direction.LEFT) {
                    g.drawImage(wormGirlImage, (int) worms[i].x, (int) worms[i].y, worms[i].width, worms[i].height, null);
                } else {
                    g.drawImage(wormGirlImage, (int) worms[i].x + worms[i].width, (int) worms[i].y, -worms[i].width, worms[i].height, null);
                }
                worms[i].team = Team.GIRL;
            }
            if (worms[i].health > 0) {
                g.drawString(Integer.toString(worms[i].health), (int) worms[i].x + worms[i].width - 15, (int) worms[i].y);
            }
        }
        if (weapon) {
            g.drawImage(boardImage, weaponX, weaponY, 130, 200, null);
            g.drawImage(bombImage, bomb.weaponX, bomb.weaponY, weaponWidth, weaponHeight, null);
            g.drawImage(teleportImage, teleport.weaponX, teleport.weaponY, teleport.width, teleport.height, null);
            g.drawImage(poisonImage, poison.weaponX, poison.weaponY, poison.width, poison.height, null);
            g.drawImage(arrowImage, arrow.weaponX, arrow.weaponY, weaponWidth, weaponHeight / 4, null);
        }
        if (bombB) {
            g.drawImage(targetImage, target.x, target.y, target.width, target.width, null);
            g.drawImage(bombImage, (int) controllerWorm.x - bomb.width, (int) controllerWorm.y, bomb.width, bomb.width, null);
            Color color = new Color(255, 255, 255, 100);
            g.setColor(color);
            g.fillRect(bomb.rectStartX, bomb.rectY, bomb.rectWidth, 40);
            color = new Color(255, 255, 255, 255);
            g.setColor(color);
            g.fillRect(bomb.rectStartX, bomb.rectY, bomb.rectX, 40);
        }
        if (bomb.realised) {
            bomb.x = bomb.x + bomb.vx * bomb.dt;
            bomb.y = bomb.y + bomb.vy * bomb.dt;
            bomb.vy = bomb.vy + c.G;
            if ((bomb.x > 0) && (bomb.x + bomb.width / 2 < windowWidth) && (bomb.y > 0) && (bomb.y + bomb.width / 2 < windowHeight)) {
                g.drawImage(bombImage, (int) bomb.x, (int) bomb.y, bomb.width, bomb.width, null);
                if (landscape.bool[(int) bomb.x + bomb.width / 2][(int) bomb.y + bomb.width / 2]) {
                    bomb.realised = false;
                    bomb.explosion = true;
                }
            }
            bomb.dt = 0;
        }
        if (bomb.explosion) {
            for (int i = 0; i < n; i++) {
                distance = Math.sqrt(Math.pow((bomb.x - worms[i].x), 2) + Math.pow((bomb.y - worms[i].y), 2));
                if (distance < bomb.radius) {
                    worms[i].health = (int) (worms[i].health - bomb.health * (bomb.radius - distance) / bomb.radius);
                }
            }
            for (int i = 0; i < windowWidth; i++) {
                for (int j = 0; j < windowHeight; j++) {
                    distance = Math.sqrt(Math.pow((bomb.x - i), 2) + Math.pow((bomb.y - j), 2));
                    if ((distance < bomb.radius) && (landscape.bool[i][j])) {
                        landscape.bool[i][j] = false;
                    }
                }
            }
            bomb.explosion = false;
            if (move == Team.GIRL) {
                move = Team.BOY;
            } else {
                move = Team.GIRL;
            }
            controllerWorm = new Worm(0, 0);
        }
        if (teleportB) {
            g.drawImage(targetImage, target.x, target.y, target.width, target.width, null);
            g.drawImage(teleportImage, (int) controllerWorm.x - bomb.width, (int) controllerWorm.y, teleport.drawWidth, teleport.drawHeight, null);
        }
        if (poisonB) {
            for (int i = 0; i < 3; i++) {
                poisonM[i] = new Poison(this);
            }
            for (int i = 0; i < 3; i++) {
                random = (int) (Math.random() * windowWidth);
                poisonM[i].x = random;
                poisonB = false;
                poisonR = true;
                poisonM[i].dt = 0;
            }
            for (int i = 0; i < 3; i++) {
                poisonM[i].y = 0 + c.G * poisonM[i].dt;
            }
        }
        if (poisonR) {
            for (int i = 0; i < 3; i++) {
                g.drawImage(poisonImage, poisonM[i].x, (int) poisonM[i].y, poisonM[i].width, poisonM[i].height, null);
                System.out.println(poisonM[i].dt);
                poisonM[i].y = 0 + c.G * Math.pow(poisonM[i].dt, 2) / 2;
                if (landscape.bool[poisonM[i].x + poison.width / 2][(int) (poisonM[i].y + poison.height)]) {
                    poisonR = false;
                    poison.explosion = true;
                }
            }
        }
        if (poison.explosion) {
            for (int i = 0; i < 3; i++) {
                for (int a = 0; a < n; a++) {
                    if ((worms[a].x + worms[a].width > poisonM[i].x - 5) && (worms[a].x < poisonM[i].x + poison.width + 5)) {
                        worms[a].health = worms[a].health - 25;
                    }
                }
            }
            poison.explosion = false;
            if (move == Team.GIRL) {
                move = Team.BOY;
            } else {
                move = Team.GIRL;
            }
            controllerWorm = new Worm(0, 0);
        }
        Color color = new Color(10, 94, 193, 255);
        g.setColor(color);
        g.fillPolygon(x2Points, y2Points, windowWidth / 2 + 2);
        color = new Color(4, 70, 151, 255);
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, windowWidth / 2 + 2);
        if (arrowB) {
            g.drawLine(0, target.y, windowWidth, target.y);
        }
        if (arrowR) {
            g.drawImage(arrowImage, arrow.x, arrow.y, arrow.width, arrow.height, null);
            arrow.x++;
            for (int i = 0; i < controllerWorm.width; i++) {
                for (int j = 0; j < n; j++) {
                    if ((arrow.x == worms[j].x)&&(arrow.y+arrow.height/2>worms[j].y));
                }
            }
        }
    }
}

