public class Worm {
    Team team;
    Direction direction = Direction.LEFT;
    int health = 100;
    public static final int width = 40;
    public static final int height = 35;
    double x;
    double v;
    double dt;
    double y;

    public Worm(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
