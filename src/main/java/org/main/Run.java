package org.main;
import back_end.DatabaseService;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import gui.AccountWindow;
import gui.MainWindow;

import java.util.Collections;


public class Run {
    public static void main(String[] args) {
//        #ffa31a - Orange
        FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", "#ffa31a" ) );
        FlatMacDarkLaf.setup();
//
        MainWindow.getInstance();
//        new DatabaseService();
    }
}