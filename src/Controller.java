//import com.sun.deploy.security.SelectableSecurityManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class Controller implements MouseListener, MouseMotionListener {
    Set<Integer> keys = new HashSet<>();
    KeyboardFocusManager keyboard;
    World world;
    boolean controllerTry = false;
    boolean bombThrown = false;
    public static final double G = 0.1;

    Controller(World world) {
        this.world = world;
        keyboard = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyboard.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if ((world.controllerWorm.health != 0)
                            && (e.getKeyCode() == KeyEvent.VK_UP)
                            && (world.controllerWorm.v == 0)) {
                        if (world.controllerWorm.x == 0) {
                            controllerTry = true;
                        } else {
                            world.controllerWorm.v = -3.5;
                            world.controllerWorm.dt = 0;
                        }
                    }
                    keys.add(e.getKeyCode());
                    if ((world.start) // выбор количества червяков
                            && ((e.getKeyCode() >= KeyEvent.VK_NUMPAD1)
                            && (e.getKeyCode() <= KeyEvent.VK_NUMPAD9)
                            || (e.getKeyCode() >= KeyEvent.VK_1)
                            && (e.getKeyCode() <= KeyEvent.VK_9))) {
                        if ((e.getKeyCode() >= KeyEvent.VK_1)
                                && (e.getKeyCode() <= KeyEvent.VK_9)) {
                            world.n = (e.getKeyCode() - 48) * 2;
                        } else {
                            world.n = (e.getKeyCode() - 96) * 2;
                        }
                        world.start = false;
                        if (world.move == Team.GIRL) {
                            world.move = Team.BOY;
                        } else {
                            world.move = Team.GIRL;
                        }
                        world.controllerWorm = new Worm(0, 0);
                        world.startTime = world.time;
                        world.startClicked = true;
                        if (world.dt % 2 == 0) {
                            world.windV = Math.random() * World.MAX_WIND;
                        } else {
                            world.windV = -Math.random() * World.MAX_WIND;
                        }
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD7)) { // выбрана бомба
                        bombThrown = true;
                        world.bombB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD8)) { // выбран телепорт
                        world.teleportB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD9)) { // выбор яда
                        world.poisonB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD4)) { // выбор стрелы
                        world.arrowB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD5)) { // выбор индивидуальной аптечки
                        world.healB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD6)) { // выбор общей аптечки
                        world.pluralHealB = true;
                        world.weapon = false;
                    }
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    keys.remove(e.getKeyCode());
                }
                return false;
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < world.n; i++) { // выбор червяка при ходе
            if ((e.getX() >= world.worms[i].x)
                    && (e.getX() <= world.worms[i].x + Worm.WIDTH)
                    && (e.getY() >= world.worms[i].y)
                    && (e.getY() <= world.worms[i].y + Worm.HEIGHT)
                    && (world.move == world.worms[i].team)) {
                controllerTry = false;
                world.controllerWorm = world.worms[i];
                world.controllerWorm.v = 0;
            }
        }
        if ((e.getX() >= world.weaponX - 750)
                && (e.getX() <= world.weaponX - 750 + world.weaponWidth)
                && (e.getY() >= world.weaponY)
                && (e.getY() <= world.weaponY + world.weaponWidth)) {
            if ((world.controllerWorm.x != 0) && (world.controllerWorm.y != 0)) {
                world.weapon = true;
                bombThrown = false;
            } else {
                controllerTry = true;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) { //выбор бомбы
        if ((e.getX() >= world.bomb.weaponX)
                && (world.weapon)
                && (e.getX() <= world.bomb.weaponX + world.weaponWidth)
                && (e.getY() >= world.bomb.weaponY)
                && (e.getY() <= world.bomb.weaponY + world.weaponHeight)) {
            world.weapon = false;
            world.bombB = true;
        } else if (world.bombB) { // параметры, нужные для угла броска бомбы
            bombThrown = true;
            world.bomb.x = e.getX();
            world.bomb.y = e.getY();
            world.bomb.rectPressed = true;
            world.bomb.rectX = 0;
        }
        if (world.teleportB) { // активация телепорта
            world.controllerWorm.x = e.getX();
            world.controllerWorm.y = e.getY();
            world.teleportB = false;
            world.teleportR = true;
            world.startTime = world.time;
            if (world.dt % 2 == 0) {
                world.windV = Math.random() * World.MAX_WIND;
            } else {
                world.windV = -Math.random() * World.MAX_WIND;
            }
            if (world.move == Team.GIRL) {
                world.move = Team.BOY;
            } else {
                world.move = Team.GIRL;
            }
            world.controllerWorm = new Worm(0, 0);
        }
        if (world.arrowB) { //активация стрелы
            world.arrow.y = e.getY();
            world.arrowB = false;
            world.arrowR = true;
        }
        if ((e.getX() >= world.teleport.weaponX) // выброр телепорт
                && (world.weapon)
                && (e.getX() <= world.teleport.weaponX + Teleport.WIDTH)
                && (e.getY() >= world.teleport.weaponY)
                && (e.getY() <= world.teleport.weaponY + Teleport.HEIGHT)) {
            world.weapon = false;
            world.teleportB = true;
        }
        if ((e.getX() >= world.poison.weaponX) // выбор яда
                && (world.weapon)
                && (e.getX() <= world.poison.weaponX + Poison.WIDTH)
                && (e.getY() >= world.poison.weaponY)
                && (e.getY() <= world.poison.weaponY + Poison.HEIGHT)) {
            world.weapon = false;
            world.poisonB = true;
        }
        if ((e.getX() >= world.arrow.weaponX) // выбор стрелы
                && (world.weapon)
                && (e.getX() <= world.arrow.weaponX + world.weaponWidth)
                && (e.getY() >= world.arrow.weaponY)
                && (e.getY() <= world.arrow.weaponY - 10 + world.weaponHeight)) {
            world.weapon = false;
            world.arrowB = true;
        }
        if ((e.getX() >= world.heal.weaponX) // выбор индивидуальной аптечки
                && (world.weapon)
                && (e.getX() <= world.heal.weaponX + world.weaponWidth)
                && (e.getY() >= world.heal.weaponY)
                && (e.getY() <= world.heal.weaponY + world.weaponHeight)) {
            world.weapon = false;
            world.healB = true;
        }
        if ((e.getX() >= world.pluralHeal.weaponXP) // выбор общей аптечки
                && (world.weapon)
                && (e.getX() <= world.pluralHeal.weaponXP + world.weaponWidth)
                && (e.getY() >= world.pluralHeal.weaponYP)
                && (e.getY() <= world.pluralHeal.weaponYP + world.weaponHeight)) {
            world.weapon = false;
            world.pluralHealB = true;
        } else if ((world.pluralHealB)) { // активация общей аптечки
            world.pluralHeal.healX = e.getX();
            world.pluralHeal.healY = e.getY();
            world.pluralHealB = false;
            world.pluralHealR = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if ((world.bombB) && (bombThrown)) { // полет бомбы
            world.bomb.rectPressed = false;
            world.bomb.v = Bomb.MAX_V * (world.bomb.rectX * 1.0 / world.bomb.rectWidth);
            world.bomb.rectX = 0;
            world.bombB = false;
            world.bomb.realised = true;
            world.target.catetS = world.bomb.y - world.controllerWorm.y;
            world.target.catetC = world.bomb.x - world.controllerWorm.x + Bomb.SIZE / 2.0;
            world.target.hypotenuse = Math.sqrt(Math.pow(world.target.catetC, 2) + Math.pow(world.target.catetS, 2));
            world.target.sin = world.target.catetS / world.target.hypotenuse;
            world.target.cos = world.target.catetC / world.target.hypotenuse;
            world.bomb.x = world.controllerWorm.x - Bomb.SIZE / 2.0;
            world.bomb.y = world.controllerWorm.y;
            world.bomb.dt = 0;
            world.bomb.vy = world.bomb.v * world.target.sin;
            world.bomb.vx = world.bomb.v * world.target.cos;
            bombThrown = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //проверяем от start до end включительно
    // (Нижняя пустая точка, нашли или нет)

    /**
     * ищет столкновение червяка с землей
     *
     * @param x     иксовая координата червяка при падении
     * @param start игриковая координата червяка на момент начала падения
     * @param end   игриковая координата червяка на момент начала падения, если он не приземлился
     * @return игриковую координату  приземления червяка (если было), столкнулся червяк с землей или нет
     */
    public Pair<Integer, Boolean> findSurface(int x, int start, int end) {
        for (int y = start; y <= end; y++) {
            if ((world.landscape.landscape.length > x)
                    && (x >= 0)
                    && (world.landscape.landscape[x].length > y)
                    && (y >= 0)
                    && (world.landscape.landscape[x][y])) {
                return new Pair<>(y - 1, true);
            }
        }
        return new Pair<>(end, false);
    }

    /**
     * ищет столкновение бомбы с поверхностью
     *
     * @param startX иксовая координата бомбы на момент начала полета
     * @param startY игриковая координата бомбы на момент начала полета
     * @param endX   иксовая координата бомбы при столкновении
     * @param endY   игриковая координата бомбы при столкновении
     * @return координаты столновения бомбы (если было), столкнулась бомба земли или нет
     */
    public Triple<Integer, Integer, Boolean> findSurface(int startX, int startY, int endX, int endY) {
        if (Math.abs(startX - endX) > Math.abs(startY - endY)) {
            for (int x = startX; (x < endX) && (endX > startX) || (x > endX) && (endX < startX); x = x + (int) Math.signum(endX - startX)) {
                int y = (int) (startY + world.bomb.vy / world.bomb.vx * (x - startX) + G * Math.pow(x - startX, 2) / 2 / Math.pow(world.bomb.vx, 2));
                if ((x > 0) && (x < world.windowWidth)
                        && (y > 0) && (y < world.windowHeight)
                        && (world.landscape.landscape[x][y])) {
                    return new Triple<>(x, y, true);
                }
            }
        } else {
            for (int y = startY; ((y < endY) && (endY > startY)) || ((y > endY) && (endY < startY)); y = y + (int) Math.signum(endY - startY)) {
                double t = 1.0*Math.abs(y-startY)/Math.abs(endY-startY);
                int x = (int) (startX + world.bomb.vx *t);
                if ((x > 0) && (x < world.windowWidth)
                        && (y > 0) && (y < world.windowHeight)
                        && (world.landscape.landscape[x][y])) {
                    return new Triple<>(x, y, true);
                }
            }
        }
        return new Triple<>(endX, endY, false);
    }

    public void update() {
        for (int i = 0; i < world.n; i++) {
            if ((world.worms[i].y + world.worms[i].v >= world.windowHeight - Worm.HEIGHT) // червячок упал в воду
                    || (world.worms[i].x + 2 + Worm.WIDTH >= world.windowWidth)
                    || (world.worms[i].x - 2 <= 0)) {
                if (world.worms[i].health != 0) {
                    world.watered = true;
                    world.worms[i].health = 0;
                }
            } else if (world.worms[i].health != 0) {

                Pair<Integer, Boolean> y2 = findSurface( // реализация падения червяка
                        (int) world.worms[i].x + Worm.WIDTH / 2,
                        (int) (world.worms[i].y) + Worm.HEIGHT,
                        (int) (world.worms[i].y + world.worms[i].v) + Worm.HEIGHT + 1);
                if (!y2.getValue() || y2.getKey() > world.worms[i].y + Worm.HEIGHT) {
                    world.worms[i].v = world.worms[i].v + G;
                } else {
                    world.worms[i].v = 0;
                }
                world.worms[i].y = y2.getKey() - Worm.HEIGHT;
            }
        }

        if (world.bomb.realised) { //реализация полета бомбы
            Triple<Integer, Integer, Boolean> y1 = findSurface(
                    (int) world.bomb.x,
                    (int) world.bomb.y,
                    (int) (world.bomb.x + world.bomb.vx * world.bomb.dt),
                    (int) (world.bomb.y + world.bomb.vy * world.bomb.dt));
            if (!y1.getC()) {
                world.bomb.x = world.bomb.x + (world.bomb.vx - world.windV) * world.bomb.dt;
                world.bomb.y = world.bomb.y + world.bomb.vy * world.bomb.dt;
                world.bomb.vy = world.bomb.vy + G;
                world.bomb.dt = 0;
            } else {
                world.bomb.realised = false;
                world.bomb.explosion = true;
            }
        }

        if (keys.contains(KeyEvent.VK_RIGHT)) { // передвижение червяка направо
            if ((world.controllerWorm.health != 0)
                    && !(world.landscape.landscape[(int) world.controllerWorm.x + 1 + Worm.WIDTH][(int) world.controllerWorm.y + Worm.HEIGHT])) {
                if (world.controllerWorm.x == 0) {
                    controllerTry = true;
                } else {
                    world.controllerWorm.direction = Direction.RIGHT;
                    world.controllerWorm.x = world.controllerWorm.x + 1;
                }
            }
        }
        if (keys.contains(KeyEvent.VK_LEFT)) { // передвижение червяка налево
            if (world.controllerWorm.x == 0) {
                controllerTry = true;
            } else {
                if ((world.controllerWorm.health != 0)
                        && !(world.landscape.landscape[(int) world.controllerWorm.x - 2][(int) world.controllerWorm.y + Worm.HEIGHT])) {
                    world.controllerWorm.direction = Direction.LEFT;
                    world.controllerWorm.x = world.controllerWorm.x - 1;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {//зажимаю
    }

    @Override
    public void mouseMoved(MouseEvent e) {//двигаю //координаты отрисовки мишени
        world.target.x = e.getX() - Target.SIZE / 2;
        world.target.y = e.getY() - Target.SIZE / 2;
    }
}
