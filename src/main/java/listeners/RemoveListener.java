package listeners;

import gui.InputList;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RemoveListener extends ButtonListener {

    public RemoveListener(InputList... list) {
        this.list = list;
        this.setText("Remove");
        this.addActionListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (InputList l : list) {
                DefaultListModel<String> temp = l.getModel();
                int selectedIndex = l.getList().getSelectedIndex();
                l.getStack().push(new Data(Undo.REMOVE, l.getModel().get(selectedIndex)));
                temp.removeElementAt(selectedIndex);
                if (this.hasChild(l)) {
                    InputList child = l.getChildList();
                    child.getStack().push(new Data(Undo.REMOVE,child.getModel().get(selectedIndex)));
                    child.getModel().removeElementAt(selectedIndex);
                }
            }
        } catch (ArrayIndexOutOfBoundsException x) {
            System.out.println("null");
        }
    }
}
