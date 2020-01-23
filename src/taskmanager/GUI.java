package taskmanager;

import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.task.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class GUI extends JFrame {
    Task selectedTask;
    int selectedTaskIndex;
    boolean isConnection = false;
    /**
     * Стандартное разрешение экрана
     */
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Список моделей, для работы с выводом на экран
     */
    private AbstractListModel model;
    /**
     * Вывод списка на экран
     */
    private JList listTask;
    /**
     * Панель, содержащая элементы управления
     */
    private JPanel panel1;
    /**
     * Кнопка редактирования задачи
     */
    private JButton editTaskButton;
    /**
     * Кнопка выхода
     */
    private JButton exitButton;
    /**
     * Кнопка удаления задачи
     */
    private JButton deleteTaskButton;
    /**
     * Кнопка добавления задачи
     */
    private JButton addTaskButton;
    /**
     * Кнопка переподключения
     */
    private JButton connectionButton;
    /**
     * Надпись статуса сервера
     */
    private JLabel statusLabel;
    /**
     * Менеджер , для работы с сервером
     */
    Manager manager;

    /**
     * Конструктор графического интерфейса
     *
     * @param manager менеджер для работы с сервером
     */
    public GUI(Manager manager) {
        super("TASK MANAGER");
        this.pack();
        this.manager = manager;
        listTask.setModel((ListModel) manager);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((sizeScreen.width / 2) - 100, sizeScreen.height / 2);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);
        connectToServer();
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {

            }

            public void windowClosed(WindowEvent event) {

            }

            public void windowClosing(WindowEvent event) {
                Object[] options = {"Да", "Нет!"};
                int n = JOptionPane
                        .showOptionDialog(event.getWindow(), "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    setVisible(false);
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
                else {
                    InputTask inputTask = new InputTask(manager, GUI.this);
                }
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnection) JOptionPane.showMessageDialog(GUI.this, "Соединение с сервером еще не установлено!");
                else {
                    try {
                        int index = listTask.getSelectedIndex();
                        manager.deleteItem(index);
                        updateList();
                    } catch (ItemNotFoundException ex) {
                        JOptionPane.showMessageDialog(GUI.this, "Неверный ввод");
                    } catch (IOException ex) {
                        connectToServer();
                    }
                }
            }
        });
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isConnection) JOptionPane.showMessageDialog(GUI.this, "Нет соединения с сервером!");
                else {
                    selectedTask = (Task) listTask.getSelectedValue();
                    EditTask editTask = new EditTask(manager, listTask.getSelectedIndex(), selectedTask, GUI.this);
                }
            }
        });
        connectionButton.addActionListener(e -> {
            try {
                manager.finalWork();
                connectToServer();
            } catch (IOException ex) {
                connectionLost();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Да", "Нет!"};
                int n = JOptionPane
                        .showOptionDialog(GUI.this, "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options,
                                options[0]);
                if (n == 0) {
                    GUI.this.setVisible(false);
                    System.exit(0);
                }
            }
        });
        listTask.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    selectedTaskIndex = listTask.getSelectedIndex();
                    selectedTask = (Task) listTask.getSelectedValue();
                }
            }
        });
    }

    public static void main(String[] argv) {
        Manager manager = new ClientManager();
        GUI gui = new GUI(manager);
    }

    /**
     * Метод обновления графического вывода журнала задач
     */
    public void updateList() {
        try {
           listTask.updateUI();
        } catch (IllegalArgumentException e) {
            connectToServer();
        }
    }


    /**
     * Метод инициализации журнала задач на экране
     */
    private void createUIComponents() {
        listTask = new JList();
        listTask.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Список задач"),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
    }

    /**
     * Метод, оповещающий пользователя о потере соединения с сервером
     */
    public void connectionLost() {
        JOptionPane.
                showMessageDialog(GUI.this, "Соединение невозможно! \n Возможно сервер в неактивном состоянии");
        DefaultListModel<String> lostConnectionList = new DefaultListModel<>();
        lostConnectionList.addElement("Нет соединения с сервером");
        listTask.setModel(lostConnectionList);
        isConnection = false;
        statusLabel.setText("Сервер недоступен");
    }

    /**
     * Метод переподключения клиента к серверу
     */
    public void connectToServer() {
        ConnectionFrame connectionFrame = new ConnectionFrame();
        try {
            statusLabel.setText("Подключение");
            manager.startWork();
            listTask.setModel((ListModel) manager);
            isConnection = true;
            statusLabel.setText("Сервер онлайн");
            connectionFrame.setVisible(false);
            connectionFrame.dispose();
            updateList();
            GUI.this.setFocusable(true);
        } catch (IOException e) {
            connectionLost();
            connectionFrame.setVisible(false);
            connectionFrame.dispose();
            GUI.this.setFocusable(true);
        }
    }
}
