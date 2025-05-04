package gui;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import java.awt.*;

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
        frame.setDefaultCloseOperation(windowConstants);
        frame.setPreferredSize(dimension);
        frame.setTitle(title);

        frame.setIconImage(
                new ImageIcon("/src/main/resources/icon.png").getImage()
        );

        if(layoutManager != null) {
            frame.setLayout(layoutManager);
        }

        for(JComponent c: components)
            frame.add(c);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
    }

}
