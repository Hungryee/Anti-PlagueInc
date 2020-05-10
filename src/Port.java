import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Port extends TransportNode{
    ArrayList<Voyage> ownVoyages = new ArrayList<>();
    ArrayList<int[][]> region;
    Map<Port, ArrayList<int[]>> paths;
    public Port(int x, int y){
        c = Color.MAGENTA;
        this.x = x;
        this.y = y;
        paths = new HashMap<>();

    }
    @Override
    public void show(Graphics2D g2d){
        super.show(g2d);
        for (int i = ownVoyages.size()-1; i >=0 ; i--) {
            ownVoyages.get(i).show(g2d);
        }
    }
    public void launchTransport(Port destination) {
        Voyage v = new Voyage(this, destination);
        ownVoyages.add(v);
    }
    public ArrayList<int[][]> expandRegionForVoyagePath(int x, int y, int destX, int destY, Color bg){
        BufferedImage copy = new BufferedImage(Main.mapImage.getWidth(), Main.mapImage.getHeight(), Main.mapImage.getType());
        Graphics g = copy.getGraphics();
        g.drawImage(Main.mapImage, 0, 0, null);
        g.dispose();

        ArrayList<int[][]> result = new ArrayList<>();
        int[][] first = new int[][]{{x,y},{x,y}};
        if (copy.getRGB(x,y)==bg.getRGB()) {
            copy.setRGB(x,y,Color.green.getRGB());
        }
        ArrayList<int[]> queue = new ArrayList<>();
        queue.add(first[0]);
        result.add(first);
        while(!queue.isEmpty()) {
            int[] current = queue.get(0);
            if (current[0]==destX&&current[1]==destY){
                break;
            }


            queue.remove(0);
            int j = current[0];
            int i = current[1];
            for (int[] arr : new int[][]{
                    {j, i - 1},
                    {j, i + 1},
                    {j - 1, i},
                    {j + 1, i},
                    {j - 1, i - 1},
                    {j + 1, i -1},
                    {j - 1, i + 1},
                    {j + 1, i + 1},
            }) {
                if (arr[1]==-1){
                    arr[1]=Main.HEIGHT-1;
                }
                if (arr[1]==Main.HEIGHT){
                    arr[1]=0;
                }
                if (arr[0]==-1){
                    arr[0]=Main.WIDTH-1;
                }
                if (arr[0]==Main.WIDTH){
                    arr[0]=0;
                }
                if (arr[1] >= 0 && arr[1] <= Main.HEIGHT && arr[0] >= 0 && arr[0] <= Main.WIDTH && copy.getRGB(arr[0],arr[1]) == bg.getRGB()) {
                    copy.setRGB(arr[0],arr[1], Color.blue.getRGB());
                    queue.add(arr);
                    result.add(new int[][]{arr,current});
                }
            }
        }
        return result;
    }
    public ArrayList<int[]> findPath(int destX, int destY){
        ArrayList<int[]> result = new ArrayList<>();
        int[] current = new int[]{destX,destY};
        while (current[0]!=x||current[1]!=y) {
            for (int[][] arr : region) {
                if (arr[0][0] == current[0] && arr[0][1] == current[1]) {
                    result.add(current);
                    current = arr[1];
                    break;
                }
            }
            if (current[0] == x && current[1] == y) {
                break;
            }
        }
        return result;
    }
    class Voyage{
        Image img;

        {
            try {
                img = ImageIO.read(new File("src/ship.png"));
                img = img.getScaledInstance(6,26,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int speed;
        Port from;
        Port to;
        int time = 0;
        int delay;
        ArrayList<int[]> path = new ArrayList<>();
        Voyage(Port from, Port to){
            this.from = from;
            this.to = to;
            if (from.paths.containsKey(to)) {
                path = from.paths.get(to);
                delay = path.size();
            }
            else{
                path = new ArrayList<>();
                delay = path.size();
            }
            speed = 2+new Random().nextInt(4);
        }
        public void show(Graphics2D g2d) {
            if (time>=delay){
                time = 0;
                from.findCountry().infect(to.findCountry(),(int) ((100+Math.random()*100)),2);
                ownVoyages.remove(this);
            }
            if (path.size()>0) {
                for (int i = path.size() - 1; i >= time; i-=3) {
                    g2d.setColor(new Color(255,255,0,80));
                    g2d.fillRect(path.get(i)[0]-1, path.get(i)[1]-1, 2, 2);
                }
                AffineTransform tr = g2d.getTransform();
                tr.translate(path.get(time)[0], path.get(time)[1]);
                if (time+1<path.size()) {
                    tr.rotate(3*Math.PI/2+Math.atan2(path.get(time+1)[1]-path.get(time)[1],path.get(time+1)[0]-path.get(time)[0]));
                }
                tr.translate(-3, -13);
                g2d.drawImage(img, tr, null);
                tr.translate(-path.get(time)[0] + 3, -path.get(time)[1] + 13);
//                g2d.setColor(Color.yellow);
//                g2d.fillOval(path.get(time)[0]-5,path.get(time)[1]-5,10,10);
            }
            time+=speed;
        }

    }
}
