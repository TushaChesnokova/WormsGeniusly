import javafx.util.Pair;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

public class Controller implements MouseListener, MouseMotionListener {
    HashMap<Integer, Boolean> keys = new HashMap<>();
    KeyboardFocusManager keyboard;
    World world;
    boolean controllerTry = false;
    int k = -1;
    int random1;
    int random2;
    int random3;
    int counter = 0;
    public static final double G = 0.3;

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
                            world.controllerWorm.v = -5;
                            world.controllerWorm.dt = 0;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {

                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

                    }
                    keys.put(e.getKeyCode(), true);
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD1)) {
                        counter = 2;
                        world.bombB = true;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD2)) {
                        world.teleportB = true;
                        counter = 2;
                        world.weapon = false;
                    }
                    if ((world.weapon) && (e.getKeyCode() == KeyEvent.VK_NUMPAD3)) {
                        counter = 2;
                        world.poisonB = true;
                        world.weapon = false;
                    }
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    keys.put(e.getKeyCode(), false);
                }
                return false;
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < world.n; i++) {
            if ((e.getX() >= world.worms[i].x)
                    && (e.getX() <= world.worms[i].x + world.worms[i].width)
                    && (e.getY() >= world.worms[i].y)
                    && (e.getY() <= world.worms[i].y + world.worms[i].height)
                    && (world.move == world.worms[i].team)) {
                controllerTry = false;
                world.controllerWorm = world.worms[i];
                k = i;
                world.controllerWorm.v = 0;
            }
        }
        if ((e.getX() >= world.weaponX - 750)
                && (e.getX() <= world.weaponX - 750 + world.weaponWidth)
                && (e.getY() >= world.weaponY)
                && (e.getY() <= world.weaponY + world.weaponWidth)) {
            if ((world.controllerWorm.x!=0)&&(world.controllerWorm.y!=0)) {
                world.weapon = true;
            }
            else{
                controllerTry = true;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (world.bombB) {
            counter++;
            world.bomb.x = e.getX();
            world.bomb.y = e.getY();
            world.bomb.rectPressed = true;
            world.bomb.rectX = 0;
        }
        if ((e.getX() >= world.bomb.weaponX)
                && (e.getX() <= world.bomb.weaponX + world.weaponWidth)
                && (e.getY() >= world.bomb.weaponY)
                && (e.getY() <= world.bomb.weaponX + world.weaponHeight)) {
            world.weapon = false;
            world.bombB = true;
            counter++;
        }
        if (world.teleportB) {
            world.controllerWorm.x = e.getX();
            world.controllerWorm.y = e.getY();
            world.teleportB = false;
            if (world.move == Team.GIRL) {
                world.move = Team.BOY;
            } else {
                world.move = Team.GIRL;
            }
        }
        if ((e.getX() >= world.teleport.weaponX)
                && (e.getX() <= world.teleport.weaponX + world.teleport.width)
                && (e.getY() >= world.teleport.weaponY)
                && (e.getY() <= world.teleport.weaponX + world.teleport.height)) {
            world.weapon = false;
            world.teleportB = true;
        }
        if (world.poisonB) {
            world.poisonB = false;
            random1 = 1;
            world.controllerWorm.x = e.getX();
            world.controllerWorm.y = e.getY();
            world.teleportB = false;
            if (world.move == Team.GIRL) {
                world.move = Team.BOY;
            } else {
                world.move = Team.GIRL;
            }
        }
        if ((e.getX() >= world.poison.weaponX)
                && (e.getX() <= world.poison.weaponX + world.poison.width)
                && (e.getY() >= world.poison.weaponY)
                && (e.getY() <= world.poison.weaponX + world.poison.height)) {
            world.weapon = false;
            world.poisonB = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if ((world.bombB) && (counter >= 2)) {
            world.bomb.rectPressed = false;
            world.bomb.v = world.bomb.maxv * (world.bomb.rectX * 1.0 / world.bomb.rectWidth);
            world.bomb.rectX = 0;
            world.bombB = false;
            world.bomb.realised = true;
            world.target.catetS = world.bomb.y - world.controllerWorm.y;
            world.target.catetC = world.bomb.x - world.controllerWorm.x + world.bomb.width / 2;
            world.target.hypotenuse = Math.sqrt(Math.pow(world.target.catetC, 2) + Math.pow(world.target.catetS, 2));
            world.target.sin = world.target.catetS / world.target.hypotenuse;
            world.target.cos = world.target.catetC / world.target.hypotenuse;
            world.bomb.x = world.controllerWorm.x - world.bomb.width / 2;
            world.bomb.y = world.controllerWorm.y;
            world.bomb.dt = 0;
            world.bomb.vy = world.bomb.v * world.target.sin;
            world.bomb.vx = world.bomb.v * world.target.cos;
            counter = 0;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //проверяем от start до end включительно
    public Pair<Integer, Boolean> findSurface(int x, int start, int end) {
        for (int y = start; y <= end; y++) {
            if (world.landscape.bool[x][y]) {
                return new Pair<>(y - 1, true);
            }
        }
        return new Pair<>(end, false);
    }

    public void update() {
        for (int i = 0; i < world.n; i++) {
            if ((world.worms[i].y + world.worms[i].v >= world.windowHeight - world.controllerWorm.height)
                    || (world.controllerWorm.x + 2 + world.controllerWorm.width >= world.windowWidth)
                    || (world.worms[i].x - 2 <= 0)) {
                world.worms[i].health = 0;
            } else if (world.worms[i].health != 0) {

                Pair<Integer, Boolean> y2 = findSurface(
                        (int) world.worms[i].x + world.worms[i].width / 2,
                        (int) (world.worms[i].y) + world.worms[i].height,
                        (int) (world.worms[i].y + world.worms[i].v) + world.worms[i].height);
                if (!y2.getValue())
                    world.worms[i].v = world.worms[i].v + G;
                else {
                    world.worms[i].v = 0;
                }
                world.worms[i].y = y2.getKey() - world.worms[i].height;
            }
        }
        if (keys.containsKey(KeyEvent.VK_RIGHT)) {
            if (keys.get(KeyEvent.VK_RIGHT)) {
                if ((world.controllerWorm.health != 0)
                        && !(world.landscape.bool[(int) world.controllerWorm.x + 1 + world.controllerWorm.width][(int) world.controllerWorm.y + world.controllerWorm.height])) {
                    if (world.controllerWorm.x == 0) {
                        controllerTry = true;
                    } else {
                        world.controllerWorm.direction = Direction.RIGHT;
                        world.controllerWorm.x = world.controllerWorm.x + 1;
                    }
                } else if ((world.controllerWorm.health != 0)
                        && !(world.landscape.bool[(int) world.controllerWorm.x + 1 + world.controllerWorm.width][(int) world.controllerWorm.y + world.controllerWorm.height - 2])) {
                    if (world.controllerWorm.x == 0) {
                        controllerTry = true;
                    } else {
                        world.controllerWorm.direction = Direction.RIGHT;
                        world.controllerWorm.x = world.controllerWorm.x + 1;
                        world.controllerWorm.y -= 2;
                    }
                }
            }
        }
        if (keys.containsKey(KeyEvent.VK_LEFT)) {
            if (keys.get(KeyEvent.VK_LEFT)) {
                if (world.controllerWorm.x == 0) {
                    controllerTry = true;
                } else {
                    if ((world.controllerWorm.health != 0)
                            && !(world.landscape.bool[(int) world.controllerWorm.x - 2][(int) world.controllerWorm.y + world.controllerWorm.height])) {
                        world.controllerWorm.direction = Direction.LEFT;
                        world.controllerWorm.x = world.controllerWorm.x - 2;
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {//зажимаю
    }

    @Override
    public void mouseMoved(MouseEvent e) {//двигаю
        if ((world.bombB) || (world.teleportB)) {
            world.target.x = e.getX() - world.target.width / 2;
            world.target.y = e.getY() - world.target.width / 2;
        }
    }
}
