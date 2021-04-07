import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame();
        frame.setSize(1000, 700);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        World world = new World(windowWidth, windowHeight);
        frame.add(world);
        frame.setUndecorated(true);
        frame.addMouseListener(world.c);
        frame.addMouseMotionListener(world.c);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /**
         * проигрывание звука
         */
        new Thread(() -> {
            while (true) {
                if (world.teleportR) {
                    new MakeSound().playSound("телепорт.wav");
                    world.teleportR = false;
                }
                if (world.startClicked) {
                    new MakeSound().playSound("startround.wav");
                    world.startClicked = false;
                }
                if (world.c.keys.contains(KeyEvent.VK_LEFT)
                        && (world.controllerWorm.x != 0)
                        && (world.controllerWorm.health != 0)
                        && !(world.landscape.landscape[(int) world.controllerWorm.x - 2][(int) world.controllerWorm.y + Worm.HEIGHT])) {
                    new MakeSound().playSound("ходьба.wav");
                }
                if (world.c.keys.contains(KeyEvent.VK_RIGHT)
                        && (world.controllerWorm.x != 0)
                        && (world.controllerWorm.health != 0)
                        && !(world.landscape.landscape[(int) world.controllerWorm.x - 2][(int) world.controllerWorm.y + Worm.HEIGHT])) {
                    new MakeSound().playSound("ходьба.wav");
                }
                if (world.arrowR) new MakeSound().playSound("стрела.wav");
                if (world.killed) {
                    new MakeSound().playSound("Убит.wav");
                    world.killed = false;
                    world.impostor = false;
                    world.notImpostor = false;
                } else if (world.impostor) {
                    new MakeSound().playSound("Предатель.wav");
                    world.impostor = false;
                    world.notImpostor = false;
                } else if (world.notImpostor) {
                    new MakeSound().playSound("Ой.wav");
                    world.notImpostor = false;
                }
                if (world.watered) {
                    new MakeSound().playSound("Бултых.wav");
                    world.watered = false;
                }
                if (world.isOver) new MakeSound().playSound("Гейм Овер.wav");
                if (world.muff) {
                    new MakeSound().playSound("Промазал.wav");
                    world.muff = false;
                }
                if (world.lucky) {
                    new MakeSound().playSound("Повезло.wav");
                    world.lucky = false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (!world.isOver) {
            world.time = (int) (System.currentTimeMillis()) / 1000;
            world.dt++;
            for (int i = 0; i < world.n; i++) {
                world.worms[i].dt++;
            }
            world.bomb.dt++;
            for (int i = 0; i < 3; i++)
                if (world.poisonA[i] != null)
                    world.poisonA[i].dt++;
            if (world.bomb.rectPressed) {
                world.bomb.rectX++;
            }
            world.c.update();
            Thread.sleep(1000 / 60);
            frame.repaint();
        }
    }
}
