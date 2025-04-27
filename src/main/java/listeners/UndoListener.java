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
            switch (l.getStack("ENUM").peek()) {
                case Undo.ADD:
                    l.getModel().removeElement(l.getStack("STRING").peek());
                    this.pop(l);
                    if(this.hasChild(l)) {
                        InputList temp = l.getChildList();
                        temp.getModel().removeElement(temp.getStack("STRING").peek());
                        this.pop(temp);
                    }
                    break;
                case Undo.REMOVE:
                    l.addListElement((String) l.getStack("STRING").peek());

                    this.pop(l);
                    if(this.hasChild(l)) {
                        InputList temp = l.getChildList();
                        temp.addListElement((String) temp.getStack("STRING").peek());
                        this.pop(temp);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void pop(InputList l) {
        l.getStack("ENUM").pop();
        l.getStack("STRING").pop();
    }
}
