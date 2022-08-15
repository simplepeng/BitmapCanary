package me.simple.bitmapcanary;

import java.io.File;

public class Scanner {

    public static void main(String[] args) {
        try {
            String rootPath;
            double maxValue ;

            if (args.length == 0) {
                rootPath = new File("").getAbsolutePath();
                maxValue = 1.00;
            } else {
                rootPath = args[0];
                maxValue = Double.parseDouble(args[1]);
            }

            ScannerJob job = new ScannerJob();
            job.run(rootPath, maxValue);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
