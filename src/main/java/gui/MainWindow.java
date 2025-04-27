package gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

interface PanelMaker {
    void set(JPanel p);
}

public class MainWindow extends JFrame {
    private static MainWindow mainWindow;
    int x = 700, y = 520;
    private MainWindow() {
        new WindowBuilder(this)
                .setDimension(x,y)
                .setTitle("Automatic File Sorter")
                .setLayout(null)
                .setWindowConstants(JFrame.EXIT_ON_CLOSE)
                .setComponents(mainPanel())
                .build();
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

    private JPanel mainPanel() {
        JPanel p = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300,200));
        p.setLayout(new BorderLayout());
        p.add(setPanel(e -> {
            e.setPreferredSize(new Dimension(x,160));
            e.add(new ComboBoxInput("File Format", x-155,20));
            e.add(new DateInput("Date", x-600,25));
            e.add(new DirectoryInput("Source Folder",x-380,25));
            e.add(new DirectoryInput("Destination Folder",x-380,25));
        }), BorderLayout.NORTH);
        p.add(setPanel(e -> {
            e.add(new InputList(new Dimension(290,280)));
        }),BorderLayout.WEST);

        p.add(setPanel(e -> {
            e.setPreferredSize(new Dimension(100,280));
            e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));
            Stream.of(new JButton("Add"),new JButton("Remove"),
                            new JButton("Clear"), new JButton("Undo"),
                            new JButton("Run"))
                    .forEach(b ->
                            {
                                b.setPreferredSize(new Dimension(80, 25));
                                e.add(setPanel(k -> k.add(b)));
                            }
                    );
        }),BorderLayout.CENTER);

        p.add(setPanel(e -> {
            e.add(new InputList(new Dimension(290,280)));
        }),BorderLayout.EAST);
        return p;
    }

}

class MenuBar extends JMenuBar {
    MenuBar() {
        JMenu[] menu = {getFileMenu(), new JMenu("Edit"),
                new JMenu("View"), new JMenu("Account"), new JMenu("Help")
        ,new JMenu("About")};

        for(JMenu m: menu) add(m);
    }

    private JMenu getFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem[] subItem = {new JMenuItem("Print"), new JMenuItem("Quit")};

        subItem[1].addActionListener(e -> System.exit(0));
        for(JMenuItem i: subItem) file.add(i);

        return file;
    }


}
