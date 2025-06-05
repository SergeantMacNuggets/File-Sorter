package listeners;
import back_end.Account;
import gui.InputList;

import javax.swing.SwingUtilities;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class InputListListener {


    public static void setLeftOneClickListener(InputList list, ClickListener clickListener) {
        list.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    clickListener.perform();
                }
            }
        });
    }

    public static void setLeftDoubleClick(InputList list, ClickListener clickListener) {
        list.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)){
                    if (e.getClickCount() % 2 == 0 && !e.isConsumed()) {
                        clickListener.perform();
                    }
                }
            }
        });
    }

    public static ClickListener disabler(InputList list) {
        return ()->{
            if (Account.getInstance().getState()){
                StringBuilder str = new StringBuilder(list.getList().getSelectedValue());
                str.setCharAt(1, (str.charAt(1) == '+') ? '-' : '+');
                list.getModel().setElementAt(str.toString(), list.getList().getSelectedIndex());
            }
        };
    }

    public static void setRightDoubleClick(InputList list, ClickListener clickListener) {
        if (Account.getInstance().getState()){
            list.getList().addMouseListener(new MouseAdapter() {
                @SuppressWarnings("unchecked")
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        JList<String> temp = (JList<String>) e.getSource();
                        temp.setSelectedIndex(temp.locationToIndex(e.getPoint()));
                        if (e.getClickCount() % 2 == 0 && !e.isConsumed()) {
                            clickListener.perform();
                        }
                    }
                }
            });
        }
    }

}
