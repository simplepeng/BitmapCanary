package me.simple.bitmapcanary;

import java.io.File;

import javax.imageio.ImageIO;

public class Scanner {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("args = " + arg);
        }
        File file = new File("");
        System.out.println("args = " + file.getAbsolutePath());
        String rootPath = file.getAbsolutePath();
        File rootDir = new File(rootPath);
        if (!rootDir.isDirectory()) {
            log("rootDir is not Directory -- " + rootPath);
            return;
        }
        traversals(rootDir);

    }

    private static void traversals(File file) {
        File[] files = file.listFiles();
        if (files == null) return;
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                traversals(childFile);
            } else {
                log("file - " + file.getAbsolutePath());
            }
        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
