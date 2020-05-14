import javax.swing.*;
import java.awt.*;

public class MainFrame {

    public static final int WIDTH = 1440;
    public static final int HEIGHT = 800;
    public static Main m;
    public static MainMenu menu;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            JFrame f = new JFrame();
            f.setSize(WIDTH, HEIGHT);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            m = new Main();
            menu = new MainMenu();

            f.add(m);
            f.add(menu);
            f.setVisible(true);
            menu.setVisible(true);
            m.setVisible(false);

        });
    }
}
