import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GUI extends JPanel {
    public static JPanel upgradesPanel;

    public static JPanel mutationsPanel;
    public static boolean isClimateShowEnabled = false;
    public static boolean areCountryGradesEnabled = false;
    public static boolean areSeaWaysEnabled = false;
    public static boolean areAirWaysEnabled = false;
    public static JProgressBar infectedMenu = new JProgressBar();
    public static JProgressBar deadMenu = new JProgressBar();
    public static JLabel upgradePoints = new JLabel("");
    public static JMenu displayOptions = new JMenu("Click for more options...");
    public GUI(){
        upgradesPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                if (GUI.upgradesPanel.isVisible()) {
                    Graphics2D g2d = (Graphics2D) g;
                    super.paintComponent(g);
                    g2d.setStroke(new BasicStroke(1));
                    for (int i : Main.upgrades.keySet()) {
                        for (Mutation m : Main.upgrades.get(i)) {
                            m.show(g2d);
                        }
                    }
                }

            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIDTH, 700);
            }

        };
        mutationsPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                if (GUI.mutationsPanel.isVisible()) {
                    Graphics2D g2d = (Graphics2D) g;
                    super.paintComponent(g);
                    g2d.setStroke(new BasicStroke(1));
                    for (int i:Main.v.mutations.keySet()){
                        for (Mutation m: Main.v.mutations.get(i)){
                            m.show(g2d);
                        }
                    }

                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(WIDTH, 700);
            }
        };

        setLayout(new BorderLayout());

        add(new BottomBar());

        mutationsPanel.setVisible(false);

        upgradesPanel.setVisible(false);
        upgradesPanel.setSize(WIDTH, 700);
        upgradesPanel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (GUI.upgradesPanel.isVisible()) {
                   for (Mutation m : Main.upgrades.values().stream().flatMap(Collection::stream).collect(Collectors.toList())) {
                       if (m.contains(e.getX(), e.getY())) {
                           m.apply();
                           break;
                       }
                   }
               }
           }
       });

        upgradePoints.setFont(new Font("Arial", Font.BOLD, 20));
        upgradePoints.setVisible(true);


        add(mutationsPanel, BorderLayout.SOUTH);
        add(upgradesPanel, BorderLayout.NORTH);
    }
}
class BottomBar extends JMenuBar{
    BottomBar(){
        setLayout(new FlowLayout());
        add(GUI.upgradePoints);
        GUI.infectedMenu.setMinimum(0);
        GUI.infectedMenu.setMaximum(Main.totalPop);
        GUI.infectedMenu.setStringPainted(true);

        GUI.deadMenu.setMinimum(0);
        GUI.deadMenu.setMaximum(Main.totalPop);
        GUI.deadMenu.setStringPainted(true);
        add(GUI.infectedMenu);
        add(GUI.deadMenu);
        GUI.displayOptions.add(new JButton("Show Climate"){{
            addActionListener(e -> {
                GUI.isClimateShowEnabled =!GUI.isClimateShowEnabled;
                if (GUI.isClimateShowEnabled){
                    for (Country c:Main.countries){
                        if (c.climate==0){
                            for (int[] arr:c.land){
                                Main.mapImage.setRGB(arr[0],arr[1],new Color(39, 70, 220).getRGB());
//                                Main.map[arr[1]][arr[0]] = new Color(39, 70, 220).getRGB();
                            }
                        }
                        if (c.climate==1){
                            for (int[] arr:c.land){
                                Main.mapImage.setRGB(arr[0],arr[1],new Color(150, 77, 220).getRGB());
//                                Main.map[arr[1]][arr[0]] = new Color(150, 77, 220).getRGB();
                            }
                        }
                        if (c.climate==2){
                            for (int[] arr:c.land){
                                Main.mapImage.setRGB(arr[0],arr[1],new Color(220, 27, 63).getRGB());
//                                Main.map[arr[1]][arr[0]] = new Color(220, 27, 63).getRGB();
                            }
                        }
                    }
                }else{
                    for (Country c:Main.countries){
                        for (int[] arr:c.land){
                            Main.mapImage.setRGB(arr[0],arr[1],c.bg.getRGB());
//                            Main.map[arr[1]][arr[0]] = c.bg.getRGB();
                        }
                    }
                }

            });
        }});
        GUI.displayOptions.add(new JButton("Show countries' grades"){{
            addActionListener(e->{
                GUI.areCountryGradesEnabled=!GUI.areCountryGradesEnabled;
                if (GUI.areCountryGradesEnabled){
                    for (Country c:Main.countries){
                        for (int[] arr:c.land){
                            Main.mapImage.setRGB(arr[0],arr[1],new Color(0,255-50*c.countryGrade,0).getRGB());
//                            Main.map[arr[1]][arr[0]] = new Color(0,255-50*c.countryGrade,0).getRGB();
                        }
                    }
                }else{
                    for (Country c:Main.countries){
                        for (int[] arr:c.land){
                            Main.mapImage.setRGB(arr[0],arr[1],c.bg.getRGB());
//                            Main.map[arr[1]][arr[0]] = c.bg.getRGB();
                        }
                    }
                }
            });
        }});
        GUI.displayOptions.add(new JButton("Show air traffic"){{
            addActionListener(e->{
                GUI.areAirWaysEnabled = !GUI.areAirWaysEnabled;
            });
        }});
        GUI.displayOptions.add(new JButton("Show sea traffic"){{
            addActionListener(e->{
                GUI.areSeaWaysEnabled = !GUI.areSeaWaysEnabled;
            });
        }});
        add(GUI.displayOptions);

        add(new JButton("Show upgrades"){{
            addActionListener(e->{
                GUI.mutationsPanel.setVisible(false);
                Main.graph.setVisible(false);
                GUI.upgradesPanel.setVisible(!GUI.upgradesPanel.isVisible());
            });
        }});
        add(new JButton("Show mutations"){{
            addActionListener(e->{
                GUI.upgradesPanel.setVisible(false);
                Main.graph.setVisible(false);
                GUI.mutationsPanel.setVisible(!GUI.mutationsPanel.isVisible());
            });
        }});
        add(new JButton("Show graph"){{
            addActionListener(e->{
                GUI.upgradesPanel.setVisible(false);
                GUI.mutationsPanel.setVisible(false);
                Main.graph.setVisible(!Main.graph.isVisible());
            });
        }});
        add(new JButton("||"){{
            addActionListener(e->{
                Main.speed = (long) 0;
            });
        }});
        add(new JButton(">"){{
            addActionListener(e->{
                Main.speed = (long) 1;
            });
        }});
        add(new JButton(">>"){{
            addActionListener(e->{
                Main.speed = (long) 2;
            });
        }});
        add(new JButton(">>>"){{
            addActionListener(e->{
                Main.speed = (long) 5;
            });
        }});
    }
}
