package gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

public class StackData {
    @Getter @Setter private Object[] obj;
    @Getter @Setter private Object object;
    @Getter @Setter private DefaultListModel<String> model;
    @Getter @Setter private Undo undo;
    public StackData(Object[] obj, Undo undo) {
        this.obj = obj;
        this.undo = undo;
    }

    public StackData(Object object, Undo undo) {
        this.object = object;
        this.undo = undo;
    }


    public StackData(Object object, DefaultListModel<String> model, Undo undo) {
        this.object = object;
        this.model = model;
        this.undo = undo;
    }
}
