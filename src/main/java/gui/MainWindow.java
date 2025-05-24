package gui;

import listeners.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.stream.Stream;

interface PanelMaker {
    void set(JPanel p);
}

public class MainWindow extends JFrame {

    private static MainWindow mainWindow;
    private final InputList rightList, leftList;
    private final ComboBoxInput file;
    private final DateInput date;
    private final Input sourceFolder, destFolder;
    int x = 700, y = 500;
    private MainWindow() {

        rightList = new InputList(new Dimension(290,280));
        leftList = new InputList(new Dimension(290,280));
        leftList.hasDisabler(true);
        file = new ComboBoxInput(new JRadioButton("File Format"), x-150,20)
            {{this.setToolTip("Input your chosen file format here");}};
        date = new DateInput("Date", x-600,25)
            {{this.setToolTip("Input your chosen file with date when it was modified");}};
        sourceFolder = new ComboBoxInput(new JLabel("Source Folder"),x-375,25)
            {{this.setToolTip("Input the URL Directory from where would you send your files");}};
        destFolder = new ComboBoxInput(new JLabel("Destination Folder"),x-375,25)
            {{this.setToolTip("Input the URL Directory to where would you send your files");}};
        new WindowBuilder(this)
                .setDimension(x,y)
                .setTitle("Automatic File Sorter")
                .setLayout(null)
                .setWindowConstants(JFrame.EXIT_ON_CLOSE)
                .setComponents(mainPanel())
                .build();

        this.updateFormat();
        this.setJMenuBar(new MenuBar());
    }
    public void start() {
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
    }

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300,200));
        p.setLayout(new BorderLayout());

        leftList.setInput(file,date,sourceFolder);
        leftList.setChildList(rightList);
        rightList.setInput(destFolder);
        InputListListener.setLeftDoubleClick(leftList, ()->{
            new FileWindow(leftList.getList().getSelectedValue().toString()).start();
        });
        rightList.getList().addListSelectionListener(_->{
            leftList.getList().setSelectedIndex(rightList.getList().getSelectedIndex());
        });
        leftList.getList().addListSelectionListener(_->{
            rightList.getList().setSelectedIndex(leftList.getList().getSelectedIndex());
        });

        InputListListener.setRightDoubleClick(leftList, InputListListener.disabler(leftList));
        file.setListener(new ComboboxListener(file, BoxInput.FILE));
        sourceFolder.setListener(new ComboboxListener(sourceFolder,BoxInput.FOLDER));
        destFolder.setListener(new ComboboxListener(destFolder,BoxInput.FOLDER));
        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(x,140));
            e.add(file);
            e.add(date);
            e.add(sourceFolder);
            e.add(destFolder);

        }), BorderLayout.NORTH);

        p.add(setPanel(e -> {
            e.add(leftList);
        }),BorderLayout.WEST);

        p.add(setPanel(e -> {

            e.setPreferredSize(new Dimension(100,280));
            e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));

            Stream.of(      new AddButton(leftList)     {{this.setToolTipText("Add Your Configuration");}},
                            new RemoveButton(leftList)  {{this.setToolTipText("Remove Your Configuration");}},
                            new ClearButton(leftList)   {{this.setToolTipText("Delete All Your Configurations");}},
                            new UndoButton(leftList)    {{this.setToolTipText("Add or Remove Your Previous Configuration");}},
                            new JButton("Run")          {{this.setToolTipText("Start Sorting");}})
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

            e.add(rightList);

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
//        subItem[0].addActionListener(_->new Printer());
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
            AccountWindow.getInstance().start();
        });
        account.add(signOut);
        return account;
    }



}
