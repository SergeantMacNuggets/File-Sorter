package gui;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public abstract class Input extends JPanel {
    public abstract String getInput() throws NullPointerException;
    public abstract void setListener(ActionListener e);
    public abstract JComponent getTextField();
}

class ComboBoxInput extends Input {
    JComboBox<String> textField;
    ComboBoxInput(JComponent component, int x, int y) {
        textField = new JComboBox<>();
        textField.setEditable(true);
        textField.setPreferredSize(new Dimension(x,y));
        textField.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX");
        textField.addItem("Other");
        textField.setSelectedIndex(-1);
        if(component instanceof JRadioButton r) {
            r.setSelected(true);
            r.addActionListener(_->{
                textField.setEnabled(r.isSelected());
                textField.setEditable(r.isSelected());
            });
        }
        this.setLayout(new MigLayout("insets 0"));
        this.add(component, "wrap");
        this.add(textField);
    }

    public void setListener(ActionListener e) {
        textField.addActionListener(e);
    }

    public JComponent getTextField() {
        return textField;
    }
    @Override
    public String getInput() throws NullPointerException{
        if(textField.getSelectedItem().toString().isEmpty() && textField.isEditable()) {
            throw new NullPointerException();
        }
        return textField.getSelectedItem().toString();
    }

}

class DateInput extends Input {
    JRadioButton radioButton;
    JDateChooser textField;
    DateInput(String title, int x, int y) {
        radioButton = new JRadioButton(title);
        textField = new JDateChooser("MM/dd/yy","##/##/##", '_');
        textField.setDateFormatString("MM/dd/yy");
        textField.setEnabled(false);
        textField.setPreferredSize(new Dimension(x,y));
        radioButton.addActionListener(_->
                textField.setEnabled(radioButton.isSelected())
        );
        this.setLayout(new MigLayout("insets 0"));
        this.add(radioButton, "wrap");
        this.add(textField);
    }

    @Override
    public String getInput() throws NullPointerException {
        if(textField.getDateFormatString().isEmpty() && textField.isEnabled())
            throw new NullPointerException();
        return (textField.isEnabled()) ? textField.getDate().toString() : "";
    }

    @Override
    public void setListener(ActionListener e) {

    }

    @Override
    public JComponent getTextField() {
        return textField;
    }
}
