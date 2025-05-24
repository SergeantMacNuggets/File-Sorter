package listeners;

import gui.*;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class ComboboxListener implements ActionListener {
    BoxInput b;
    Input i;
    public ComboboxListener(Input i, BoxInput b) {
        this.b = b;
        this.i = i;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (b) {
            case FILE:
                JComboBox<String> file = (JComboBox<String>) i.getTextField();
                if(Objects.equals(file.getSelectedItem(), "Other")) {
                    file.setSelectedIndex(-1);
                    Settings.getInstance().start();
                }
                break;
            case FOLDER:
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
                break;
        }
    }

}