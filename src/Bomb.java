public class Bomb {
    World world;

    public Bomb(World world) {
        this.world = world;
        weaponX = world.weaponX+5;
        weaponY = world.weaponY;
    }

    boolean rectPressed = false;
    int weaponX;
    int weaponY;
    int rectWidth = 100;
    public static final int HEALTH = 100;
    boolean realised = false;
    public static final int SIZE = 20;
    int rectX = 0;
    int rectStartX = 800;
    public static final int RECT_Y = 70;
    double dt;
    public static final int RADIUS = 80;
    double x;
    double y;
    public static final double MAX_V = 12;
    double v;
    double vx;
    double vy;
    boolean explosion = false;
}
