public class Arrow {
    World world;
    int x=0;
    int y;
    int weaponX;
    int weaponY;
    public static final int WIDTH = 120;
    public static final int HEIGHT = 20;
    public Arrow(World world) {
        this.world = world;
        weaponX = world.weaponX+5;
        weaponY = world.weaponY+world.weaponHeight+10;
    }
}
