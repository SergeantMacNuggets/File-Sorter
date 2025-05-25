package gui;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;


public abstract class Input extends JPanel {
    public abstract String getInput() throws NullPointerException;
    public abstract void setListener(ActionListener e);
    public abstract JComponent getTextField();
    public abstract void setToolTip(String s);
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

    public JComboBox<String> getTextField() {
        return textField;
    }

    @Override
    public void setToolTip(String s) {
        textField.setToolTipText(s);
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

    private String monthToNum(String month) {
        return switch(month) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };
    }

    @Override
    public String getInput() throws NullPointerException {

        if(textField.getDateFormatString().isEmpty() && textField.isEnabled())
            throw new NullPointerException();

        String[] output = textField.getDate().toString().split(" ");

        return (textField.isEnabled()) ? String.format("%s/%s/%s", monthToNum(output[1]), output[2], output[output.length-1]) : "";
    }

    @Override
    public void setListener(ActionListener e) {

    }

    @Override
    public JComponent getTextField() {
        return textField;
    }

    @Override
    public void setToolTip(String s) {
        textField.setToolTipText(s);
    }
}
