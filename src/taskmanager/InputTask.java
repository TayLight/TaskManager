package taskmanager;

import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalTime;

public class InputTask extends JFrame {
    /**
     * Стандартное разрешение экрана
     */
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Поле ввода описания новой задачи
     */
    private JTextField descriptionField;
    private JPanel panel1;
    /**
     * Поле ввода времени задачи
     */
    private JTextField timeField;
    /**
     * Поле ввода имени задачи
     */
    private JTextField nameField;
    /**
     * Кнопка добавления задачи
     */
    private JButton addButton;
    private JLabel timeLabel;
    private JLabel descriptionLabel;
    private JLabel nameLabel;
    private JLabel fieldSeparator;
    private JSpinner spinnerMinute;
    private JSpinner spinnerHour;

    public InputTask(Manager manager, GUI gui) throws HeadlessException {
        super("Создание задачи");
        pack();
        spinnerHour.setModel(new SpinnerNumberModel(0,0,24,1));
        spinnerMinute.setModel(new SpinnerNumberModel(0,0,60,1));
        setSize((sizeScreen.width / 3) - 100, sizeScreen.height / 3);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);
        spinnerHour.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    if (name.isEmpty()) throw new NameTaskException("Пустое поле");
                    String description = descriptionField.getText();
                    LocalTime time = LocalTime.of((int) spinnerHour.getValue(),(int) spinnerMinute.getValue());
                    manager.addItem(new Task(name, description, time));
                    setVisible(false);
                    gui.updateList();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(InputTask.this, "Неправильно введено имя!");
                } catch (NameTaskException ex) {
                    JOptionPane.showMessageDialog(InputTask.this, "Такое имя уже существует!");
                } catch (IOException ex) {
                    gui.reconnectToServer();
                }
            }
        });
    }
}
