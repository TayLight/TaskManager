package taskmanager;

import javax.swing.*;
import java.util.Vector;

public class ListItems<T> extends AbstractListModel<T> {

    private Vector<T> items = new Vector<T>();

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getElementAt(int index) {
        return items.elementAt(index);
    }

    public void clear() {
        int index1 = items.size() - 1;
        items.removeAllElements();
        if (index1 >= 0) {
            fireIntervalRemoved(this, 0, index1);
        }
    }

    public void addElement(T tempName) {
        int index = items.size();
        items.addElement(tempName);
        fireIntervalAdded(this, index, index);
    }
}
