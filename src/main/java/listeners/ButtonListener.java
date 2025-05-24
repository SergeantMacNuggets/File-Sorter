package listeners;

import gui.InputList;

import java.awt.event.ActionListener;



public abstract class ButtonListener implements ActionListener {
    protected InputList[] list;
    protected boolean hasChild(InputList l) {
        return l.getChildList() != null;
    }
}
