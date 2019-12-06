package taskmanager;

import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;

public class GUI extends JFrame {
    /** Разрешение экрана
     *
     */
    private  static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    /** Список моделей, содержащий список задач
     *
     */
    private DefaultListModel model = new DefaultListModel();
    /**Список задач
     *
     */
    private JList listTask;
    /**Панель, содержащая элементы графического вывода
     *
     */
    private JPanel panel1;
    /**Кнопка редактирования задачи
     *
     */
    private JButton editTaskButton;
    /**Кнопка выхода
     *
     */
    private JButton exitButton;
    /**Кнопка удаления задаи
     *
     */
    private JButton deleteTaskButton;
    /**
     * Кнопка добавления задачи
     */
    private JButton addTaskButton;
    Manager manager ;

    /** Графическая форма
     * @throws IOException Ошибка потоков ввода/вывода
     * @throws ClassNotFoundException Класс не найден
     */
    public GUI(Manager manager) throws IOException, ClassNotFoundException {
        super("TASK MANAGER");
        this.pack();
        this.manager = manager;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(sizeScreen.width/2,sizeScreen.height/2);
        manager.loadJournalTask();
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name=null;
                boolean nameInput= false;
                boolean timeInput= false;
                String description = null;
                LocalTime time = null;
                while (!nameInput) {
                    try {
                        name = JOptionPane.showInputDialog(GUI.this, "Введите имя задачи");
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
                        timeInput =true;
                    } catch (Exception error) {
                        JOptionPane.showMessageDialog(GUI.this, "Ошибка ввода времени");

                    }
                    Task task = new Task(name, description, time);
                    JOptionPane.showMessageDialog(GUI.this, "Задача успешно создана! \n Добавление в журнал задач в следующих версиях");
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
                GUI.this.dispose();
            }
        });
    }

    public static void main(String[] argv) throws IOException, ClassNotFoundException {
        Manager manager = new ClientManager();
        GUI gui = new GUI(manager);
    }

    /** Метод обновления списка на форме, в соответствии с журналом задач
     * @param journalTask Получаемый журнал задач
     */
    private void updateList(LinkedList<Task> journalTask)
    {
        String tempName;
        int temp =0;
        for(int i=0; i<journalTask.size(); i++){
            temp++;
            tempName = (temp)+"."+journalTask.get(i).getName();
            model.addElement(tempName);
        }
        listTask.setModel(model);
    }

    /**Метод инициализации графического вывода списка на форме
     *
     */
    private void createUIComponents() {
        listTask = new JList();
        listTask.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Список задач"),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
    }
}
