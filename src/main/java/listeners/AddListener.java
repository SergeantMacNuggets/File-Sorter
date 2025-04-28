package listeners;

import gui.InputList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddListener extends ButtonListener implements ActionListener {
    public AddListener (InputList... list) {
        this.list = list;
        this.setText("Add");
        this.addActionListener(this);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (InputList l : list) {
                l.check();
                if(this.hasChild(l)) {
                    l.getChildList().check();
                }
            }
        } catch (NullPointerException x) {
            System.out.println("Input Blank");
            return;
        }
        for(InputList l: list) {
            l.addAll();
            l.getStack().push(new Data(Undo.ADD,l.getString()));
            if(this.hasChild(l)) {
                InputList temp = l.getChildList();
                temp.addAll();
                temp.getStack().push(new Data(Undo.ADD, temp.getString()));
            }
        }
    }
}