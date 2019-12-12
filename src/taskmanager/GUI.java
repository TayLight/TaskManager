package taskmanager;

import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.LinkedList;

import static javax.swing.JOptionPane.showInputDialog;

public class GUI extends JFrame {
    boolean isConnection = false;
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
    private JButton connectionButton;
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
        setVisible(true);
        try
        {
            manager.startWork();
            isConnection = true;
            System.out.println("Загружено");
            updateList();
        } catch (IOException e)
        {
            connectionLost();
        }
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
                    if (isConnection) manager.finalWork();
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
                if (!isConnection) JOptionPane.showMessageDialog(GUI.this, "Нет соединения с сервером!");
                else {InputTask inputTask = new InputTask(manager);}
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnection) JOptionPane.showMessageDialog(GUI.this, "Нет соединения с сервером!");
                else {
                    int index = Integer.parseInt(JOptionPane.
                            showInputDialog(GUI.this, "Введите индекс удаляемой задачи"));
                    try {
                        manager.deleteItem(index--);
                    } catch (ItemNotFoundException ex) {
                        JOptionPane.showMessageDialog(GUI.this, "Неверный ввод");
                    }
                }
            }
        });
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnection) JOptionPane.showMessageDialog(GUI.this, "Нет соединения с сервером!");
                else {JOptionPane.showMessageDialog(GUI.this, "Функционал в разработке");}
            }
        });
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isConnection) {
                    try {
                        manager.startWork();
                        isConnection = true;
                        updateList();
                    } catch (IOException ex) {
                        connectionLost();
                    }
                }
                else JOptionPane.
                        showMessageDialog(GUI.this, "Соединение уже установлено!");
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
                    if(isConnection) manager.finalWork();
                    System.exit(0);
                }
            }
        });
    }

    public static void main(String[] argv)  {
        Manager manager = new ClientManager();
        GUI gui = new GUI(manager);
    }

    /**Метод обновления графического вывода журнала задач
     */
    private void updateList() {
        LinkedList<Task> journalTask = (LinkedList<Task>) manager.getTasks();
        String tempName;
        int temp = 0;
        model.clear();
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

    private void connectionLost()
    {
        JOptionPane.
                showMessageDialog(GUI.this, "Соединение невозможно! \n Возможно сервер в неактивном состоянии");
        model.clear();
        model.addElement("Нет соединения с сервером.");
        listTask.setModel(model);
        isConnection = false;
    }
}
