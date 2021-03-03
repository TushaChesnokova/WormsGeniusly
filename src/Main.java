import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame frame = new JFrame();
        frame.setSize(1000, 700);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        World world = new World(windowWidth, windowHeight);
        frame.add(world);
        frame.setUndecorated(true);
        frame.addMouseListener(world.c);
        frame.addMouseMotionListener(world.c);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        while (true) {
            world.dt++;
            for (int i = 0; i < world.n; i++) {
                world.worms[i].dt++;
            }
            world.bomb.dt++;
            for(int i=0; i<3; i++)
                if(world.poisonM[i] != null)
                    world.poisonM[i].dt++;
           if( world.bomb.rectPressed == true) {
                world.bomb.rectX++;
                if(world.bomb.rectX>=world.bomb.rectWidth){
                    world.bomb.rectPressed=false;
                    world.bomb.v= world.bomb.maxv*(world.bomb.rectX*1.0/world.bomb.rectWidth);
                    world.bomb.rectX=0;
                    world.bombB = false;
                    world.bomb.realised = true;
                    world.target.catetS = world.bomb.y + world.target.width / 2 - world.controllerWorm.y;
                    world.target.catetC = world.bomb.x + world.target.width / 2 - world.controllerWorm.x + world.bomb.width / 2;
                    world.target.hypotenuse = Math.sqrt(Math.pow(world.target.catetC, 2) + Math.pow(world.target.catetS, 2));
                    world.target.sin = world.target.catetS / world.target.hypotenuse;
                    world.target.cos = world.target.catetC / world.target.hypotenuse;
                    world.bomb.x = world.controllerWorm.x - world.bomb.width / 2;
                    world.bomb.y = world.controllerWorm.y;
                    world.bomb.dt = 0;
                    world.bomb.vy = world.bomb.v * world.target.sin;
                    world.bomb.vx = world.bomb.v * world.target.cos;
                }
            }
            world.c.update();
            Thread.sleep(1000 / 60);
            frame.repaint();
        }
    }
}
