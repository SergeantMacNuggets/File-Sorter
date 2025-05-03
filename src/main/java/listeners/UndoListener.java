package listeners;

import gui.Input;
import gui.InputList;

import java.awt.event.ActionEvent;

public class UndoListener extends ButtonListener{
    public UndoListener(InputList... list) {
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(InputList l: list) {
            if (l.getUndoCount() > 0){
                switch (l.getStack().peek().undo()) {
                    case Undo.ADD:
                        l.getModel().removeElement(l.getStack().peek().input());
                        l.pop();
                        l.setUndoCount(l.getUndoCount()-1);
                        System.out.println(l.getUndoCount());
                        if (this.hasChild(l)) {
                            InputList temp = l.getChildList();
                            temp.getModel().removeElement(temp.getStack().peek().input());
                            temp.pop();
                        }
                        break;
                    case Undo.REMOVE:
                        l.addListElement(l.getStack().peek().input());
                        l.pop();
                        l.setUndoCount(l.getUndoCount()-1);
                        if (this.hasChild(l)) {
                            InputList temp = l.getChildList();
                            temp.addListElement(temp.getStack().peek().input());
                            temp.pop();
                        }
                        break;

                    default:
                        break;
                }
            } else return;
        }
    }
}
