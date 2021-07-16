package me.simple.bitmapcanary;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Scanner {

    private static List<File> analysisFiles = new ArrayList<>();

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("args = " + arg);
        }
        File file = new File("");
        System.out.println("project path = " + file.getAbsolutePath());
        String rootPath = file.getAbsolutePath();
        File rootDir = new File(rootPath);
        if (!rootDir.isDirectory()) {
            log("rootDir is not Directory -- " + rootPath);
            return;
        }
        //
        traversals(rootDir);
        //
        for (File file1 : analysisFiles) {
            log("file1 - " + file1.getAbsolutePath());
        }
        //
        startAnalysis();
        //
        analysisFiles.clear();
    }

    private static void traversals(File file) {
        if (file.isFile()) {
            addAnalysis(file);
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File child : files) {
            if (child.isDirectory()) {
                traversals(child);
            }
            if (child.isFile()) {
                addAnalysis(child);
            }
        }
    }

    private static void addAnalysis(File file) {
        String path = file.getAbsolutePath();
        String parentName = file.getParent();
        String name = file.getName();
        if (parentName == null || parentName.length() < 1) return;
        if ((parentName.contains("drawable") || parentName.contains("mipmap"))
                && !path.contains("/build/generated")
                && !name.endsWith("xml")) {
            analysisFiles.add(file);
        }
    }

    private static void startAnalysis() {
        for (File file : analysisFiles) {
            try {
                log("path - " + file.getAbsolutePath());

                if (file.getName().endsWith("webp")) {
                    readWebp(file);
                    continue;
                }

                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    log("image is null - " + file.getAbsolutePath());
                    return;
                }
                log("width - " + image.getWidth());
                log("height - " + image.getHeight());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static void readWebp(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[30];
        fis.read(bytes, 0, bytes.length);
        int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
        int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
        log("width - " + width);
        log("height - " + height);
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
