package taskmanager;

import taskmanager.exceptions.ItemNotFoundException;
import taskmanager.exceptions.NameTaskException;
import taskmanager.task.Task;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;

public class EditTask extends JFrame {
    /**
     * Стандартное разрешение экрана
     */
    private static Dimension sizeScreen = Toolkit.getDefaultToolkit().getScreenSize();
    private JTextField textFieldDescription;
    private JPanel panel1;
    private JTextField textFieldName;
    private JSpinner spinnerMinute;
    private JSpinner spinnerHour;
    private JButton buttonEdit;
    private JCheckBox actualCheckBox;

    public EditTask(Manager manager, int index, Task task, GUI gui) throws HeadlessException {
        super("Создание задачи");
        pack();
        spinnerHour.setModel(new SpinnerNumberModel(0, 0, 24, 1));
        spinnerMinute.setModel(new SpinnerNumberModel(0, 0, 60, 1));
        setSize((sizeScreen.width / 3) - 100, sizeScreen.height / 3);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        textFieldName.setText(task.getName());
        textFieldDescription.setText(task.getDescription());
        spinnerHour.setValue(task.getTime().getHour());
        spinnerMinute.setValue(task.getTime().getMinute());
        if (!task.getRelevance()) actualCheckBox.setSelected(false);
        else actualCheckBox.setSelected(true);
        setVisible(true);
        buttonEdit.addActionListener(e -> {
            try {
                String name = textFieldName.getText();
                if (name.isEmpty()) throw new NameTaskException("Пустое поле");
                String description = textFieldDescription.getText();
                LocalTime time = LocalTime.of((int) spinnerHour.getValue(), (int) spinnerMinute.getValue());
                if (actualCheckBox.isSelected())
                    manager.updateItem(index, new Task(name, description, time));
                else
                    manager.updateItem(index, new Task(name, description, time, false));
                gui.updateList();
                setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(EditTask.this, "Неправильно введено имя!");
            } catch (NameTaskException ex) {
                JOptionPane.showMessageDialog(EditTask.this, "Такое имя уже существует!");
            } catch (ItemNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                gui.connectToServer();
            }
        });
    }
}
