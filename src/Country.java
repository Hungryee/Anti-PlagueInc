

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Country {
    public int x;
    public int y;
    public String name;
    public ArrayList<TransportNode> transportNodes = new ArrayList<>();

    public int population;
    public int infected = 0;
    public int dead = 0;
    public int countryGrade;
    public int climate;
    public ArrayList<int[]> land = new ArrayList<>();
    public ArrayList<BonusPopup> bonuses = new ArrayList<>();
    Color bg = Color.white;
    int lifetime = 0;

    public Country(int x, int y, String name,int population, int countryGrade, TransportNode... nodes) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.population = population;
        this.countryGrade = countryGrade;
        transportNodes.addAll(Arrays.asList(nodes));
        land = Main.expandRegion(x, y, Color.lightGray);

        if (y < MainFrame.HEIGHT / 6 || y > MainFrame.HEIGHT * 5 / 6) {
            climate = 0;
        } else if (y < MainFrame.HEIGHT / 3 || y > MainFrame.HEIGHT * 2 / 3) {
            climate = 1;
        }else{
            climate = 2;
        }
    }
    public Country(ArrayList<int[]> coords, String name,int population, int countryGrade, TransportNode... nodes) {
        this.x = coords.get(0)[0];
        this.y = coords.get(0)[1];
        this.name = name;
        this.population = population;
        this.countryGrade = countryGrade;
        transportNodes.addAll(Arrays.asList(nodes));
        for (int[] arr:coords){
            land.addAll(Main.expandRegion(arr[0],arr[1],Color.lightGray));
        }
    }
    public void transferInfection(Country c, int amount){
            if (Math.random() < Main.v.transportationChances[0]) {
                infect(c, (amount), 0);
                return;
            }
            if (Math.random() < Main.v.transportationChances[1]) {
                if (transportNodes.size() > 0 && c.transportNodes.size() > 0) {
                    ArrayList<Airport> airportsFrom = transportNodes.stream().filter((t) -> t instanceof Airport).map((o) -> (Airport) o).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Airport> airportsTo = c.transportNodes.stream().filter((t) -> t instanceof Airport).map((o) -> (Airport) o).collect(Collectors.toCollection(ArrayList::new));
                    if (airportsFrom.size() > 0 && airportsTo.size() > 0) {
                        Airport nodeFrom = airportsFrom.get(new Random().nextInt(airportsFrom.size()));
                        Airport nodeTo = airportsTo.get(new Random().nextInt(airportsTo.size()));
                        nodeFrom.launchTransport(nodeTo);
                    }
                }
                return;
            }
            if (Math.random() < Main.v.transportationChances[2]) {
                if (transportNodes.size() > 0 && c.transportNodes.size() > 0) {
                    ArrayList<Port> portsFrom = transportNodes.stream().filter((t) -> t instanceof Port).map((o) -> (Port) o).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Port> portsTo = c.transportNodes.stream().filter((t) -> t instanceof Port).map((o) -> (Port) o).collect(Collectors.toCollection(ArrayList::new));
                    if (portsFrom.size() > 0 && portsTo.size() > 0) {
                        Port nodeFrom = portsFrom.get(new Random().nextInt(portsFrom.size()));
                        Port nodeTo = portsTo.get(new Random().nextInt(portsTo.size()));
                        nodeFrom.launchTransport(nodeTo);
                    }
                }
            }
    }
    public void infect(Country c, int amount, int way){
        amount = (int) ((amount
                *((6d-c.countryGrade)/2d)
                *Main.v.climateInfectability[c.climate]*
                Main.v.transportWayInfectability[way]));
        int safeAmount = Math.min(Math.min(amount, c.population-c.infected), infected);
        c.infected += safeAmount;
        if (c!=this) {
            infected -= safeAmount/2;
        }
        c.repaint();
    }
    public void update(){
        lifetime++;
        if (lifetime>5) {
            Country tmp = Main.countries.get(new Random().nextInt(Main.countries.size()));
            while (tmp==this){
                tmp = Main.countries.get(new Random().nextInt(Main.countries.size()));
            }
            transferInfection(tmp, 300 + new Random().nextInt(450));

            double factor = new Random().nextDouble()*(6d-countryGrade)/2d;
            infected += Main.v.innerInfectabilityRate * infected;

            dead+=factor*Main.v.mortality * infected;
            infected -= factor*Main.v.mortality*infected;


            if (Math.random()<0.5) {
                infected -= infected*(4-factor) * Main.v.medicineEffect;
            }


            infected = Math.max(0, Math.min(infected,population));
            dead = Math.max(0,Math.min(dead,population));

            bg = new Color((1 - 1f * dead / population), (1 - 1f * infected / population), (1 - 1f * infected / population));

            if (Math.random()<Main.v.popupChance && bonuses.size()<3){
                int[] coords = land.get(new Random().nextInt(land.size()));
                if (Main.v.isAutoPickupEnabled){
                    new BonusPopup(coords[0], coords[1]).activate();
                }else {
                    bonuses.add(new BonusPopup(coords[0], coords[1]));
                }
            }
            if (bonuses.size()>0&&Math.random()<0.01){
                bonuses.remove(0);
            }
            lifetime = 0;

        }
    }
    public void show(Graphics2D g2d){
        for (TransportNode node:transportNodes){
            node.show(g2d);
        }
        for (BonusPopup b: bonuses){
            b.show(g2d);
        }
    }
    public void repaint(){
        if (!GUI.areCountryGradesEnabled&&!GUI.isClimateShowEnabled) {
            for (int[] arr : land) {
                Main.mapImage.setRGB(arr[0], arr[1], bg.getRGB());
            }
        }
    }

    static class BonusPopup{
        int x;
        int y;
        int size = 20;
        int value;
        public static int maxValue = 4;
        Polygon polygon = new Polygon();
        public BonusPopup(int x, int y){
            this.x = x;
            this.y = y;
            value = 1+new Random().nextInt(maxValue);
            polygon.addPoint(x,y);
            polygon.addPoint(x+size*3/4,y-size);
            polygon.addPoint(x,y-size*2);
            polygon.addPoint(x-size*3/4,y-size);
        }
        public void show(Graphics2D g2d){
            g2d.setColor(Color.CYAN);
            g2d.fill(polygon);
        }
        public void activate(){
            Main.upgradePts+=value;
        }
    }
}
