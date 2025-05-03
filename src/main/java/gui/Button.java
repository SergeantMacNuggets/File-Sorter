package gui;

import listeners.AddListener;
import listeners.ClearListener;
import listeners.RemoveListener;
import listeners.UndoListener;

import javax.swing.*;



class AddButton extends JButton {
    AddButton(InputList... list) {
        this.setText("Add");
        this.addActionListener(new AddListener(list));
    }
}

class RemoveButton extends JButton {
    RemoveButton(InputList... list) {
        this.setText("Remove");
        this.addActionListener(new RemoveListener(list));
    }
}

class ClearButton extends JButton {
    ClearButton(InputList... list) {
        this.setText("Clear");
        this.addActionListener(new ClearListener(list));
    }
}

class UndoButton extends JButton {
    UndoButton(InputList... list) {
        this.setText("Undo");
        this.addActionListener(new UndoListener(list));
    }
}