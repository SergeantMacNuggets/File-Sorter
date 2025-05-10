package gui;

import listeners.Data;
import listeners.Undo;
import listeners.UndoListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Stack;


public class InputList extends JScrollPane {
    private ArrayList<String> arrayList;
    private DefaultListModel<String> list;
    private JList<String> mainList;
    private Input[] components;
    private JTextField input;
    private InputList childList=null;
    private Dimension dimension;
    private int undoCount;
    private final int undoLimit;
    private boolean disabler;
    private Stack<Data> stack;
    private StringBuilder sb;
    private boolean childInput;


    InputList(Dimension dimension) {
        this.dimension = dimension;
        this.undoLimit = 5;
        this.setup();
    }

    private void setup() {
        list = new DefaultListModel<>();
        mainList = new JList<>(list);
        stack = new Stack<>();
        childInput = false;
        disabler = false;
        arrayList = new ArrayList<>();
        mainList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        mainList.setSelectionBackground(SpecificColor.buttonColor);
        mainList.setSelectionForeground(SpecificColor.buttonText);
        mainList.addListSelectionListener(_ -> {
            if(childList!=null && !childInput) {
                childList.getList().setSelectedIndex(mainList.getSelectedIndex());
            }
        });

        mainList.setCellRenderer(getRenderer());
        this.setViewportView(mainList);
        this.setPreferredSize(dimension);
        mainList.setFocusable(false);
        mainList.addKeyListener(new UndoListener(this));
    }

    public void setChildList(InputList childList) {
        this.childList = childList;
        this.childList.getList().setEnabled(false);
    }
    public InputList getChildList() {
        return childList;
    }

    public void ignoreChildInput(boolean childInput) {
        this.childInput = childInput;
    }
    public boolean childInputState() {return this.childInput;}

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
    public Data peek() {
        return this.stack.peek();
    }

    public Stack<Data> getStack() {return stack;}

    public DefaultListModel<String> getModel() {
        return list;
    }

    public void setInput(Input... components){
        this.components = components;
    }
    public void setInput(JTextField input){
        this.input = input;
    }

    public Input[] getInput() {
        return this.components;
    }

    public void setModel(DefaultListModel<String> model) {
        mainList.setModel(model);
        this.list = model;
    }

    public void check() throws NullPointerException {
        try {
            if (components != null){
                for (Input c : this.getInput()) {
                    c.getInput();
                }
            } else {
                if(input.getText().isBlank())
                    throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }
    public void hasDisabler(boolean disabler){this.disabler=disabler;}


    public void addAll(boolean state) throws NullPointerException {
        sb = new StringBuilder();
        if (components != null){
            for (Input x : this.getInput()) {
                sb.append(x.getInput()).append(" ");
            }
        } else {
            sb.append(input.getText());
        }

        if(state) {
            if(!arrayList.contains(sb.toString()))
                this.addListElement(sb.toString());
            else throw new NullPointerException();
        }
        else this.addListElement(sb.toString());

    }


    public String getString() {
        return sb.toString();
    }

    public void addListElement(String word) {
        arrayList.add(word);
        list.addElement((this.disabler) ? "[+] " + word : word);
    }

    public ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.darkGray));
                return listCellRendererComponent;
            }
        };
    }

}