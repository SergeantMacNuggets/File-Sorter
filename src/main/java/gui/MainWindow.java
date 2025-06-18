package gui;

import back_end.Account;
import back_end.ConfigService;
import listeners.*;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.stream.Stream;

interface PanelMaker {
    void set(JPanel p);
}

public class MainWindow extends JFrame {

    private static MainWindow mainWindow;
    private final ComboBoxInput file;
    private final DateInput date;
    private final Input sourceFolder, destFolder;
    int x = 950, y = 600;
    private MainWindow() {
        AccountWindow.getInstance(this);
        ConfigService.getInstance().refresh();
        file = new ComboBoxInput(new JRadioButton("File Format"), x-350,20)
            {{this.setToolTip("Input your chosen file format here");}};

        date = new DateInput("Date", x-700,25)
            {{this.setToolTip("Input your chosen file with date when it was modified");}};

        sourceFolder = new ComboBoxInput(new JLabel("Source Folder"),x-525,25)
            {{this.setToolTip("Input the URL Directory from where would you send your files");}};

        destFolder = new ComboBoxInput(new JLabel("Destination Folder"),x-525,25)
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
        JComboBox<String> tempBox = file.getTextField();
        tempBox.removeAllItems();

        for(String key: FileMap.getInstance().keySet()) {

            DefaultListModel<String> temp = FileMap.getInstance().get(key);
            tempBox.addItem("Category: " + key);

            for(int i=0; i < temp.getSize(); i++) {
                tempBox.addItem(temp.get(i));
            }
        }
        tempBox.addItem("Other");
        tempBox.setSelectedIndex(-1);
        file.setListener(new ComboboxListener(file, BoxInput.FILE));
    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        Table leftTable = new Table("Include","Category","File Format", "Date");
        Table rightTable = new Table("Source Folder","Destination Folder");
        JScrollPane rightPane = new JScrollPane(rightTable);
        JScrollPane leftPane = new JScrollPane(leftTable);
        leftPane.setPreferredSize(new Dimension(410,380));
        rightPane.setPreferredSize(new Dimension(410,380));
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});
        leftTable.getDefaultModel().addRow(new Object[] {true, "Videos", "mp4", "11/23/25"});


//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setPreferredSize(new Dimension(300,200));
        p.setLayout(new BorderLayout());

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(x,140));
            e.add(file);
            e.add(date);
            e.add(sourceFolder);
            e.add(destFolder);

        }), BorderLayout.NORTH);

        p.add(setPanel(e -> {
            e.add(leftPane);
        }),BorderLayout.WEST);

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(100,280));
            e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));

            Stream.of(      new AddButton()     {{this.setToolTipText("Add Your Configuration");}},
                            new RemoveButton()  {{this.setToolTipText("Remove Your Configuration");}},
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

    public static void clearInstance() {
        mainWindow.setVisible(false);
        mainWindow.dispose();
        mainWindow = null;
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
