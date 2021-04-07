public class Worm {
    Team team;
    Direction direction = Direction.LEFT;
    public static final int START_HEALTH = 100;
    int health = START_HEALTH;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 35;
    double x;
    double v;
    double dt;
    double y;

    public Worm(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
