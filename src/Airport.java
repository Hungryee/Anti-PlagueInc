import java.awt.*;
import java.util.ArrayList;

public class Airport extends TransportNode {
    ArrayList<Flight> ownFlights = new ArrayList<>();
    public Airport(int x, int y){
        c = Color.blue;
        this.x = x;
        this.y = y;
    }
    @Override
    public void show(Graphics2D g2d){
        super.show(g2d);
        for (int i = ownFlights.size()-1; i >=0 ; i--) {
            ownFlights.get(i).show(g2d);
        }
    }
    public void launchTransport(Airport destination) {
        Flight f = new Flight(this, destination);
        ownFlights.add(f);
    }


    class Flight{
        int x;
        int y;
        int dx;
        int dy;
        Airport from;
        Airport to;
        float time = 0;
        float delay = 4;
        Flight(Airport from, Airport to){
            this.from = from;
            this.to = to;
            x = this.from.x;
            y = this.from.y;
            dx = to.x-from.x;
            dy = to.y-from.y;
        }
        public void show(Graphics2D g2d) {
            if (time>delay){
                time = 0;
                from.findCountry().infect(to.findCountry(),(int) ((150+Math.random()*250)),1);
                ownFlights.remove(this);
            }
            g2d.setColor(Color.green);
            g2d.setStroke(new BasicStroke((delay-time)));
            g2d.drawLine(from.x,from.y, (int) (from.x+time*dx/delay), (int) (from.y+time*dy/delay));

            time+=0.25;
        }
    }
}
