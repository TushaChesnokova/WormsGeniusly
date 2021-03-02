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
    public static final int health = 100;
    boolean realised = false;
    public static final int width = 20;
    int rectX = 0;
    int rectStartX = 800;
    public static final int rectY = 70;
    double dt;
    public static final int radius = 80;
    double x;
    double y;
    public static final double maxv = 12;
    double v;
    double vx;
    double vy;
    boolean explosion = false;
}
