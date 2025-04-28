package listeners;

import gui.Input;
import gui.InputList;

import java.awt.event.ActionEvent;

public class UndoListener extends ButtonListener{
    public UndoListener(InputList... list) {
        this.setText("Undo");
        this.addActionListener(this);
        this.list = list;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        for(InputList l: list) {
            switch (l.getStack().peek().undo()) {
                case Undo.ADD:
                    l.getModel().removeElement(l.getStack().peek().input());
                    l.getStack().pop();
                    if(this.hasChild(l)) {
                        InputList temp = l.getChildList();
                        temp.getModel().removeElement(temp.getStack().peek().input());
                        temp.getStack().pop();
                    }
                    break;
                case Undo.REMOVE:
                    l.addListElement(l.getStack().peek().input());
                    l.getStack().pop();
                    if(this.hasChild(l)) {
                        InputList temp = l.getChildList();
                        temp.addListElement(temp.getStack().peek().input());
                        temp.getStack().pop();
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
