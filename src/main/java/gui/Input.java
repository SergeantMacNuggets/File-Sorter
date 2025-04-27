package gui;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;


class ComboBoxInput extends JPanel{
    JComboBox<String> textField;
    JRadioButton radioButton;
    ComboBoxInput(String title, int x, int y) {
        radioButton = new JRadioButton(title);
        textField = new JComboBox<>();
        textField.setEditable(true);
        textField.setPreferredSize(new Dimension(x,y));
        this.setLayout(new MigLayout());
        this.add(radioButton, "wrap");
        this.add(textField);
    }
    public String getInput() {
        return (String) textField.getSelectedItem();
    }

}

class DirectoryInput extends JPanel {
    JComboBox<String> textField;
    DirectoryInput(String title,int x, int y) {
        textField = new JComboBox<>();
        textField.setPreferredSize(new Dimension(x,y));
        textField.setMaximumSize(new Dimension(x,y));
        textField.addItem("Other Location");
        textField.setSelectedIndex(-1);
        textField.addActionListener(openDirectory());
        this.setLayout(new MigLayout());
        this.add(new JLabel(title), "wrap");
        this.add(textField);
    }

    private ActionListener openDirectory() {
        return e -> {
            if (Objects.equals(textField.getSelectedItem(), "Other Location")){
                textField.setSelectedIndex(-1);
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == 0) {
                    textField.addItem(chooser.getSelectedFile().getAbsolutePath());
                    textField.setSelectedItem(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        };
    }

    public String getInput() {
        return (String) textField.getSelectedItem();
    }
}

class DateInput extends JPanel {
    JRadioButton radioButton;
    DateInput(String title, int x, int y) {
        radioButton = new JRadioButton(title);
        JDateChooser textField = new JDateChooser();
        textField.setPreferredSize(new Dimension(x,y));
        this.setLayout(new MigLayout());
        this.add(radioButton, "wrap");
        this.add(textField);
    }
}
