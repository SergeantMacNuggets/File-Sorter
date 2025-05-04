package listeners;


import gui.FileMap;

import javax.swing.*;

public class Data {
    Undo undo;
    String input;
    DefaultListModel<String> model;
    public Data(Undo undo, String input) {
        this.undo = undo;
        this.input = input;
    }

    public String input() {return input;}
    public Undo undo() {return undo;}

    public void setModel(DefaultListModel<String> model) {
        this.model = model;
    }
    public DefaultListModel<String> model() {
        return model;
    }
}

