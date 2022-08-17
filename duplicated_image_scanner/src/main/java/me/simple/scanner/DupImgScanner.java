package me.simple.scanner;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DupImgScanner {

    public static void main(String[] args) throws Exception {
        List<String> dirPaths = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();

        if (args.length == 0) {
            dirPaths.add(new File("").getAbsolutePath());
        } else {
            dirPaths.addAll(Arrays.asList(args));
        }

//        dirPaths.add("/Users/simple/Desktop/workspace/android/sleepsignin/app/src/main/res/drawable-xxhdpi");

        for (String dirPath : dirPaths) {
            File dirFile = new File(dirPath);
            if (dirFile.isFile()) return;
            File[] files = dirFile.listFiles();
            if (files == null || files.length == 0) return;
            for (File file : files) {
                String md5 = Files.asByteSource(file).hash(Hashing.md5()).toString();
                if (hashMap.containsKey(md5)) {
                    System.out.println("图片重复了");
                    System.out.printf("old = %s\n", hashMap.get(md5));
                    System.out.printf("new = %s\n", file.getName());
                } else {
                    hashMap.put(md5, file.getName());
                }
            }
        }
    }
}