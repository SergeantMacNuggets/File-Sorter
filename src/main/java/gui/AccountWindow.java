package gui;

import back_end.Account;
import back_end.AccountFactory;
import back_end.DatabaseConnection;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.sun.tools.javac.Main;
import net.miginfocom.swing.MigLayout;
import back_end.AccountType;
import javax.imageio.ImageIO;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@FunctionalInterface
interface SubmitFunction {
    void submitPerform() throws Exception;
}

@FunctionalInterface
interface PanelFunction {
    void panelPerform();
}

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

public class AccountWindow extends JDialog implements NativeKeyListener {
    private static AccountWindow accountWindow;
    private final MainWindow parentFrame;
    private final JTextField username;
    private JTextField password;
    private JTextField newPassField;
    private JButton submit, guestIn, leftButton;
    private JPanel mainPanel, currentPanel;
    private JLabel forgotPass;
    private SubmitFunction function;
    private PanelFunction panelFunction;

    private AccountWindow(MainWindow parentFrame) {
        super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
        DatabaseConnection.run();
        submit = new JButton(">");
        guestIn = new JButton("Enter as Guest");
        leftButton = new JButton();
        this.parentFrame = parentFrame;

        leftButton.setMaximumSize(new Dimension(120,30));
        leftButton.setMinimumSize(new Dimension(120,30));
        guestIn.setMaximumSize(new Dimension(120,30));
        guestIn.setMinimumSize(new Dimension(120,30));
        username = new RoundJTextField(30);
        currentPanel = AccountFactory.getAccount(AccountType.USER).isTableEmpty() ? signupPanel() : loginPanel();
        setup();
        this.setPreferredSize(new Dimension(600,350));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(mainPanel);
        try {
            BufferedImage logo = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/icon1.png")));

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

    public static AccountWindow getInstance(MainWindow parentFrame) {
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
            guestIn.addActionListener(guestListener());
            submit.addActionListener(submitListener());
            leftButton.addActionListener(panelListener());
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

    private void setFunction(SubmitFunction function) {
        this.function = function;
    }

    private void setPanelFunction(PanelFunction panelFunction) {
        this.panelFunction = panelFunction;
    }


    public JPanel loginPanel() {
        JPanel p = new JPanel();
        forgotPass = new JLabel("Forgot Password?");
        forgotPass.setFont(new Font("Ariel", Font.PLAIN, 10));
        forgotPass.setForeground(SpecificColor.buttonColor);
        leftButton.setText("Create Account");
        leftButton.setFont(new Font("Ariel",Font.PLAIN, 11));
        password = new RoundJPasswordField(26);
        submit.setFont(new Font("Impact", Font.BOLD, 15));
        submit.setPreferredSize(new Dimension(45,40));
        submit.setBackground(SpecificColor.buttonColor);
        submit.setForeground(SpecificColor.buttonText);
        username.setPreferredSize(new Dimension(350,40));
        password.setPreferredSize(new Dimension(300,40));

        setFunction(()-> {
            boolean loginButtonState = Account.isEqual(username.getText(),password.getText());
            if(loginButtonState) {
                Account.clearInstance();
                AccountFactory.getAccount(AccountType.USER);
                parentFrame.setVisible(true);
                this.setVisible(false);

            } else {
                throw new Exception();
            }
        });
        p.setLayout(new MigLayout());
        p.add(new JLabel("Username"){{setFont(new Font("Impact", Font.PLAIN, 16));}},"wrap");
        p.add(username,"wrap 15");
        p.add(new JLabel("Password"){{setFont(new Font("Impact", Font.PLAIN, 16));}},"split 2");
        p.add(forgotPass,"gapleft 140");
        p.add(new JLabel(),"wrap");
        p.add(password, "split 2");
        p.add(submit,"wrap 20");
        p.add(leftButton, "split 2");
        p.add(guestIn, "gapleft 120");

        forgotPass.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelSwitcher(changePassPanel());
            }
        });

        setPanelFunction(()-> panelSwitcher(signupPanel()));
        
        return p;
    }

    public JPanel signupPanel() {
        JPanel p = new JPanel();
        password = new RoundJPasswordField(12);
        newPassField = new RoundJPasswordField(12);
        leftButton.setText("<- Go Back");
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
        p.add(new JLabel("Password"){{setFont(new Font("Impact", Font.PLAIN, 14));}}, "split 2");
        p.add(new JLabel("Confirm Password"){{setFont(new Font("Impact", Font.PLAIN, 14));}}, "gapleft 100");
        p.add(new JLabel(),"wrap");
        p.add(password, "split 3");
        p.add(newPassField);
        p.add(submit,"wrap 20");
        p.add(leftButton, "split 2");
        p.add(guestIn, "gapleft 120");

        setFunction(()-> {
            try {
                if (password.getText().equals(newPassField.getText())) {
                    if(username.getText().isEmpty() || password.getText().isEmpty()) throw new SQLException();
                    Account.addUser(username.getText(), password.getText());
                    panelSwitcher(loginPanel());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Username already exist");
            }
        });

        setPanelFunction(()-> panelSwitcher(loginPanel()));
        return p;
    }

    public JPanel changePassPanel() {
        JPanel p = new JPanel();
        password = new RoundJPasswordField(12);
        newPassField = new RoundJPasswordField(12);
        leftButton.setText("<- Go Back");
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
        p.add(leftButton, "split 2");
        p.add(guestIn, "gapleft 120");

        setFunction(()->{
            boolean loginButtonState = Account.isEqual(username.getText(),password.getText());
            if(loginButtonState) {
                Account.changePassword(username.getText(),newPassField.getText());
                panelSwitcher(loginPanel());
            } else {
                throw new Exception();
            }
        });

        setPanelFunction(()-> panelSwitcher(loginPanel()));

        return p;
    }

    private ActionListener panelListener() {
        return _-> {
            this.panelFunction.panelPerform();
        };
    }


    private ActionListener submitListener() {
        return _ -> {
            try {
                this.function.submitPerform();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong Username or Password");
            }

        };
    }

    private ActionListener guestListener() {
        return _->{
            String username = JOptionPane.showInputDialog(null,"Input the username you want to visit:");
            if(!username.isEmpty() && Account.isEqual(username)) {
                JOptionPane.showMessageDialog(null, "Logging in as " + username);
                Account.clearInstance();
                AccountFactory.getAccount(AccountType.GUEST);
                parentFrame.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Username not found");
            }
        };
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        int key = e.getKeyCode();
        if(key == NativeKeyEvent.VC_ENTER) {
            try {
                this.function.submitPerform();
            } catch (Exception _) {
                JOptionPane.showMessageDialog(null, "Wrong Username or Password");
            }
        }
    }
}