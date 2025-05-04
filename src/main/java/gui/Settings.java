package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import listeners.RemoveListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Settings extends JFrame {
    private static Settings settings;
    JButton categoryButton, fileButton, okButton, cancelButton;
    JTextField categoryText, fileText;
    InputList categoryList, fileList;
    Settings() {

        FlatLaf.setup(new FlatDarkLaf());
        categoryList = new InputList(new Dimension(180,100));
        fileList = new InputList(new Dimension(160,0));
        categoryText = new JTextField();
        fileText = new JTextField();
        categoryButton = new AddButton(categoryList);
        fileButton = new AddButton(fileList);
        fileList.setInput(fileText);
        categoryList.setInput(categoryText);
        categoryList.setChildList(fileList);
        categoryList.ignoreChildInput(true);
        categoryList.getList().setFocusable(true);
        fileList.getList().setFocusable(true);
        fileList.getList().setEnabled(true);
        for(String key: FileMap.getInstance().keySet()) {
            categoryList.addListElement(key);
        }

        categoryList.getList().addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    JList<String> mouseList = (JList<String>) e.getSource();
                    String key = mouseList.getSelectedValue();
                    categoryList.getChildList().setModel(FileMap.getInstance().get(key));
                }
            }
        });
        categoryButton.addActionListener(e->{
            if(!FileMap.getInstance().containsKey(categoryText.getText()))
                FileMap.getInstance().put(categoryText.getText(), new DefaultListModel<>());
        });

        categoryList.getList().addMouseListener(new RemoveListener(categoryList));
        fileList.getList().addMouseListener(new RemoveListener(fileList));

        new WindowBuilder(this)
                .setDimension(400,300)
                .setTitle("Settings")
                .setWindowConstants(JFrame.DO_NOTHING_ON_CLOSE)
                .setComponents(getMainPanel())
                .build();
    }

    public void start() {
        this.setVisible(true);
    }


    public static void clearInstance() {
        settings = null;
    }

    public static Settings getInstance() {
        if(settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    private JPanel getMainPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        main.add(getLeftPanel(),BorderLayout.WEST);
        main.add(fileList,BorderLayout.EAST);
        main.add(getSouthPanel(),BorderLayout.SOUTH);
        return main;
    }


    private JPanel getSouthPanel() {
        JPanel south =  new JPanel();
        okButton = new JButton("Ok");
        okButton.addActionListener(_->{
            MainWindow.getInstance().updateFormat();
            this.dispose();
            this.setVisible(false);
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e->{
            this.dispose();
            this.setVisible(false);
        });
        south.add(okButton);
        south.add(cancelButton);
        return south;
    }

    private JPanel getLeftPanel() {
        JPanel left = new JPanel();
        Dimension d =left.getPreferredSize();
        d.width = 210;

        Component[] components = {categoryList,new JLabel("Add a category"), textField(categoryText,categoryButton),
                new JLabel("Add a File Format"), textField(fileText,fileButton)};

        left.setPreferredSize(d);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        for(Component c: components)
            left.add(c);

        return left;
    }

    private JPanel textField(JTextField t, JButton b) {
        JPanel p = new JPanel();
        b.setPreferredSize(new Dimension(30,20));
        t.setPreferredSize(new Dimension(170,20));
        p.add(t);
        p.add(b);
        return p;
    }



}