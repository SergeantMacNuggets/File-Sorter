package gui;

import back_end.Account;
import listeners.Data;
import listeners.UndoListener;

import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.util.Stack;


public class InputList extends JScrollPane {
    private DefaultListModel<String> list;
    private JList<String> mainList;
    private Input[] components;
    private JTextField input;
    private InputList childList=null;
    private Dimension dimension;
    private int undoCount;
    private boolean disabler;
    private Stack<Data> stack;
    private StringBuilder sb;
    private boolean childInput;

    InputList(Dimension dimension) {
        this.dimension = dimension;
        this.setup();
    }

    private void setup() {
        list = new DefaultListModel<>();
        mainList = new JList<>(list);
        stack = new Stack<>();
        childInput = false;
        disabler = false;
        mainList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        mainList.setSelectionBackground(SpecificColor.buttonColor);
        mainList.setSelectionForeground(SpecificColor.buttonText);

        mainList.setCellRenderer(getRenderer());
        this.setViewportView(mainList);
        this.setPreferredSize(dimension);
        mainList.setFocusable(false);
        mainList.addKeyListener(new UndoListener(this));
    }

    public void setChildList(InputList childList) {
        this.childList = childList;
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
        this.undoCount = Math.min(undoCount, 5);
    }
    public int getUndoCount() {return undoCount;}

    public void push(Data data) {
        this.stack.push(data);
    }

    public void clearStack() {this.stack.clear();}

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

    public void hasDisabler(boolean disabler){this.disabler=disabler;}


    public void addAll(boolean state) throws NullPointerException {
        sb = new StringBuilder();
        if (components != null) {
            for (Input x : this.components) {
                if(x.isEnabled())
                    sb.append(x.getInput()).append(" ");
            }
        } else {
            sb.append(input.getText());
        }

        if(state) {
            if(!list.contains((this.disabler) ? "[+] "  + sb.toString() : sb.toString())) {
                if(!list.contains((this.disabler) ? "[-] "  + sb.toString() : sb.toString())) {
                    this.addListElement(sb.toString());
                }
                else throw new NullPointerException();
            }
            else throw new NullPointerException();
        }
        else {
            this.addListElement(sb.toString());
        }
    }


    public String getString() {
        return (this.disabler) ? "[+] "  + sb.toString() : sb.toString();
    }

    public void addListElement(String word) {
        list.addElement((this.disabler) ? "[+] " + word : word);
    }

    public ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer() {
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