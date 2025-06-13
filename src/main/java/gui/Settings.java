package gui;
import back_end.Account;
import back_end.ConfigService;
import listeners.RemoveListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Settings extends JFrame {
    private static Settings settings;
    private final ConfigService configService;
    private final JButton categoryButton;
    private final JButton fileButton;
    private final JTextField categoryText;
    private final JTextField fileText;
    private final InputList categoryList;
    private final InputList fileList;
    private Settings() {
        configService = ConfigService.getInstance();
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

        if(Account.getInstance().getState()) {
            categoryButton.setEnabled(Account.getInstance().getState());
            fileButton.setEnabled(Account.getInstance().getState());
            categoryButton.addActionListener(_ -> {
                if (!FileMap.getInstance().containsKey(categoryText.getText())) {
                    FileMap.getInstance().put(categoryText.getText(), new DefaultListModel<>());
                }
            });

            categoryList.getList().addMouseListener(new RemoveListener(categoryList));
            fileList.getList().addMouseListener(new RemoveListener(fileList));
        }
        categoryList.getList().addMouseListener(new MouseAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    JList<String> mouseList = (JList<String>) e.getSource();
                    String key = mouseList.getSelectedValue();
                    categoryList.getChildList().setModel(FileMap.getInstance().get(key));
                }
            }
        });

        for(String key: FileMap.getInstance().keySet()) {
            categoryList.addListElement(key);
        }


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

    private void insertIntoTable() {
        configService.resetFile();
        for(String key: FileMap.getInstance().keySet()) {
            DefaultListModel<String> temp = FileMap.getInstance().get(key);
            for(int i=0; i < temp.getSize(); i++) {
                configService.addFile(key,temp.get(i));
            }
        }
    }


    private JPanel getSouthPanel() {
        JPanel south =  new JPanel();
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(_->{
            System.out.println(FileMap.getInstance());
            insertIntoTable();
            MainWindow.getInstance().updateFormat();
            categoryList.clearStack();
            fileList.clearStack();
            this.dispose();
            this.setVisible(false);
        });
        JButton cancelButton = new JButton("Cancel");
        okButton.setForeground(SpecificColor.buttonText);
        okButton.setBackground(SpecificColor.buttonColor);
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