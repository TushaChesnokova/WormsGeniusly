import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class World extends JPanel {
    boolean teleportR = false;
    boolean startClicked = false;
    double windV;
    public static final double MAX_WIND = 3;
    boolean start = true;
    public static final int MOVE_TIME = 60;
    int time;
    int startTime;
    int summarGirlHealth = 0;
    int summarBoyHealth = 0;
    boolean muff = false;
    boolean lucky = false;
    boolean impostor = false;
    boolean notImpostor = false;
    boolean isOver = false;
    boolean watered = false;
    boolean killed = false;
    int random;
    int dt = 0;
    int weaponX = 820;
    int weaponY = 30;
    int weaponWidth = 40;
    int weaponHeight = 50;
    int n = 20;
    boolean poisonR = false;
    boolean weapon = false;
    Target target = new Target();
    Bomb bomb = new Bomb(this);
    Teleport teleport = new Teleport(this);
    boolean bombB = false;
    boolean healB = false;
    boolean pluralHealB = false;
    boolean arrowB = false;
    boolean teleportB = false;
    boolean poisonB = false;
    boolean arrowR = false;
    boolean pluralHealR = false;
    Worm controllerWorm = new Worm(0, 0);
    Worm[] worms = new Worm[n];
    Poison poison = new Poison(this);
    Poison[] poisonA = new Poison[3];
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
    BufferedImage healImage;
    BufferedImage pluralHealImage;
    BufferedImage girlsWonImage;
    BufferedImage boysWonImage;
    BufferedImage startImage;
    BufferedImage windImage;
    int windowHeight;
    int windowWidth;
    int[] xPoints;
    int[] yPoints;//координаты для первой волны
    int[] x2Points;
    int[] y2Points;//координаты для второй волны
    double distance;
    Team move = Team.GIRL;//девочки ходят первые и выигрывают
    Controller c = new Controller(this);
    Arrow arrow = new Arrow(this);
    Heal heal = new Heal(this);
    Heal pluralHeal = new Heal(this);

    public World(int windowWidth, int windowHeight) throws IOException {
        this.windImage = ImageIO.read(World.class.getResourceAsStream("ветер.png"));
        this.startImage = ImageIO.read(World.class.getResourceAsStream("start.jpg"));
        this.girlsWonImage = ImageIO.read(World.class.getResourceAsStream("The girls won.png"));
        this.boysWonImage = ImageIO.read(World.class.getResourceAsStream("The boys won.png"));
        this.wormGirlImage = ImageIO.read(World.class.getResourceAsStream("Girl.png"));
        this.wormBoyImage = ImageIO.read(World.class.getResourceAsStream("Boy.png"));
        this.bombImage = ImageIO.read(World.class.getResourceAsStream("бомба.png"));
        this.boardImage = ImageIO.read(World.class.getResourceAsStream("доска.png"));
        this.backgroundImage = ImageIO.read(World.class.getResourceAsStream("фон.jpg"));
        this.weaponImage = ImageIO.read(World.class.getResourceAsStream("оружие.jpg"));
        this.targetImage = ImageIO.read(World.class.getResourceAsStream("мишень.png"));
        this.girlImage = ImageIO.read(World.class.getResourceAsStream("Girls надпись.png"));
        this.boyImage = ImageIO.read(World.class.getResourceAsStream("Boys надпись.png"));
        this.girlChosenImage = ImageIO.read(World.class.getResourceAsStream("girlChosen.png"));
        this.boyChosenImage = ImageIO.read(World.class.getResourceAsStream("boyChosen.png"));
        this.teleportImage = ImageIO.read(World.class.getResourceAsStream("телепорт.png"));
        this.poisonImage = ImageIO.read(World.class.getResourceAsStream("яд.png"));
        this.arrowImage = ImageIO.read(World.class.getResourceAsStream("стрела.png"));
        this.healImage = ImageIO.read(World.class.getResourceAsStream("аптечка.png"));
        this.pluralHealImage = ImageIO.read(World.class.getResourceAsStream("аптечка1.png"));
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        xPoints = new int[windowWidth / 2 + 2];
        yPoints = new int[windowWidth / 2 + 2];
        x2Points = new int[windowWidth / 2 + 2];
        y2Points = new int[windowWidth / 2 + 2];
        for (int i = 0; i < n; i++) {
            worms[i] = new Worm((Math.random() * (Landscape.WIDTH - Worm.HEIGHT) + 100), windowHeight - Landscape.HEIGHT - Worm.HEIGHT - 100);
            if ((i % 2 == 0) && (worms[i].health > 0)) {
                worms[i].team = Team.BOY;
            } else {
                worms[i].team = Team.GIRL;
            }
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
        for (int i = 0; i < windowWidth / 2; i++) { // изменение координат волн
            xPoints[i] = i * 2;
            yPoints[i] = 650 + (int) (Math.sin(xPoints[i] / 5.0 / Math.PI + dt / 20.0) * 10);
            x2Points[i] = i * 2;
            y2Points[i] = (int) (650 + (Math.sin(xPoints[i] / 5.0 / Math.PI + dt / 10.0) * 10) + Math.sin(dt / 5.0 / Math.PI) * 20);
        }
        g.drawImage(backgroundImage, 0, 0, windowWidth, windowHeight, null);
        if (move == Team.GIRL) {
            g.drawImage(girlImage, 910, 580, 80, 40, null);
        } else {
            g.drawImage(boyImage, 910, 580, 80, 40, null);
        }
        g.drawImage(weaponImage, weaponX - 750, weaponY, weaponWidth, weaponWidth, null);
        if ((controllerWorm.x == 0) && (controllerWorm.y == 0) && (c.controllerTry)) {
            if (move == Team.GIRL) {
                g.drawImage(girlChosenImage, 200, 50, 300, 60, null);
            } else {
                g.drawImage(boyChosenImage, 200, 50, 300, 60, null);
            }
        }
        for (int i = 0; i < windowWidth; i += 1) { // отрисовка ландшафта
            for (int j = 0; j < windowHeight; j = j + 1) {
                if (landscape.landscape[i][j]) {
                    g.setColor(new Color(165, 224, 255, 255));
                    g.drawLine(i, j, i, j);
                }
            }
        }
        for (int i = 0; i < n; i++) { // отрисовка чевряков
            if ((i % 2 == 0) && (worms[i].health > 0)) {
                if (worms[i].direction == Direction.LEFT) {
                    g.drawImage(wormBoyImage, (int) worms[i].x, (int) worms[i].y, Worm.WIDTH, Worm.HEIGHT, null);
                } else {
                    g.drawImage(wormBoyImage, (int) worms[i].x + Worm.WIDTH, (int) worms[i].y, -Worm.WIDTH, Worm.HEIGHT, null);
                }
            }
            if ((i % 2 != 0) && (worms[i].health > 0)) {
                if (worms[i].direction == Direction.LEFT) {
                    g.drawImage(wormGirlImage, (int) worms[i].x, (int) worms[i].y, Worm.WIDTH, Worm.HEIGHT, null);
                } else {
                    g.drawImage(wormGirlImage, (int) worms[i].x + Worm.WIDTH, (int) worms[i].y, -Worm.WIDTH, Worm.HEIGHT, null);
                }
            }
            if (worms[i].health > 0) {
                g.setColor(new Color(0, 0, 0, 255));
                g.drawString(Integer.toString(worms[i].health), (int) worms[i].x + Worm.WIDTH - 15, (int) worms[i].y);
            }
        }
        if (weapon) { // отрисовка доски оружия
            g.drawImage(boardImage, weaponX, weaponY, 130, 200, null);
            g.drawImage(bombImage, bomb.weaponX, bomb.weaponY, weaponWidth, weaponHeight, null);
            g.drawImage(teleportImage, teleport.weaponX, teleport.weaponY, Teleport.WIDTH, Teleport.HEIGHT, null);
            g.drawImage(poisonImage, poison.weaponX, poison.weaponY, Poison.WIDTH, Poison.HEIGHT, null);
            g.drawImage(arrowImage, arrow.weaponX, arrow.weaponY, weaponWidth, weaponHeight / 4, null);
            g.drawImage(healImage, heal.weaponX, heal.weaponY, weaponWidth, (int) (weaponHeight / 1.5), null);
            g.drawImage(pluralHealImage, heal.weaponXP, heal.weaponYP, weaponWidth, (int) (weaponHeight / 1.5), null);
        }

        if (bombB) { // прямоугольник скорости бомбы
            g.drawImage(targetImage, target.x, target.y, Target.SIZE, Target.SIZE, null);
            g.drawImage(bombImage, (int) controllerWorm.x - Bomb.SIZE, (int) controllerWorm.y, Bomb.SIZE, Bomb.SIZE, null);
            Color color = new Color(255, 255, 255, 100);
            g.setColor(color);
            g.fillRect(bomb.rectStartX, Bomb.RECT_Y, bomb.rectWidth, 40);
            color = new Color(255, 255, 255, 255);
            g.setColor(color);
            g.fillRect(bomb.rectStartX, Bomb.RECT_Y, bomb.rectX, 40);
        }
        if ((bomb.rectPressed) && (bomb.rectX >= bomb.rectWidth)) { // расчитывание угла полета
            bomb.rectPressed = false;
            bomb.v = Bomb.MAX_V * (bomb.rectX * 1.0 / bomb.rectWidth);
            bomb.rectX = 0;
            bombB = false;
            bomb.realised = true;
            target.catetS = bomb.y + Target.SIZE / 2.0 - controllerWorm.y;
            target.catetC = bomb.x + Target.SIZE / 2.0 - controllerWorm.x + Bomb.SIZE / 2.0;
            target.hypotenuse = Math.sqrt(Math.pow(target.catetC, 2) + Math.pow(target.catetS, 2));
            target.sin = target.catetS / target.hypotenuse;
            target.cos = target.catetC / target.hypotenuse;
            target.tg = target.catetS / target.catetC;
            bomb.x = controllerWorm.x - Bomb.SIZE / 2.0;
            bomb.y = controllerWorm.y;
            bomb.dt = 0;
            bomb.vy = bomb.v * target.sin;
            bomb.vx = bomb.v * target.cos;
        }
        if (bomb.realised) { // отрисовка при полете бомбы
            g.drawImage(bombImage, (int) bomb.x, (int) bomb.y, Bomb.SIZE, Bomb.SIZE, null);
            if ((bomb.x <= 0)
                    || (bomb.x >= windowWidth)
                    || (bomb.y <= 0)
                    || (bomb.y >= windowHeight)) {
                bomb.realised = false;
                bomb.explosion = true;
            }
        }
        if (bomb.explosion) { // взрыв бомбы
            for (int i = 0; i < n; i++) {
                distance = Math.sqrt(Math.pow((bomb.x+bomb.SIZE/2 - worms[i].x), 2) + Math.pow((bomb.y+bomb.SIZE/2 - worms[i].y), 2));
                if (distance < Bomb.RADIUS) {
                    worms[i].health = (int) (worms[i].health - Bomb.HEALTH * (Bomb.RADIUS - distance) / Bomb.RADIUS);
                    if (worms[i].team == move) {
                        impostor = true;
                    }
                    if (worms[i].team != move) {
                        notImpostor = true;
                    }
                    if (worms[i].health <= 0) killed = true;
                }
            }
            if ((!notImpostor) && (!impostor)) {
                muff = true;
            }
            for (int i = 0; i < windowWidth; i++) {
                for (int j = 0; j < windowHeight; j++) {
                    distance = Math.sqrt(Math.pow((bomb.x - i), 2) + Math.pow((bomb.y - j), 2));
                    if ((distance < Bomb.RADIUS) && (landscape.landscape[i][j])) {
                        landscape.landscape[i][j] = false;
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
            startTime = time;
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        if (teleportB) { // выбор места активации телепорта
            g.drawImage(targetImage, target.x, target.y, Target.SIZE, Target.SIZE, null);
            g.drawImage(teleportImage, (int) controllerWorm.x - Bomb.SIZE, (int) controllerWorm.y, Teleport.DRAW_WIDTH, Teleport.DRAW_HEIGHT, null);
        }
        if (poisonB) { // отрисовка яда
            for (int i = 0; i < 3; i++) {
                poisonA[i] = new Poison(this);
            }
            for (int i = 0; i < 3; i++) {
                random = (int) (Math.random() * windowWidth-Poison.WIDTH);
                poisonA[i].x = random;
                poisonB = false;
                poisonR = true;
                poisonA[i].dt = 0;
            }
            for (int i = 0; i < 3; i++) {
                poisonA[i].y = 0 + Controller.G * poisonA[i].dt;
            }
        }
        if (poisonR) { // полет яда
            for (int i = 0; i < 3; i++) {
                g.drawImage(poisonImage, poisonA[i].x, (int) poisonA[i].y, Poison.WIDTH, Poison.HEIGHT, null);
                poisonA[i].y = 0 + Controller.G * Math.pow(poisonA[i].dt, 2) / 2;
                if (landscape.landscape[poisonA[i].x + Poison.WIDTH / 2][(int) (poisonA[i].y + Poison.HEIGHT)]) {
                    poisonR = false;
                    poison.explosion = true;
                }
            }
        }
        if (poison.explosion) { // активация яда
            for (int i = 0; i < 3; i++) {
                for (int a = 0; a < n; a++) {
                    if ((worms[a].x + Worm.WIDTH > poisonA[i].x - 5) && (worms[a].x < poisonA[i].x + Poison.WIDTH + 5)) {
                        worms[a].health = worms[a].health - 25;
                        if (worms[a].health <= 0) {
                            if (worms[a].x!=0) killed = true;
                            worms[a].x=0;
                            worms[a].y=0;
                        }
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
            startTime = time;
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        Color color = new Color(10, 94, 193, 255);
        g.setColor(color);
        g.fillPolygon(x2Points, y2Points, windowWidth / 2 + 2);
        color = new Color(4, 70, 151, 255);
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, windowWidth / 2 + 2);
        if (arrowB) { // отрисовка линии при выборе координаты полета стрелы
            arrow.x = 0;
            g.drawLine(0, target.y, windowWidth, target.y);
        }
        if (arrowR) { // полет стрелы
            g.drawImage(arrowImage, arrow.x, arrow.y - arrow.HEIGHT, arrow.WIDTH, arrow.HEIGHT, null);
            arrow.x = arrow.x + 70;
        }
        if ((arrowR) && (arrow.x >= 970)) { // забирание жизней от стрелы
            for (int j = 0; j < n; j++) {
                if ((arrow.y - arrow.HEIGHT > worms[j].y) && (arrow.y - arrow.HEIGHT < worms[j].y + Worm.HEIGHT)) {
                    worms[j].health = worms[j].health - 10;
                    if (worms[j].team == move) {
                        impostor = true;
                    }
                    if (worms[j].team != move) {
                        notImpostor = true;
                    }
                    if (worms[j].health <= 0) {
                        if (worms[j].x!=0) killed = true;
                        worms[j].x=0;
                        worms[j].y=0;
                    }
                }
            }
            if ((!notImpostor) && (!impostor)) {
                muff = true;
            }
            arrowR = false;
            if (move == Team.GIRL) {
                move = Team.BOY;
            } else {
                move = Team.GIRL;
            }
            controllerWorm = new Worm(0, 0);
            startTime = time;
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        color = new Color(135, 6, 16, dt * 4 % 255);
        g.setColor(color);
        g.fillOval((int) controllerWorm.x + 5, (int) controllerWorm.y - 15, 10, 10);
        if (pluralHealB) {
            g.drawImage(targetImage, target.x, target.y, Target.SIZE, Target.SIZE, null);
        }
        if (healB) { // личная аптечка активирована
            g.drawImage(healImage, (int) controllerWorm.x, (int) controllerWorm.y, heal.width, heal.height, null);
            if (dt % 10 == 0) {
                lucky = true;
                controllerWorm.health += heal.heal;
                healB = false;
                if (move == Team.GIRL) {
                    move = Team.BOY;
                } else {
                    move = Team.GIRL;
                }
                controllerWorm = new Worm(0, 0);
                startTime = time;
            }
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        if ((pluralHealR)) { // общая аптечка активирована
            g.drawImage(pluralHealImage, pluralHeal.healX, pluralHeal.healY, pluralHeal.width, pluralHeal.height, null);
            if (dt % 10 == 0) {
                for (int a = 0; a < n; a++) {
                    if ((worms[a].x + Worm.WIDTH > pluralHeal.healX - 5)
                            && (worms[a].x < pluralHeal.healX + Poison.WIDTH + 5)
                            && (worms[a].y + Worm.HEIGHT > pluralHeal.healY)
                            && (worms[a].y < pluralHeal.healY + pluralHeal.width)) {
                        worms[a].health = worms[a].health + pluralHeal.pluralHeal;
                    }
                }
                lucky = true;
                pluralHealR = false;
                if (move == Team.GIRL) {
                    move = Team.BOY;
                } else {
                    move = Team.GIRL;
                }
                controllerWorm = new Worm(0, 0);
                startTime = time;
            }
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        if (time >= startTime + MOVE_TIME) {
            if (move == Team.GIRL) {
                move = Team.BOY;
            } else {
                move = Team.GIRL;
            }
            controllerWorm = new Worm(0, 0);
            startTime = time;
            bombB = false;
            teleportB = false;
            arrowB = false;
            pluralHealB = false;
            weapon = false;
            if (dt % 2 == 0) {
                windV = Math.random() * MAX_WIND;
            } else {
                windV = -Math.random() * MAX_WIND;
            }
        }
        g.setColor(new Color(255, 255, 255, 255));
        if (windV > 0) {
            g.drawImage(windImage, 965, 625, -25, 25, null);
        }
        if (windV < 0) {
            g.drawImage(windImage, 940, 625, 25, 25, null);
        }
        g.fillRect(885, 655, 110, 20);
        g.setColor(new Color(0, 0, 0, 255));
        DecimalFormat df = new DecimalFormat("#.##");
        String windsp = "wind speed = " + df.format(Math.abs(windV));
        g.drawString(windsp, 890, 670);
        g.drawRect(20, 650, 100, 20);
        g.drawRect(20, 620, 100, 20);
        g.setColor(new Color(203, 5, 148, 255));
        summarGirlHealth = 0;
        for (int i = 1; i < n; i += 2) {
            if (worms[i].health > 0) {
                summarGirlHealth = summarGirlHealth + worms[i].health;
            }
        }
        g.fillRect(20, 650, (int) (summarGirlHealth * 1.0 / (Worm.START_HEALTH * n / 2) * 100), 20);
        summarBoyHealth = 0;
        g.setColor(new Color(5, 48, 203, 255));
        for (int i = 0; i < n; i += 2) {
            if (worms[i].health > 0) {
                summarBoyHealth = summarBoyHealth + worms[i].health;
            }
        }
        g.fillRect(20, 620, (int) (summarBoyHealth * 1.0 / (Worm.START_HEALTH * n / 2) * 100), 20);
        boolean isDead = true;
        for (int i = 0; i < n; i += 2) {
            isDead = isDead && worms[i].health <= 0;
        }
        isOver = isOver || isDead;
        if (isDead) g.drawImage(girlsWonImage, 0, 0, windowWidth, windowHeight - 20, null);
        isDead = true;
        for (int i = 1; i < n; i += 2) {
            isDead = isDead && worms[i].health <= 0;
        }
        isOver = isOver || isDead;
        if (isDead) g.drawImage(boysWonImage, 0, 0, windowWidth, windowHeight - 20, null);
        g.setColor(new Color(111, 222, 255));
        g.fillRect(465, 630, 70, 40);
        g.setColor(new Color(0, 0, 0));
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 3F);
        g.setFont(newFont);
        g.drawString(Integer.toString(MOVE_TIME - time + startTime), 480, 665);
        if (start) {
            g.drawImage(startImage, 0, 0, windowWidth, windowHeight, null);
        }
    }
}
