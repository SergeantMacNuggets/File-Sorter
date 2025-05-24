package gui;

import listeners.AddListener;
import listeners.ClearListener;
import listeners.RemoveListener;
import listeners.UndoListener;

import javax.swing.JButton;
class Button extends JButton {
    Button() {
        this.setEnabled(Account.getState());
    }
}

class AddButton extends Button {
    AddButton(InputList... list) {
        super.setText("Add");
        super.addActionListener(new AddListener(list));
    }
}

class RemoveButton extends Button {
    RemoveButton(InputList... list) {
        super.setText("Remove");
        super.addActionListener(new RemoveListener(list));
    }
}

class ClearButton extends Button {
    ClearButton(InputList... list) {
        super.setText("Clear");
        super.addActionListener(new ClearListener(list));
    }
}

class UndoButton extends Button {
    UndoButton(InputList... list) {
        super.setText("Undo");
        super.addActionListener(new UndoListener(list));
    }
}