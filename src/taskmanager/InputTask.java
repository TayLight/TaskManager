package taskmanager;

import taskmanager.conrollers.Manager;
import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalTime;

public class InputTask extends JFrame {
    /**Стандартное разрешение экрана
     *
     */
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    private JTextField descriptionField;
    private JPanel panel1;
    private JTextField timeField;
    private JTextField nameField;
    private JButton addButton;
    private JLabel timeLabel;
    private JLabel descriptionLabel;
    private JLabel nameLabel;

    public InputTask(Manager manager) throws HeadlessException {
        super("Создание задачи");
        pack();
        setSize((sizeScreen.width / 3)-100, sizeScreen.height / 3);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    if(name.isEmpty()) throw new NameTaskException("Пустое поле");
                    String description = descriptionField.getText();
                    LocalTime time = StringParser.timeParse(timeField.getText());
                    manager.checkUniqueName(name);
                    manager.addItem(new Task(name, description, time));
                    setVisible(false);
                } catch (NumberFormatException | DateTimeException | NameTaskException ex) {
                    JOptionPane.showMessageDialog(InputTask.this, "Неверный ввод!");
                }
            }
        });
    }


}
