package listeners;

import gui.FileMap;
import gui.InputList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RemoveListener extends ButtonListener implements MouseListener {

    public RemoveListener(InputList... list) {
        this.list = list;
    }

    private void perform() {
        try {
            for (InputList l : list) {
                JList<String> temp = l.getList();
                int selectedIndex = temp.getSelectedIndex();

                l.push(new Data(Undo.REMOVE, temp.getSelectedValue()));

                l.getModel().removeElementAt(selectedIndex);

                if(l.getUndoCount() < l.getUndoLimit()) {
                    l.setUndoCount(l.getUndoCount()+1);
                }

                if (!l.childInputState() && hasChild(l)) {
                    InputList child = l.getChildList();
                    child.push(new Data(Undo.REMOVE,child.getModel().get(selectedIndex)));
                    child.getModel().removeElementAt(selectedIndex);

                }
            }
        } catch (NullPointerException x) {
            System.out.println("null");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        perform();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void mouseClicked(MouseEvent e) {

        if(SwingUtilities.isRightMouseButton(e)) {
            JList<String> temp = (JList<String>) e.getSource();
            temp.setSelectedIndex(temp.locationToIndex(e.getPoint()));

            if(e.getClickCount() % 2 == 0 && !e.isConsumed()) {
                FileMap.getInstance().remove(temp.getSelectedValue());
                perform();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
