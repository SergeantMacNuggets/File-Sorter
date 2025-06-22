package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Stack;

public class Table extends JTable {
    private DefaultTableModel model;
    private Stack<StackData> undoStack;
    private int undoLimit;
    Table() {
        undoStack = new Stack<>();
        undoLimit = 0;
    }

    public void setDefaultModel(DefaultTableModel model) {
        this.model = model;
    }

    public void setColumns(Object... columns) {
        for(Object c: columns) {
            model.addColumn(c);
        }
        this.setModel(model);
    }

    public void addRow(Object[] obj) {
        undoStack.push(new StackData(obj, Undo.ADD));
        model.addRow(obj);
        undoLimit = (undoLimit <= 5) ? undoLimit+1 : 5;
    }

    public void removeRow(int selectedIndex) {
        ArrayList<Object> tempObj = new ArrayList<>();
        for(int i=0; i<model.getColumnCount();i++) {
            tempObj.add(model.getValueAt(selectedIndex, i));
        }
        undoStack.push(new StackData(tempObj.toArray(), Undo.REMOVE));
        model.removeRow(selectedIndex);
        undoLimit = (undoLimit < 5) ? undoLimit+1 : 5;
    }

    public void clearTable() {
        undoStack.clear();
        model.setRowCount(0);
    }

    public void undoRow() {
        if(undoLimit == 0) {
            return;
        }
        switch (undoStack.peek().getUndo()) {
            case ADD:
                int selectedIndex = -1;
                for(int y = 0; y < model.getRowCount(); y++) {
                    boolean state = false;
                    for(int x = 0; x < model.getColumnCount(); x++) {
                        if(model.getValueAt(y,x) == undoStack.peek().getObj()[x]) {
                            state = true;
                        }
                    }
                    if(state) {
                        selectedIndex = y;
                        break;
                    }
                }
                model.removeRow(selectedIndex);
                break;
            case REMOVE:
                model.addRow(undoStack.peek().getObj());
                break;
        }
        undoStack.pop();
        undoLimit--;
    }

    public DefaultTableModel getDefaultModel() {
        return model;
    }
}
