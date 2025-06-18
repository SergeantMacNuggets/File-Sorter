package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Table extends JTable {
    private DefaultTableModel model;
    Table() {
    }

    public void setDefaultModel(DefaultTableModel model) {
        this.model = model;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    };

    public void setColumns(Object... columns) {
        for(Object c: columns) {
            model.addColumn(c);
        }
        this.setModel(model);
    }

    public DefaultTableModel getDefaultModel() {
        return model;
    }
}
