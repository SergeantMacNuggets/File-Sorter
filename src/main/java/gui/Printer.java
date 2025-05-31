package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;
public class Printer implements Printable {

    public Printer() {
        printImmediately();
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());


        return PAGE_EXISTS;
    }


    public void printImmediately() {
        new Thread(() -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);

            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                } catch (PrinterException exc) {
                    System.out.println("Printing Error: " + exc.getMessage());
                }
            } else {
                System.out.println("You cancelled the print.");
            }
        }).start();
    }
}
