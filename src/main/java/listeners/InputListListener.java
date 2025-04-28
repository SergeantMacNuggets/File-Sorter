package listeners;

import gui.InputList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputListListener {

    public static void setOneClickListener(InputList list) {
        list.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(list.getList().getSelectedValue());
            }
        });
    }

}
