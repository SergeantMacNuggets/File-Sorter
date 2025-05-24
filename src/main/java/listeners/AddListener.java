package listeners;

import gui.InputList;

import java.awt.event.ActionEvent;

public class AddListener extends ButtonListener {
    public AddListener (InputList... list) {
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (InputList l : list) {
                l.check();
                if(!l.childInputState() && hasChild(l)) {
                    l.getChildList().check();
                }
            }

            for (InputList l : list) {
                l.addAll(true);
                l.push(new Data(Undo.ADD, l.getString()));

                l.setUndoCount(l.getUndoCount() + 1);

                if (!l.childInputState() && hasChild(l)) {
                    InputList temp = l.getChildList();
                    temp.addAll(false);
                    temp.push(new Data(Undo.ADD, temp.getString()));
                }
            }
        } catch (NullPointerException x) {
            System.out.println("Input Blank");
        }
    }
}