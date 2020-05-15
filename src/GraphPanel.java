import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    public ArrayList<Integer> infectedGraphPts = new ArrayList();
    public ArrayList<Integer> deadGraphPts = new ArrayList();
    public double scaleX = 1;
    public double scaleY = 1;
    public GraphPanel(){
        setVisible(false);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (infectedGraphPts.size()>=2000){
            infectedGraphPts.remove(0);
        }
        if (isVisible()) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2));
            for (int i1 = infectedGraphPts.size() - 1; i1 >= 1; i1--) {
                int value = infectedGraphPts.get(i1);
                int prevX = (int) ((i1-1)/scaleX);
                int prevY = (int) (getHeight() - 10 - (infectedGraphPts.get(i1-1))/scaleY);
                int x = (int) (i1/scaleX);
                int y = (int) (getHeight() - 10 - value/scaleY);
                if (x>=getWidth()){
                    scaleX*=1.5;
                }
                if (y<=0){
                    scaleY *= 1.5;
                }
                g2d.fillOval(x-1, y-1, 2, 2);
                g2d.drawLine(prevX,prevY,x,y);
            }
            g2d.setColor(Color.red);
            for (int i1 = deadGraphPts.size() - 1; i1 >= 1; i1--) {
                int value = deadGraphPts.get(i1);
                int prevX = (int) ((i1-1)/scaleX);
                int prevY = (int) (getHeight() - 10 - (deadGraphPts.get(i1-1))/scaleY);
                int x = (int) (i1/scaleX);
                int y = (int) (getHeight() - 10 - value/scaleY);
                if (x>=getWidth()){
                    scaleX*=1.5;
                }
                if (y<=0){
                    scaleY *= 1.5;
                }
                g2d.fillOval(x-1, y-1, 2, 2);
                g2d.drawLine(prevX,prevY,x,y);
            }
        }
    }
}
