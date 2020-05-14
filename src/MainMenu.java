import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {
    BufferedImage bg = Main.mapImage;
    JButton start = new JButton("START");
    JButton tutorial = new JButton("HELP");
    JList<String> leaderboards = new JList<>();
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
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.menu.setVisible(false);
                MainFrame.m.setVisible(true);
            }
        });

        add(start);
        start.setVisible(true);

        leaderboards.setListData(new String[]{
                "Hungryee - 100",
                "Dimas - 150",
                "Andrej - 200",
                "Artem - 999",
                "Hungryee - 100",
                "Dimas - 150",
                "Andrej - 200",
                "Artem - 999",
                "Hungryee - 100",
                "Dimas - 150",
                "Andrej - 200",
                "Artem - 999",
                "Hungryee - 100",
                "Dimas - 150",
                "Andrej - 200",
                "Artem - 999",
                "Hungryee - 100",
                "Dimas - 150",
                "Andrej - 200",
                "Artem - 999"
        });

        leaderboards.setBorder(BorderFactory.createLineBorder(Color.orange, 5,true));
        leaderboards.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboards.setVisible(true);
        leaderboards.setSize(200,500);
        leaderboards.setBounds(100,MainFrame.HEIGHT/2-leaderboards.getHeight()/2,leaderboards.getWidth(),leaderboards.getHeight());
        add(leaderboards);

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

