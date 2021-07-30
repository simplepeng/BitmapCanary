package me.simple.bitmapcanary;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

public class ScannerJob {

    private final List<File> analyseFiles = new ArrayList<>();

    private String mRootPath = "";
    private double mMaxValue = 3.00;

    private void println(String msg) {
        System.out.println(msg);
    }

    public void run(String rootPath, double maxValue) {
        this.mRootPath = rootPath;
        this.mMaxValue = maxValue;

        new Thread(new Runnable() {
            @Override
            public void run() {
                File rootDir = new File(mRootPath);
                println("--------------------------------------------------------------------------------------------");
                println("rootPath -- " + rootDir.getAbsolutePath());
                if (!rootDir.isDirectory()) {
                    println("rootPath is not Directory");
                    return;
                }

                //遍历
                traversals(rootDir);
                //开始分析
                startAnalysis();
                //清空集合
                analyseFiles.clear();
            }
        }).start();
    }

    /**
     * 递归遍历项目内所有目录
     */
    public void traversals(File file) {
        if (file.isFile()) {
            addAnalyse(file);
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
                addAnalyse(child);
            }
        }
    }

    /**
     * 将符合规则的File添加进分析列表
     */
    public void addAnalyse(File file) {
        String path = file.getAbsolutePath();
        String parentName = file.getParent();
        String name = file.getName();

        if (parentName == null || parentName.length() < 1) return;
        if (path.contains("/build/generated")) return;
        if (name.endsWith("xml")) return;

        if ((parentName.contains("drawable") || parentName.contains("mipmap"))) {
            if (name.endsWith("png") || name.endsWith("jpg")
                    || name.endsWith("jpeg") || name.endsWith("webp")
                    || name.endsWith("gif") || name.endsWith("bmp")) {
                analyseFiles.add(file);
            }
        }
    }

    /**
     * 开始分析
     */
    public void startAnalysis() {
        for (File file : analyseFiles) {
            try {
                //webp
                if (file.getName().endsWith("webp")) {
                    readWebp(file);
                    continue;
                }
                //png,jpg,gif
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    println("image is null - " + file.getAbsolutePath());
                    return;
                }
                computeMemory(file, image.getWidth(), image.getHeight());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * webP单独处理
     */
    public void readWebp(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[30];
        fis.read(bytes, 0, bytes.length);
        int width = ((int) bytes[27] & 0xff) << 8 | ((int) bytes[26] & 0xff);
        int height = ((int) bytes[29] & 0xff) << 8 | ((int) bytes[28] & 0xff);
        computeMemory(file, width, height);
    }

    /**
     * 计算占用内存
     */
    public void computeMemory(File file, int width, int height) {
        long memory = width * height * 4;
        double maxByte = mMaxValue * 1024 * 1024;

        if (memory >= maxByte) {
            String m = String.format(Locale.getDefault(), "%.02fM", memory / 1024f / 1024f);
            println("--------------------------------------------------------------------------------------------");
            println("this bitmap is too large in memory  -- " + m);
            println("path == " + file.getAbsolutePath());
        }
    }
}
