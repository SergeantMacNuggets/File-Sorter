package gui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;


public class AccountWindow extends JFrame {
    public static AccountWindow accountWindow;
    private JTextField username, password;
    AccountWindow() {
        FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", "#ffa31a" ) );
        FlatMacDarkLaf.setup();
        username = new JTextField();
        password = new JPasswordField();
        new WindowBuilder(this)
                .setDimension(600,350)
                .setWindowConstants(JFrame.EXIT_ON_CLOSE)
                .setComponents(mainPanel())
                .setTitle("Login")
                .build();
    }

    public static AccountWindow getInstance() {
        if(accountWindow == null) {
            accountWindow = new AccountWindow();
        }
        return accountWindow;
    }

    public JPanel mainPanel() {
        JPanel p = new JPanel();
        JLabel l = new JLabel("File Sorter", SwingConstants.CENTER);
        try {
            BufferedImage logo = ImageIO.read(new File("src/main/resources/profile.png"));
            JLabel picLabel = new JLabel(new ImageIcon(logo));
            l.setFont(new Font("Impact", Font.BOLD, 63));
            l.setForeground(SpecificColor.buttonColor);
            l.setPreferredSize(new Dimension(600,50));
            p.setLayout(new MigLayout("","[][][]", "[grow 25][]"));
            p.add(l,"dock north");
            p.add(new JPanel(), "dock west");
            p.add(picLabel, "gapleft 40");
            p.add(loginPanel(), "gapleft 20");
        } catch (IOException e) {
            System.out.println("Missing file");
        }
        return p;
    }

    public JPanel loginPanel() {
        JPanel p = new JPanel();
        JButton b = new JButton(">");
        b.setFont(new Font("Impact", Font.BOLD, 15));
        b.setPreferredSize(new Dimension(45,40));
        b.setBackground(SpecificColor.buttonColor);
        b.setForeground(SpecificColor.buttonText);
        username.setPreferredSize(new Dimension(350,40));
        password.setPreferredSize(new Dimension(300,40));
        p.setLayout(new MigLayout());
        p.add(new JLabel("Username"),"wrap");
        p.add(username,"wrap 15");
        p.add(new JLabel("Password"),"wrap");
        p.add(password, "split 2");
        p.add(b,"wrap 20");
        p.add(new JButton("Forgot Password"), "split 2");
        p.add(new JButton("Enter as Guest"), "gapleft 120");
        return p;
    }

    public void start() {
        this.setVisible(true);
    }
}
