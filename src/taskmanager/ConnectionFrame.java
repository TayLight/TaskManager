package taskmanager;

import javax.swing.*;
import java.awt.*;

public class ConnectionFrame extends JFrame {
    private JProgressBar progressBar1;
    private JLabel statusField;
    private JPanel panel1;
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();


    public ConnectionFrame() throws HeadlessException {
        super("Подключение к серверу");
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setSize((sizeScreen.width / 2) - 400, (sizeScreen.height / 4)-100);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
