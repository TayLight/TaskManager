package taskmanager;

import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;

public class GUI extends JFrame {
    /**Стандартное разрешение экрана
     *
     */
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    /**Список моделей, для работы с выводом на экран
     *
     */
    private DefaultListModel model = new DefaultListModel();
    /**Вывод списка на экран
     *
     */
    private JList listTask;
    /**Панель, содержащая элементы управления
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
    /** Кнопка удаления задачи
     *
     */
    private JButton deleteTaskButton;
    /**Кнопка добавления задачи
     *
     */
    private JButton addTaskButton;
    /** Менеджер , для работы с сервером
     *
     */
    Manager manager;

    /** Конструктор графического интерфейса
     * @param manager менеджер для работы с сервером
     */
    public GUI(Manager manager) {
        super("TASK MANAGER");
        this.pack();
        this.manager = manager;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((sizeScreen.width / 2)-100, sizeScreen.height / 2);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        updateList(manager.loadTaskJournal());
        setVisible(true);
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {

            }

            public void windowClosed(WindowEvent event) {

            }

            public void windowClosing(WindowEvent event) {
                Object[] options = { "Да", "Нет!" };
                int n = JOptionPane
                        .showOptionDialog(event.getWindow(), "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    event.getWindow().setVisible(false);
                    manager.closeSession();
                    System.exit(0);
                }
            }

            public void windowDeactivated(WindowEvent event) {

            }

            public void windowDeiconified(WindowEvent event) {

            }

            public void windowIconified(WindowEvent event) {

            }

            public void windowOpened(WindowEvent event) {

            }

        });
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
                Object[] options = { "Да", "Нет!" };
                int n = JOptionPane
                        .showOptionDialog(GUI.this, "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    GUI.this.setVisible(false);
                    manager.closeSession();
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] argv) throws IOException, ClassNotFoundException {
        Manager manager = new ClientManager();
        GUI gui = new GUI(manager);
    }

    /**Метод обновления графического вывода журнала задач
     * @param journalTask
     */
    private void updateList(LinkedList<Task> journalTask) {
        String tempName;
        int temp = 0;
        for (Task task : journalTask) {
            temp++;
            tempName = (temp) + "." + task.getName() + "[" + task.getTime() + "]";
            model.addElement(tempName);
        }
        listTask.setModel(model);
    }

    /**Метод инициализации журнала задач на экране
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
