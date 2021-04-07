public class Poison {
    public Poison(World world){
        weaponX=world.weaponX + 10 + 2*world.weaponWidth;
        weaponY = world.weaponY+2;
    }
    int weaponX;
    boolean explosion = false;
    double dt;
    int x;
    int weaponY;
    double y=0;
    public static final int WIDTH =35;
    public static final int HEIGHT =50;
}
