public class Hill {
    int pluralHill = 10;
    int hill = 20;
    World world;
    int width = 27;
    int height = 20;
    int hillX;
    int hillY;
    int weaponX;
    int weaponY;
    int weaponXP;
    int weaponYP;
    public Hill(World world) {
        this.world = world;
        weaponX=world.weaponX + 5 + world.weaponWidth;
        weaponY = world.weaponY+world.weaponHeight+10;
        weaponXP=world.weaponX + 10 + 2*world.weaponWidth;
        weaponYP = world.weaponY+world.weaponHeight+10;
    }
}
