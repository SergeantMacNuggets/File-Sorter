package gui;


import listeners.InputListListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileWindow extends JFrame {

    private interface Buttons{
        String DISABLER = "Exclude";
        String OK = "Ok";
        String CANCEL = "Cancel";
    }

    InputList fileList;
    Border padding;
    FileWindow(String category) {
        fileList = new InputList(new Dimension(300,0));
        padding = BorderFactory.createEmptyBorder(5,5,5,5);
        JButton[] buttons = getAllButtons();
        JPanel main = new JPanel();
        fileList.hasDisabler(true);
        fileList.getList().addListSelectionListener(e -> {

            JList<String> temp =(JList<String>) e.getSource();
            if(temp.getSelectedValue().charAt(1)=='+') buttons[0].setText("Exclude");
            else if(temp.getSelectedValue().charAt(1)=='-') buttons[0].setText("Include");

        });
        fileList.addListElement("index.cpp");
        fileList.addListElement("main.c");
        fileList.addListElement("bubbleSort.java");
        fileList.addListElement("encrypt.py");
        main.setBorder(padding);
        main.setLayout(new BorderLayout());
        main.add(fileList,BorderLayout.WEST);
        main.add(getRightPanel(buttons));
        WindowBuilder windowBuilder = new WindowBuilder(this);
        windowBuilder
                .setDimension(430,230)
                .setTitle(category.substring(4))
                .setWindowConstants(JFrame.DO_NOTHING_ON_CLOSE)
                .setComponents(main)
                .build();
    }

    public void start() {
        this.setVisible(true);
    }

    private JPanel getRightPanel(Component[] component) {
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(4,1,0,20));
        right.setBorder(padding);
        right.add(new JLabel(String.format("Choose what file to\ninclude or exclude")));
        for(Component c: component) {
            right.add(c);
        }
        return right;
    }

    private JButton[] getAllButtons() {
        JButton[] returnButton = {new JButton(Buttons.DISABLER),
                new JButton(Buttons.OK) {{
                    setForeground(SpecificColor.buttonText);
                    setBackground(SpecificColor.buttonColor);
                }},
                new JButton(Buttons.CANCEL)};
        for(JButton b: returnButton) {
            b.addActionListener(addButtonListeners(b.getText()));
        }

        return returnButton;
    }

    private ActionListener addButtonListeners(String buttonText) {
        JList<String> temp = fileList.getList();
        return switch (buttonText) {
            case Buttons.DISABLER -> (ActionListener) e -> {

                JButton temp1 = (JButton) e.getSource();
                temp1.setText((temp1.getText().equals("Exclude"))? "Include": "Exclude");
                InputListListener.disabler(fileList).perform();

            };

            case Buttons.OK -> (ActionListener) e -> System.out.println("Ok");

            case Buttons.CANCEL -> (ActionListener) e -> {
                dispose();
                setVisible(false);
            };

            default -> throw new IllegalStateException("Unexpected value: " + buttonText);
        };
    }
}