package listeners;

import gui.FileMap;
import gui.InputList;

import javax.swing.DefaultListModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UndoListener extends ButtonListener implements KeyListener {
    public UndoListener(InputList... list) {
        this.list = list;
    }

    public void perform() {
        for(InputList l: list) {
            if (l.getUndoCount() > 0){
                switch (l.getStack().peek().undo()) {
                    case Undo.ADD:
                        l.getModel().removeElement(l.getStack().peek().input());
                        l.pop();
                        l.setUndoCount(l.getUndoCount()-1);
                        if (!l.childInputState() && hasChild(l)) {
                            InputList temp = l.getChildList();
                            temp.getModel().removeElement(temp.getStack().peek().input());
                            temp.pop();
                        }
                        break;
                    case Undo.REMOVE:
                        String input = l.peek().input();
                        DefaultListModel<String> temp = l.peek().model();
                        l.getModel().addElement(input);
                        if(temp != null) {
                            FileMap.getInstance().put(input, temp);
                        }
                        l.pop();
                        l.setUndoCount(l.getUndoCount()-1);
                        if (!l.childInputState() && hasChild(l)) {
                            InputList tempList = l.getChildList();
                            tempList.addListElement(tempList.peek().input());
                            tempList.pop();
                        }
                        break;
                }
            } else return;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        perform();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_Z) {
            perform();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
