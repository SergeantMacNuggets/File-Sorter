package gui;

import listeners.Data;
import listeners.Undo;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;


public class InputList extends JScrollPane {
    private DefaultListModel<String> list;
    private JList<String> mainList;
    private Input[] components;
    private InputList childList=null;
    private Dimension dimension;
    private int undoCount;
    private final int undoLimit;
    private Stack<Data> stack;
    private StringBuilder sb;
    InputList(Dimension dimension) {
        this.dimension = dimension;
        this.undoLimit = 5;
        this.setup();
    }

    private void setup() {
        list = new DefaultListModel<>();
        mainList = new JList<>(list);
        stack = new Stack<>();
        mainList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.getList()
                .setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.lightGray));
        mainList.setCellRenderer(getRenderer());
        this.setViewportView(mainList);
        this.setPreferredSize(dimension);
    }

    public void setChildList(InputList childList) {
        this.childList = childList;
    }
    public InputList getChildList() {
        return childList;
    }

    public JList<String> getList() {
        return mainList;
    }


    public void setUndoCount(int undoCount) {
        this.undoCount = undoCount;
    }
    public int getUndoLimit() {return undoLimit;}

    public int getUndoCount() {return undoCount;}

    public void push(Data data) {
        this.stack.push(data);
    }

    public void pop() {
        this.stack.pop();

    }

    public Stack<Data> getStack() {return stack;}

    public DefaultListModel<String> getModel() {
        return list;
    }

    public void setInput(Input... components){
        this.components = components;
    }
    public Input[] getInput() {
        return this.components;
    }

    public void setList(DefaultListModel<String> model, String s) {
        model.addElement(s);
        mainList.setModel(model);
    }

    public void check() throws NullPointerException {
        try {
            for (Input c : this.getInput()) {
                c.getInput();
            }
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

    public void addAll() {
        sb = new StringBuilder();
        for(Input x: this.getInput()) {
            sb.append(x.getInput()).append(" ");
        }
        this.addListElement(sb.toString());
    }

    public String getString() {
        return sb.toString();
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