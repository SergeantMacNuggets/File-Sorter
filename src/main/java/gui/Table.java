package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Table extends JTable {
    private DefaultTableModel model;
    public Table(Object... columns) {
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (aValue instanceof Boolean && column == 0) {
                    System.out.println(aValue);
                    Vector rowData = (Vector)getDataVector().get(row);
                    rowData.set(0, (boolean)aValue);
                    fireTableCellUpdated(row, column);
                }
            }
        };
        for(Object c: columns) {
            model.addColumn(c);
        }

        this.setModel(model);
    }

    public DefaultTableModel getDefaultModel() {
        return model;
    }
}
