package gui;

import back_end.Account;
import back_end.ConfigService;
import back_end.MainService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Stream;


@FunctionalInterface
interface PanelMaker {
    void set(JPanel p);
}

public class MainWindow extends JFrame {

    private static MainWindow mainWindow;
    private final ComboBoxInput file, category;
    private final DateInput date;
    private final Input sourceFolder, destFolder;
    private final Table leftTable;
    private final Table rightTable;
    int x = 950, y = 600;
    private MainWindow() {
        AccountWindow.getInstance(this);
        ConfigService.getInstance().refresh();
        leftTable = getLeftTable();
        rightTable = new Table() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        category = new ComboBoxInput(new JLabel("Category"),x-650,20);

        file = new ComboBoxInput(new JLabel("File Format"), x-650,20)
            {{this.setToolTip("Input your chosen file format here");}};

        date = new DateInput("Date", x-700,25)
            {{this.setToolTip("Input your chosen file with date when it was modified");}};

        sourceFolder = new ComboBoxInput(new JLabel("Source Folder"),x-450,25)
            {{this.setToolTip("Input the URL Directory from where would you send your files");}};

        destFolder = new ComboBoxInput(new JLabel("Destination Folder"),x-450,25)
            {{this.setToolTip("Input the URL Directory to where would you send your files");}};
        new WindowBuilder(this)
                .setDimension(x,y)
                .setTitle("Automatic File Sorter")
                .setLayout(null)
                .setWindowConstants(JFrame.EXIT_ON_CLOSE)
                .setComponents(mainPanel())
                .build();
        this.setJMenuBar(new MenuBar());
        this.updateFormat();
        this.setVisible(true);
    }

    public static MainWindow getInstance() {
        if(mainWindow==null) {
            mainWindow = new MainWindow();
        }
        return mainWindow;
    }
    private JPanel setPanel(PanelMaker panelMaker) {
        JPanel p = new JPanel();
        panelMaker.set(p);
        return p;
    }

    public void load() {
        ArrayList<Object[]> obj = MainService.getInstance().load();
        for (Object[] objects : obj) {
            Object[] leftObj = {objects[0], objects[1], objects[2], objects[3]};
            Object[] rightObj = {objects[4], objects[5]};
            leftTable.getDefaultModel().addRow(leftObj);
            rightTable.getDefaultModel().addRow(rightObj);
        }
    }

    public void updateFormat() {
        JComboBox<String> categoryBox = category.getTextField();
        JComboBox<String> fileBox = file.getTextField();
        categoryBox.removeAllItems();

        for(String key: FileMap.getInstance().keySet()) {
            DefaultListModel<String> tempModel = FileMap.getInstance().get(key);
            categoryBox.addItem(key);
            for(int i=0; i<tempModel.size(); i++) {
                fileBox.addItem(tempModel.get(i));
            }
        }
        categoryBox.setSelectedIndex(-1);
        fileBox.setSelectedIndex(-1);
    }

    private ActionListener changeCategory() {
        return _ -> {
            JComboBox<String> categoryBox = category.getTextField();
            JComboBox<String> fileBox = file.getTextField();
            fileBox.removeAllItems();
            if (categoryBox.getSelectedIndex() != -1){
                DefaultListModel<String> tempModel = FileMap.getInstance().get((String)categoryBox.getSelectedItem());
                fileBox.removeAllItems();
                for (int i = 0; i < tempModel.getSize(); i++) {
                    fileBox.addItem(tempModel.get(i));
                }
            }
        };
    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        rightTable.setDefaultModel(new DefaultTableModel());
        rightTable.setColumns("Source Folder", "Destination Folder");
        JScrollPane rightPane = new JScrollPane(rightTable);
        JScrollPane leftPane = new JScrollPane(leftTable);
        leftTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer(){{this.setHorizontalAlignment(JLabel.CENTER);}});
        leftPane.setPreferredSize(new Dimension(410,380));
        rightPane.setPreferredSize(new Dimension(410,380));
        sourceFolder.addItem("Other");
        destFolder.addItem("Other");
        category.setListener(changeCategory());
        sourceFolder.setListener(openDirectory(sourceFolder));
        destFolder.setListener(openDirectory(destFolder));
        this.load();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.clearInstance();
                System.exit(0);
            }
        });

        p.setLayout(new BorderLayout());

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(x,140));
            e.setLayout(new MigLayout());
            e.add(category);
            e.add(sourceFolder);
            e.add(date, "wrap");
            e.add(file);
            e.add(destFolder);
            e.add(new JButton("Setting")
            {{
                this.setPreferredSize(new Dimension(x-700,20));
                this.addActionListener(_ -> Settings.getInstance().start());
            }}, "gaptop 20");

        }), BorderLayout.NORTH);

        p.add(setPanel(e -> e.add(leftPane)),BorderLayout.WEST);

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(100,280));
            e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));

            Stream.of(      new JButton("Add")     {{this.setToolTipText("Add Your Configuration");
                                                            this.addActionListener(_ -> addRow(leftTable, rightTable));}},
                            new JButton("Remove")  {{this.setToolTipText("Remove Your Configuration");
                                                            this.addActionListener(_ -> removeRow(leftTable, rightTable));}},
                            new JButton("Clear")   {{this.setToolTipText("Delete All Your Configurations");
                                                            this.addActionListener(_ -> clearRow(leftTable,rightTable));}},
                            new JButton("Undo")    {{this.setToolTipText("Add or Remove Your Previous Configuration");
                                                            this.addActionListener(_ -> undoRow(leftTable,rightTable));}},
                            new JButton("Run")     {{this.setToolTipText("Start Sorting");}})
                    .forEach(b ->
                            {
                                b.setPreferredSize(new Dimension(80, 25));
                                b.setForeground(SpecificColor.buttonText);
                                b.setBackground(SpecificColor.buttonColor);
                                b.setEnabled(Account.getInstance().getState());
                                e.add(setPanel(k -> k.add(b)));
                            }
                    );

        }),BorderLayout.CENTER);

        p.add(setPanel(e -> e.add(rightPane)),BorderLayout.EAST);
        return p;
    }

    private static Table getLeftTable() {
        Table leftTable = new Table();
        leftTable.setDefaultModel(new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (aValue instanceof Boolean && column == 0) {
                    System.out.println(aValue);
                    Vector rowData = getDataVector().get(row);
                    rowData.set(0, aValue);
                    fireTableCellUpdated(row, column);
                }
            }
        });
        leftTable.setColumns("Include", "Category", "File Format", "Date");
        return leftTable;
    }

    private void save() {
        MainService service = MainService.getInstance();
        service.truncate();
        for(int y = 0; y < leftTable.getDefaultModel().getRowCount(); y++) {

            service.add((boolean) leftTable.getValueAt(y,0),    //Include
                        (String)  leftTable.getValueAt(y,1),    //Category
                        (String)  leftTable.getValueAt(y,2),    //File
                        (String)  leftTable.getValueAt(y,3),    //Date

                        (String)  rightTable.getValueAt(y,0),   //Source Folder
                        (String)  rightTable.getValueAt(y,1)    //Destination Folder

            );
        }
    }

    public static void clearInstance() {
        mainWindow.save();
        mainWindow.setVisible(false);
        mainWindow.dispose();
        mainWindow = null;
    }

    @SuppressWarnings("unchecked")
    private ActionListener openDirectory(Input i) {
        return e -> {
            JComboBox<String> folder = (JComboBox<String>) i.getTextField();
            if (Objects.equals(folder.getSelectedItem(), "Other")){
                folder.setSelectedIndex(-1);
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (chooser.showOpenDialog(null) == 0) {
                    folder.addItem(chooser.getSelectedFile().getAbsolutePath());
                    folder.setSelectedItem(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        };
    }


    public void addRow(Table leftTable, Table rightTable) {
        try {
            MainService service = MainService.getInstance();
            Object[] leftObject = {true, category.getInput(), file.getInput(), date.getInput()};
            Object[] rightObject = {sourceFolder.getInput(), destFolder.getInput()};
            leftTable.addRow(leftObject);
            rightTable.addRow(rightObject);
            service.add(true,category.getInput(),file.getInput(), date.getInput(), sourceFolder.getInput(), destFolder.getInput());
        } catch (NullPointerException x) {
            JOptionPane.showMessageDialog(null, "Some input is blank!");
        }
    }

    public void removeRow(Table leftTable, Table rightTable) {
        int selectedRow = leftTable.getSelectedRow();
        MainService.getInstance().remove((String) leftTable.getValueAt(selectedRow, 1),
                (String) leftTable.getValueAt(selectedRow, 2),
                (String) leftTable.getValueAt(selectedRow, 3),
                (String) rightTable.getValueAt(selectedRow, 0),
                (String) rightTable.getValueAt(selectedRow, 1));
        leftTable.removeRow(selectedRow);
        rightTable.removeRow(selectedRow);
    }

    private void clearRow(Table leftTable, Table rightTable) {
        leftTable.clearTable();
        rightTable.clearTable();
        MainService.getInstance().truncate();
    }

    private void undoRow(Table leftTable, Table righTable) {
        try{
            leftTable.undoRow();
            righTable.undoRow();

        } catch (Exception e) {
            return;
        }
    }
}

class MenuBar extends JMenuBar {
    MenuBar() {
        JMenu[] menu = {getFileMenu(), new JMenu("Edit"),
                new JMenu("View"), getAccountMenu(), new JMenu("Help")
        ,new JMenu("About")};

        for(JMenu m: menu) add(m);
    }

    private JMenu getFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem[] subItem = {new JMenuItem("Printer"), new JMenuItem("Quit")};
        subItem[0].addActionListener(_->new Printer());
        subItem[1].addActionListener(_ -> System.exit(0));
        for(JMenuItem i: subItem) file.add(i);

        return file;
    }

    private JMenu getAccountMenu() {
        JMenu account = new JMenu("Account");
        JMenuItem signOut = new JMenuItem("Sign Out");
        signOut.addActionListener(_->{
            MainWindow.clearInstance();
            Account.clearInstance();
            AccountWindow.clearInstance();
            Settings.clearInstance();
            MainWindow.getInstance();
        });
        account.add(signOut);
        return account;
    }



}
