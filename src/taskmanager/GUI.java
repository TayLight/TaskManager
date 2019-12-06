package taskmanager;

import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;

public class GUI extends JFrame {
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    private DefaultListModel model = new DefaultListModel();
    private JList listTask;
    private JPanel panel1;
    private JButton editTaskButton;
    private JButton exitButton;
    private JButton deleteTaskButton;
    private JButton addTaskButton;
    Manager manager = new ClientManager();

    public GUI() throws IOException, ClassNotFoundException {
        super("TASK MANAGER");
        this.pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(sizeScreen.width / 2, sizeScreen.height / 2);
        //manager.loadJournalTask();
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = null;
                boolean nameInput = false;
                boolean timeInput = false;
                String description = null;
                LocalTime time = null;
                while (!nameInput) {
                    try {
                        name = JOptionPane.showInputDialog(GUI.this, "Введите имя задачи");
                        System.out.println("+" + name);
                        if (name.isEmpty()) throw new NameTaskException("Не введено имя");
                        else nameInput = true;
                    } catch (NameTaskException error) {
                        JOptionPane.showMessageDialog(GUI.this, "Ошибка ввода имени");

                    }
                }
                description = JOptionPane
                        .showInputDialog(GUI.this, "Введите описание задачи");
                while (!timeInput) {
                    try {
                        StringParser stringParser = new StringParser();
                        time = stringParser.timeParse(JOptionPane
                                .showInputDialog(GUI.this, "Введите время (HH:MM)"));
                        timeInput = true;
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(GUI.this, "Ошибка ввода времени");

                    }
                    Task task = new Task(name, description, time);
                }
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GUI.this, "Функционал в разработке");
            }
        });
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(GUI.this, "Функционал в разработке");
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.closeSession();
                GUI.this.dispose();
            }
        });
    }

    public static void main(String[] argv) throws IOException, ClassNotFoundException {
        GUI gui = new GUI();
    }

    private void updateList(LinkedList<Task> journalTask) {
        String tempName;
        int temp = 0;
        for (int i = 0; i < journalTask.size(); i++) {
            temp++;
            tempName = (temp) + "." + journalTask.get(i).getName();
            model.addElement(tempName);
        }
        listTask.setModel(model);
    }

    private void createUIComponents() {
        listTask = new JList();
        listTask.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Список задач"),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
    }
}
