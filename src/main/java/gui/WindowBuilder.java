package gui;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WindowBuilder {
    private JFrame frame;
    private Dimension dimension;
    private LayoutManager layoutManager;
    private String title;
    int windowConstants;
    JComponent[] components;

    public WindowBuilder(JFrame frame) {
        this.frame = frame;
    }

    public WindowBuilder setDimension(int x, int y) {
        this.dimension = new Dimension(x,y);
        return this;
    }

    public WindowBuilder setLayout(LayoutManager layoutManager){
        this.layoutManager = layoutManager;
        return this;
    }

    public WindowBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public WindowBuilder setWindowConstants(int windowConstants) {
        this.windowConstants = windowConstants;
        return this;
    }

    public WindowBuilder setComponents(JComponent... components) {
        this.components = components;
        return this;
    }

    public void build() {
        try {
            BufferedImage logo = ImageIO.read(this.getClass().getResourceAsStream("/icon1.png"));

            frame.setIconImage(
                    new ImageIcon(logo).getImage()
            );
        } catch (IOException e) {

        }
        frame.setDefaultCloseOperation(windowConstants);
        frame.setPreferredSize(dimension);
        frame.setTitle(title);

        if (components != null){
            for (JComponent c : components)
                frame.getContentPane().add(c);
        }


        if(layoutManager != null) {
            frame.setLayout(layoutManager);
        }

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }

}
