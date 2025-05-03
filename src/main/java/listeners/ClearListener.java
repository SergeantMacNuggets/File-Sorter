package listeners;

import gui.InputList;

import java.awt.event.ActionEvent;

public class ClearListener extends ButtonListener {

    public ClearListener(InputList... list) {
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(InputList l: list) {
            l.getModel().removeAllElements();
            l.getStack().clear();
            if(this.hasChild(l)) {
                InputList temp = l.getChildList();
                temp.getStack().clear();
                temp.getModel().removeAllElements();
            }
        }
    }
}
