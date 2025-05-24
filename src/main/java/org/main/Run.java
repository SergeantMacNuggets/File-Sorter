package org.main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import gui.AccountWindow;

import java.util.Collections;


public class Run {
    public static void main(String[] args) {
        FlatLaf.setGlobalExtraDefaults( Collections.singletonMap( "@accentColor", "#ffa31a" ) );
        FlatMacDarkLaf.setup();
        AccountWindow.getInstance().start();
    }
}