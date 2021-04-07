public class Teleport {
    World world;

    public Teleport(World world) {
        this.world = world;
        weaponX = world.weaponX + 5 + world.weaponWidth;
        weaponY = world.weaponY;
    }

    int weaponX;
    int weaponY;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 55;
    public static final int DRAW_WIDTH = 20;
    public static final int DRAW_HEIGHT = 30;
}
