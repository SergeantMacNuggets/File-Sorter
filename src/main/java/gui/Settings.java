package gui;
import back_end.Account;
import back_end.ConfigService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@FunctionalInterface
interface Lambda {
    void perform();
}

public class Settings extends JFrame {
    private static Settings settings;
    private final ConfigService configService;
    private final JButton categoryButton;
    private final JButton fileButton;
    private final Table categoryTable;
    private final Table fileTable;
    private final JScrollPane categoryPane;
    private final JScrollPane filePane;
    private final JTextField categoryText;
    private final JTextField fileText;
    private Settings() {
        configService = ConfigService.getInstance();
        categoryTable = new Table();
        fileTable = new Table();
        categoryText = new JTextField();
        fileText = new JTextField();
        categoryButton = new JButton("...");
        fileButton = new JButton("...");
        categoryButton.addActionListener(addRow(categoryTable, categoryText, ()-> {
            FileMap.getInstance().put(categoryText.getText(), new DefaultTableModel() {{
                addColumn("File Format");
            }});
        }));
        fileButton.addActionListener(addRow(fileTable, fileText, ()->{
            String val = (String) categoryTable.getDefaultModel().getValueAt(categoryTable.getSelectedRow(), 0);
            FileMap.getInstance().get(val).addRow(new Object[]{fileText.getText()});
        }));
        categoryTable.setDefaultModel(new DefaultTableModel());
        fileTable.setDefaultModel(new DefaultTableModel());
        categoryTable.setColumns("Category");
        fileTable.setColumns("File Format");
        categoryPane = new JScrollPane(categoryTable);
        filePane = new JScrollPane(fileTable);
        categoryPane.setPreferredSize(new Dimension(180,100));
        filePane.setPreferredSize(new Dimension(160,0));
        categoryTable.setRowSelectionAllowed(false);
        categoryTable.getSelectionModel().addListSelectionListener(switchModel(categoryTable,fileTable));
        categoryTable.addMouseListener(removeRow(categoryTable,()->{
            String key = (String) categoryTable.getValueAt(categoryTable.getSelectedRow(),0);
            FileMap.getInstance().remove(key);
        }));

        fileTable.addMouseListener(removeRow(fileTable,()->{
            String key = (String) fileTable.getValueAt(fileTable.getSelectedRow(),0);
            FileMap.getInstance().get(key).removeRow(fileTable.getSelectedRow());
        }));
        if(Account.getInstance().getState()) {
            categoryButton.setEnabled(Account.getInstance().getState());
            fileButton.setEnabled(Account.getInstance().getState());

//            categoryList.getList().addMouseListener(new RemoveListener(categoryList));
//            fileList.getList().addMouseListener(new RemoveListener(fileList));
        }
//        categoryList.getList().addMouseListener(new MouseAdapter() {
//            @SuppressWarnings("unchecked")
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (SwingUtilities.isLeftMouseButton(e)) {
//                    JList<String> mouseList = (JList<String>) e.getSource();
//                    String key = mouseList.getSelectedValue();
//                    categoryList.getChildList().setModel(FileMap.getInstance().get(key));
//                }
//            }
//        });

//        for(String key: FileMap.getInstance().keySet()) {
//            categoryList.addListElement(key);
//        }


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
        main.add(filePane,BorderLayout.EAST);
        main.add(getSouthPanel(),BorderLayout.SOUTH);
        return main;
    }

//    private void insertIntoTable() {
//        configService.resetFile();
//        for(String key: FileMap.getInstance().keySet()) {
//            DefaultListModel<String> temp = FileMap.getInstance().get(key);
//            for(int i=0; i < temp.getSize(); i++) {
//                configService.addFile(key,temp.get(i));
//            }
//        }
//    }


    private JPanel getSouthPanel() {
        JPanel south =  new JPanel();
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(_->{
            System.out.println(FileMap.getInstance());
//            insertIntoTable();
            MainWindow.getInstance().updateFormat();
//            categoryList.clearStack();
//            fileList.clearStack();
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

        Component[] components = {categoryPane,new JLabel("Add a category"), textField(categoryText,categoryButton),
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

    private ActionListener addRow(Table table, JTextField text, Lambda lambda) {
        return _ -> {
            table.addRow(new Object[] {text.getText()});
            lambda.perform();
            table.getSelectionModel().clearSelection();
        };
    }

    private MouseAdapter removeRow(Table table, Lambda lambda) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.getSelectedRow();
                if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() % 2 == 0) {
                    lambda.perform();
                    table.removeRow(row);
                }
            }
        };
    }

    private ListSelectionListener switchModel(Table parentTable, Table childTable) {
        return e -> {
            String key = (String) parentTable.getValueAt(parentTable.getSelectedRow(),0);
            DefaultTableModel temp = FileMap.getInstance().get(key);
            childTable.setModel(temp);
        };
    }



}