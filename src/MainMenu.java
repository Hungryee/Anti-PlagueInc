import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainMenu extends JPanel {
    public static ArrayList<String> scoresList = new ArrayList<>();
    BufferedImage bg = Main.mapImage;
    JButton start = new JButton("START");
    JButton tutorial = new JButton("HELP");
    JTextField usernameField = new JTextField();
    public static JList<String> leaderboards = new JList<>();
    JDialog helpDialog = new JDialog(){
        {
            add(new JTextArea("The world is infected by an unknown virus and your aim as a player is to prevent the spread of the disease and elimination of the humanity. The virus is spread with planes, ships and human interaction. The virus mutates with time. However, to counter this you may inflict countermeasures by buying upgrades with OmegaCoins. You may collect them by clicking on the blue bonuses that appear on the map."){{
                setWrapStyleWord(true);
                setLineWrap(true);
                setEditable(false);
                setFont(new Font("Arial", Font.PLAIN, 30));
            }});
        }
    };
    MainMenu(){
        File scores = new File("src/scores.txt");
        try {
            scores.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(scores));
            scoresList = br.lines().collect(Collectors.toCollection(ArrayList::new));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLayout(null);
        Kernel kernel = new Kernel(3, 3,
                new float[] {

                        1f/27f, 1f/27f, 1f/27f,

                        1f/27f, 1f/27f, 1f/27f,

                        1f/27f, 1f/27f, 1f/27f});

        BufferedImageOp op = new ConvolveOp(kernel);

        bg = op.filter(bg, null);
        setSize(MainFrame.WIDTH, MainFrame.HEIGHT);
        start.setForeground(Color.red);
        start.setSize(300,75);
        start.setFont(new Font("Arial", Font.BOLD, 40));
        start.setBounds(MainFrame.WIDTH/2-start.getWidth()/2, MainFrame.HEIGHT/2-start.getHeight()/2,start.getWidth(),start.getHeight());
        start.addActionListener(e -> {
            MainFrame.menu.setVisible(false);
            MainFrame.m.setVisible(true);

            //MainFrame.m.setBounds(0,0,MainFrame.WIDTH, MainFrame.HEIGHT);
            new Thread(()->{
                int x = -MainFrame.WIDTH;
                while (x!=0){
                    MainFrame.m.setBounds(x,0,MainFrame.WIDTH, MainFrame.HEIGHT);
                    x+=10;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();

            MainFrame.gameState = 1;
            Main.username = (usernameField.getText().length()>0?usernameField.getText():"Guest");
        });

        add(start);
        start.setVisible(true);



        leaderboards.setBorder(BorderFactory.createLineBorder(Color.orange, 5,true));
        leaderboards.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboards.setVisible(true);
        leaderboards.setSize(200,500);
        leaderboards.setBounds(100,MainFrame.HEIGHT/2-leaderboards.getHeight()/2,leaderboards.getWidth(),leaderboards.getHeight());
        sortScores();
        add(leaderboards);

        add(new JLabel("LEADERBOARD"){{
            setFont(new Font("Arial", Font.ITALIC, 20));
            setVisible(true);
            setBounds(leaderboards.getX()+20,leaderboards.getY()-50,300,50);
        }});

        helpDialog.setSize(500,500);
        helpDialog.setVisible(false);
        helpDialog.setLocation(MainFrame.WIDTH/2-helpDialog.getWidth()/2, MainFrame.HEIGHT/2-helpDialog.getHeight()/2);

        tutorial.setForeground(Color.red);
        tutorial.setSize(300,75);
        tutorial.setFont(new Font("Arial", Font.BOLD, 40));
        tutorial.setBounds(MainFrame.WIDTH/2-tutorial.getWidth()/2, MainFrame.HEIGHT/2+tutorial.getHeight()/2,tutorial.getWidth(),tutorial.getHeight());
        tutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpDialog.setVisible(true);
            }
        });

        add(tutorial);
        tutorial.setVisible(true);

        usernameField.setSize(300,50);
        usernameField.setBounds(MainFrame.WIDTH/2-usernameField.getWidth()/2,
                MainFrame.HEIGHT/2-start.getHeight()/2-usernameField.getHeight(),
                usernameField.getWidth(),
                usernameField.getHeight());
        usernameField.setVisible(true);
        usernameField.setFont(new Font("Arial", Font.BOLD, 40));
        usernameField.setToolTipText("USERNAME:");
        add(usernameField);

        add(new JLabel("USERNAME:"){{
            setFont(new Font("Arial", Font.ITALIC, 20));
            setVisible(true);
            setBounds(usernameField.getX()+80,usernameField.getY()-50,300,50);
        }});
    }
    public static void writeScoresToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/scores.txt")));
        int score = (int) (100 - 100 * (1d*Main.maxInfectedOverTime)/Main.countries.stream().mapToInt(c -> c.population).sum()
        +Main.upgradePts/2
        -GUI.deadMenu.getPercentComplete()*100);
        MainMenu.scoresList.add(Main.username+"-"+ score);
        MainMenu.sortScores();
        String result = "";
        for (String s:MainMenu.scoresList){
            result+=(s+"\n");
        }
        bw.write(result);
        bw.close();

        MainFrame.gameState=0;
        MainFrame.m.setVisible(false);
        MainFrame.menu.setVisible(true);
    }
    public static void sortScores(){
        scoresList.sort((s1,s2)->{
            String[] tmp1 = s1.split("-");
            String[] tmp2 = s2.split("-");
            int score1 = Integer.parseInt(tmp1[1]);
            int score2 = Integer.parseInt(tmp2[1]);
            return -(score1-score2);
            //sorting in descending order
        });
        leaderboards.setListData(scoresList.toArray(new String[]{}));
        leaderboards.setSize(200,30*scoresList.size());
        leaderboards.setBounds(100,MainFrame.HEIGHT/2-leaderboards.getHeight()/2,leaderboards.getWidth(),leaderboards.getHeight());


    }
    @Override
    protected void paintComponent(Graphics g) {
        if (isVisible()) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(bg, 0, 0, null);
        }
    }
}

