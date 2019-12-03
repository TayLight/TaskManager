package taskmanager;

import taskmanager.task.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.LinkedList;

public class GUI extends JFrame {
    private JList<Task> listTask;
    private JPanel panel1;
    private JButton editTaskButton;
    private JButton exitButton;
    private JButton deleteTaskButton;
    private JButton addTaskButton;
    Manager manager = new ClientManager();

    public GUI() {
        super("TASK MANAGER");
        this.pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(panel1);
        setSize(400,300);
        manager.loadJournalTask();
        setLocationRelativeTo(null);
        setVisible(true);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name =JOptionPane.showInputDialog(GUI.this,"Введите имя задачи" );
                String description =JOptionPane
                        .showInputDialog(GUI.this,"Введите описание задачи" );
                StringParser stringParser= new StringParser();
                LocalTime time = stringParser.timeParse(JOptionPane
                        .showInputDialog(GUI.this,"Введите время (HH:MM)" ));
                Task task = new Task(name,description,time);
            }
        });
    }

    public static void main(String[] argv)
    {
        GUI gui = new GUI();
    }
}
