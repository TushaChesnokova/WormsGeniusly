public class Arrow {
    World world;
    int x=0;
    int y;
    int weaponX;
    int weaponY;
    int width = 120;
    int height = 20;
    public Arrow(World world) {
        this.world = world;
        weaponX = world.weaponX+5;
        weaponY = world.weaponY+world.weaponHeight+10;
    }
}
