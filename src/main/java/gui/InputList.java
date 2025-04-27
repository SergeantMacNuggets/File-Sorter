package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


public class InputList extends JScrollPane {
    private DefaultListModel<String> list;
    private JList<String> mainList;
    private InputList inputList = null;
    private JComponent[] components;
    private Dimension dimension;
    InputList(Dimension dimension) {
        this.dimension = dimension;
        this.setup();
    }
    InputList(Dimension dimension, InputList inputList) {
        this.inputList = inputList;
        this.dimension = dimension;
        this.setup();
    }

    private void setup() {
        list = new DefaultListModel<>();
        mainList = new JList<>(list);
        mainList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        mainList.setCellRenderer(getRenderer());
        this.setViewportView(mainList);
        this.setPreferredSize(dimension);
    }

    public JList<String> getList() {
        return mainList;
    }

    public DefaultListModel<String> getModel() {
        return list;
    }

    public void setInput(JComponent... components){
        this.components = components;
    }
    public JComponent[] getInput() {
        return this.components;
    }

    public void setList(DefaultListModel<String> model, String s) {
        model.addElement(s);
        mainList.setModel(model);
    }

    private void check(InputList i) throws NullPointerException {
        for(JComponent c: i.getInput()) {
            if(c instanceof JTextField t && (t.getText().isEmpty() && t.isEnabled())) {
                throw new NullPointerException();
            }
            else if(c instanceof JComboBox<?> t
                    && (t.getSelectedItem().toString().isEmpty() && t.isEnabled())) {
                throw new NullPointerException();
            }
        }
    }

    public void perform() {
        try {
            check(inputList);
            check(this);
            inputList.addAll();
            this.addAll();
        } catch (NullPointerException e) {
            System.out.println("Input Blank");
        }
    }

    public void addAll() {
        StringBuilder sb = new StringBuilder();
        for(JComponent x: this.getInput()) {
            if(x instanceof JTextField t) {
                sb.append(t.getText()).append(" ");
            }
            else if(x instanceof JComboBox<?> t && t.isEnabled()) {
                sb.append(t.getSelectedItem().toString()).append(" ");
            }
        }
        this.addListElement(sb.toString());
    }

    public void changeList(DefaultListModel<String> model) {
        mainList.setModel(model);
    }

    public void addListElement(String word) {
        list.addElement(word);
    }


    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.lightGray));
                return listCellRendererComponent;
            }
        };
    }

}