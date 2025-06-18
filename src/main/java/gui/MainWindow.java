package gui;

import back_end.Account;
import back_end.ConfigService;
import listeners.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Stream;

interface PanelMaker {
    void set(JPanel p);
}

public class MainWindow extends JFrame {

    private static MainWindow mainWindow;
    private final ComboBoxInput file, category;
    private final DateInput date;
    private final Input sourceFolder, destFolder;
    int x = 950, y = 600;
    private MainWindow() {
        AccountWindow.getInstance(this);
        ConfigService.getInstance().refresh();

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

    public void updateFormat() {
        JComboBox<String> tempBox = category.getTextField();
        tempBox.removeAllItems();

        for(String key: FileMap.getInstance().keySet()) {
            tempBox.addItem(key);
        }
        tempBox.setSelectedIndex(-1);
    }

    private ActionListener changeCategory() {
        return e -> {
            JComboBox<String> categoryBox = category.getTextField();
            JComboBox<String> fileBox = file.getTextField();
            if (categoryBox.getSelectedIndex() != -1){
                DefaultListModel<String> tempModel = FileMap.getInstance().get(categoryBox.getSelectedItem());
                fileBox.removeAllItems();
                for (int i = 0; i < tempModel.getSize(); i++) {
                    fileBox.addItem(tempModel.get(i));
                }
                fileBox.addItem("All");
            }
        };
    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        Table leftTable = getLeftTable();
        Table rightTable = new Table();
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

        p.add(setPanel(e -> {
            e.add(leftPane);
        }),BorderLayout.WEST);

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(100,280));
            e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));

            Stream.of(      new JButton("Add")     {{this.setToolTipText("Add Your Configuration");
                                                            this.addActionListener(addRow(leftTable, rightTable));}},
                            new JButton("Remove")  {{this.setToolTipText("Remove Your Configuration");
                                                            this.addActionListener(removeRow(leftTable, rightTable));}},
                            new ClearButton()   {{this.setToolTipText("Delete All Your Configurations");}},
                            new UndoButton()    {{this.setToolTipText("Add or Remove Your Previous Configuration");}},
                            new JButton("Run")     {{this.setToolTipText("Start Sorting");}})
                    .forEach(b ->
                            {
                                b.setPreferredSize(new Dimension(80, 25));
                                b.setForeground(SpecificColor.buttonText);
                                b.setBackground(SpecificColor.buttonColor);
                                e.add(setPanel(k -> k.add(b)));
                            }
                    );

        }),BorderLayout.CENTER);

        p.add(setPanel(e -> {

            e.add(rightPane);

        }),BorderLayout.EAST);
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
                    Vector rowData = (Vector)getDataVector().get(row);
                    rowData.set(0, (boolean)aValue);
                    fireTableCellUpdated(row, column);
                }
            }
        });
        leftTable.setColumns("Include", "Category", "File Format", "Date");
        return leftTable;
    }

    public static void clearInstance() {
        mainWindow.setVisible(false);
        mainWindow.dispose();
        mainWindow = null;
    }

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

    private ActionListener addRow(Table leftTable, Table rightTable) {
        return e -> {
            try {
                Object[] leftObject = {true, category.getInput(), file.getInput(), date.getInput()};
                Object[] rightObject = {sourceFolder.getInput(), destFolder.getInput()};

                leftTable.getDefaultModel().addRow(leftObject);
                rightTable.getDefaultModel().addRow(rightObject);
            } catch (NullPointerException x) {
                JOptionPane.showMessageDialog(null, "Some input is blank!");
            }
        };
    }

    private ActionListener removeRow(Table leftTable, Table rightTable) {
        return _->{
            int selectedRow = leftTable.getSelectedRow();
            leftTable.getDefaultModel().removeRow(selectedRow);
            rightTable.getDefaultModel().removeRow(selectedRow);
        };
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
            Account.clearInstance();
            AccountWindow.clearInstance();
            MainWindow.clearInstance();
            Settings.clearInstance();
            MainWindow.getInstance();
        });
        account.add(signOut);
        return account;
    }



}
