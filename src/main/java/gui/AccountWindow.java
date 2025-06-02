package gui;

import back_end.AccountFactory;
import back_end.DatabaseConnection;
import net.miginfocom.swing.MigLayout;
import back_end.AccountType;
import javax.imageio.ImageIO;
import javax.swing.JTextField;
import java.awt.Shape;
import java.awt.Graphics;
import javax.swing.JPasswordField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

class RoundJTextField extends JTextField {
    private Shape shape;
    public RoundJTextField(int size) {
        super(size);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        super.paintComponent(g);
    }
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
    }
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        }
        return shape.contains(x, y);
    }
}

class RoundJPasswordField extends JPasswordField {
    private Shape shape;
    public RoundJPasswordField(int size) {
        super(size);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        super.paintComponent(g);
    }
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
    }
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
        }
        return shape.contains(x, y);
    }
}

public class AccountWindow extends JDialog {
    private static AccountWindow accountWindow;
    private final JFrame parentFrame;
    private final JTextField username;
    private JTextField password;
    private JTextField newPassField;
    private JButton submit, guestIn, changePass;
    private JPanel mainPanel, currentPanel;
    private boolean panelState;
    private AccountWindow(JFrame parentFrame) {
        super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        DatabaseConnection.run();
        submit = new JButton(">");
        guestIn = new JButton("Enter as Guest");
        changePass = new JButton();
        this.parentFrame = parentFrame;
        changePass.setMaximumSize(new Dimension(120,30));
        changePass.setMinimumSize(new Dimension(120,30));
        guestIn.setMaximumSize(new Dimension(120,30));
        guestIn.setMinimumSize(new Dimension(120,30));
        username = new RoundJTextField(30);
        panelState = true;
        currentPanel = loginPanel();
        setup();
        this.setPreferredSize(new Dimension(600,350));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(mainPanel);
        try {
            BufferedImage logo = ImageIO.read(this.getClass().getResourceAsStream("/icon1.png"));

            this.setIconImage(
                    new ImageIcon(logo).getImage()
            );
        } catch (IOException e) {
        }this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setResizable(false);
        this.setTitle("Login");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setVisible(true);
    }

    public static AccountWindow getInstance(JFrame parentFrame) {
        if(accountWindow == null) {
            accountWindow = new AccountWindow(parentFrame);
        }
        return accountWindow;
    }

    public static void clearInstance() {
        accountWindow.setVisible(false);
        accountWindow.dispose();
        accountWindow = null;
    }

    public void setup() {
        JLabel l = new JLabel("File Sorter", SwingConstants.CENTER);
        mainPanel = new JPanel();
        try {
            BufferedImage logo = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/profile.png" )));
            JLabel picLabel = new JLabel(new ImageIcon(logo));
            changePass.addActionListener(changePanel());
            submit.addActionListener(submitListener());
            guestIn.addActionListener(guestListener());
            l.setFont(new Font("Impact", Font.BOLD, 63));
            l.setForeground(SpecificColor.buttonColor);
            l.setPreferredSize(new Dimension(600,50));
            mainPanel.setLayout(new MigLayout("","[][][]", "[grow 25][]"));
            mainPanel.add(l,"dock north");
            mainPanel.add(new JPanel(), "dock west");
            mainPanel.add(picLabel, "gapleft 40");
            mainPanel.add(currentPanel, "gapleft 20");
        } catch (IOException e) {
            System.out.println("Missing file");
        }
    }

    private void panelSwitcher(JPanel p) {
        mainPanel.remove(currentPanel);
        currentPanel = p;
        mainPanel.add(p, "gapleft 20");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private ActionListener changePanel() {
        return _ -> {
            if (panelState){
                panelSwitcher(signupPanel());
            } else panelSwitcher(loginPanel());
            panelState = !panelState;
        };
    }

    public JPanel loginPanel() {
        JPanel p = new JPanel();
        changePass.setText("Forgot Password");
        changePass.setFont(new Font("Ariel",Font.PLAIN, 11));
        password = new RoundJPasswordField(26);
        submit.setFont(new Font("Impact", Font.BOLD, 15));
        submit.setPreferredSize(new Dimension(45,40));
        submit.setBackground(SpecificColor.buttonColor);
        submit.setForeground(SpecificColor.buttonText);
        username.setPreferredSize(new Dimension(350,40));
        password.setPreferredSize(new Dimension(300,40));
        p.setLayout(new MigLayout());
        p.add(new JLabel("Username"){{setFont(new Font("Impact", Font.PLAIN, 16));}},"wrap");
        p.add(username,"wrap 15");
        p.add(new JLabel("Password"){{setFont(new Font("Impact", Font.PLAIN, 16));}},"wrap");
        p.add(password, "split 2");
        p.add(submit,"wrap 20");
        p.add(changePass, "split 2");
        p.add(guestIn, "gapleft 120");
        return p;
    }

    public JPanel signupPanel() {
        JPanel p = new JPanel();
        password = new RoundJPasswordField(12);
        newPassField = new RoundJPasswordField(12);
        changePass.setText("<- Go Back");
        submit.setFont(new Font("Impact", Font.BOLD, 15));
        submit.setPreferredSize(new Dimension(45,40));
        submit.setBackground(SpecificColor.buttonColor);
        submit.setForeground(SpecificColor.buttonText);
        username.setPreferredSize(new Dimension(350,40));
        password.setPreferredSize(new Dimension(150,40));
        newPassField.setPreferredSize(new Dimension(150,40));
        p.setLayout(new MigLayout());
        p.add(new JLabel("Username"){{setFont(new Font("Impact", Font.PLAIN, 16));}},"wrap");
        p.add(username,"wrap 15");
        p.add(new JLabel("Old Password"){{setFont(new Font("Impact", Font.PLAIN, 14));}}, "split 2");
        p.add(new JLabel("New Password"){{setFont(new Font("Impact", Font.PLAIN, 14));}}, "gapleft 75");
        p.add(new JLabel(),"wrap");
        p.add(password, "split 3");
        p.add(newPassField);
        p.add(submit,"wrap 20");
        p.add(changePass, "split 2");
        p.add(guestIn, "gapleft 120");
        return p;
    }

    private ActionListener submitListener() {
        return _ -> {
            boolean loginButtonState = AccountFactory.getAccount(AccountType.ADMIN).isEqual(username.getText(),password.getText());
            if(!panelState) {
                if(loginButtonState) {
                    AccountFactory.getAccount(AccountType.ADMIN).changePassword(username.getText(),newPassField.getText());
                    panelSwitcher(loginPanel());
                    panelState = !panelState;
                }
            } else if(loginButtonState) {
                AccountFactory.getAccount(AccountType.ADMIN);
                parentFrame.setVisible(true);
                this.setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(null, "Wrong Username or Password");
            }
        };
    }

    private ActionListener guestListener() {
        return _->{
            AccountFactory.getAccount(AccountType.GUEST);
            parentFrame.setVisible(true);
            this.setVisible(false);
        };
    }

}

