package gui;
import back_end.Account;
import back_end.ConfigService;
import lombok.Builder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

@FunctionalInterface
interface Lambda {
    void perform(DefaultListModel<String> model);
}

@FunctionalInterface
interface UndoLambda {
    void perform(Stack<StackData> undo);
}

public class Settings extends JFrame {
    private static Settings settings;
    private final ConfigService configService;
    private final JButton categoryButton;
    private final JButton fileButton;
    private final JList<String> categoryList;
    private final JList<String> fileList;
    private final JScrollPane categoryPane;
    private final JScrollPane filePane;
    private final JTextField categoryText;
    private final JTextField fileText;
    private final Stack<StackData> undoCategory;
    private Settings() {
        configService = ConfigService.getInstance();
        undoCategory = new Stack<>();
        categoryList = new JList<>();
        fileList = new JList<>();
        categoryText = new JTextField();
        fileText = new JTextField();
        categoryButton = new JButton("...");
        fileButton = new JButton("...");
        categoryList.setModel(new DefaultListModel<>());
        fileList.setModel(new DefaultListModel<>());
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        categoryPane = new JScrollPane(categoryList);
        filePane = new JScrollPane(fileList);
        categoryPane.setPreferredSize(new Dimension(180,100));
        filePane.setPreferredSize(new Dimension(160,0));
        this.loadData();

        if(Account.getInstance().getState()) {
            categoryButton.setEnabled(Account.getInstance().getState());
            fileButton.setEnabled(Account.getInstance().getState());

            categoryButton.addActionListener(addRow(categoryList, categoryText, _ ->{
                FileMap.getInstance().put(categoryText.getText(), new DefaultListModel<>());
                undoCategory.push(new StackData(categoryText.getText(), Undo.ADD));
            }));


            categoryList.addMouseListener(popupMenu(categoryList,undoCategory, e ->{
                DefaultListModel<String> fileModelTemp = FileMap.getInstance().get(categoryList.getSelectedValue());
                undoCategory.push(new StackData(categoryList.getSelectedValue(), fileModelTemp, Undo.REMOVE));
                FileMap.getInstance().remove(categoryList.getSelectedValue());
                categoryList.setSelectedIndex(-1);
                fileList.setModel(new DefaultListModel<>());
                e.removeElement(categoryList.getSelectedValue());
            }, x -> {
                switch(x.peek().getUndo()) {
                    case ADD:
                        DefaultListModel<String> tempModel = (DefaultListModel<String>) categoryList.getModel();
                        FileMap.getInstance().remove((String)x.peek().getObject());
                        tempModel.removeElement(x.peek().getObject());
                        categoryList.setSelectedIndex(-1);
                        fileList.setModel(new DefaultListModel<>());
                        break;
                    case REMOVE:
                        DefaultListModel<String> model = (DefaultListModel<String>) categoryList.getModel();
                        String category = (String) x.peek().getObject();
                        model.addElement(category);
                        FileMap.getInstance().put(category, x.peek().getModel());
                        break;
                }

            }));

            fileButton.addActionListener(addRow(fileList, fileText, e->{
                if(categoryList.getSelectedValue()!=null){
                    FileMap.getInstance().put(categoryList.getSelectedValue(), e);
                } else {
                    JOptionPane.showMessageDialog(null, "Please Choose a Category First");
                    fileList.setModel(new DefaultListModel<>());
                }
            }));

            fileList.addMouseListener(popupMenu(fileList,null, e->{
                if (categoryList.getSelectedValue()!=null){
                    e.removeElement(fileList.getSelectedValue());
                    FileMap.getInstance().put(categoryList.getSelectedValue(), e);
                }
            },null));
        }
        categoryList.addMouseListener(switchModel(categoryList,fileList));

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

    private void insertIntoTable() {
        configService.resetFile();
        for(String key: FileMap.getInstance().keySet()) {
            DefaultListModel<String> temp = FileMap.getInstance().get(key);
            for(int i=0; i < temp.getSize(); i++) {
                configService.addFile(key,temp.get(i));
            }
        }
    }

    private void loadData() {
        for(String key: FileMap.getInstance().keySet()) {
            DefaultListModel<String> model = (DefaultListModel<String>) categoryList.getModel();
            model.addElement(key);
        }
    }


    private JPanel getSouthPanel() {
        JPanel south =  new JPanel();
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(_->{
            undoCategory.clear();
            insertIntoTable();
            MainWindow.getInstance().updateFormat();
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

    private ActionListener addRow(JList<String> list, JTextField text, Lambda lambda) {
        return _ -> {
            DefaultListModel<String> temp = (DefaultListModel<String>) list.getModel();
            if(!temp.contains(text.getText())){
                temp.addElement(text.getText());
                lambda.perform(temp);
            }
            else JOptionPane.showMessageDialog(null,text.getText() + " is already exist!");
            text.setText("");
        };
    }

    private MouseAdapter popupMenu(JList<String> list, Stack<StackData> undoStack, Lambda lambda, UndoLambda undoLambda) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem delete = new JMenuItem(new AbstractAction("Delete") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
                            lambda.perform(model);
                        }
                    });
                    menu.add(delete);

                    if(undoLambda!=null){
                        JMenuItem undo = new JMenuItem(new AbstractAction("Undo") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                undoLambda.perform(undoStack);
                            }
                        });

                        menu.add(undo);
                    }
                    menu.show(e.getComponent(),e.getX(),e.getY());
                }
            }
        };
    }


    private MouseAdapter switchModel(JList<String> parentList, JList<String> childList) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String key = parentList.getSelectedValue();
                childList.setModel(FileMap.getInstance().get(key));
            }
        };
    }



}