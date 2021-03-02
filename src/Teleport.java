public class Teleport {
    World world;
    public Teleport(World world){
        this.world = world;
        weaponX=world.weaponX + 5 + world.weaponWidth;
        weaponY = world.weaponY;
    }
    int weaponX;
    int weaponY;
    public static final int width=40;
    public static final int height=55;
    public static final int drawWidth = 20;
    public static final int drawHeight =30 ;
    int x;
    int y;
}
