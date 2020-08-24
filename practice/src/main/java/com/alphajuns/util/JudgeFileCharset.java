package com.alphajuns.util;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName JudgeFileCharset
 * @Description 判断文件原来编码方式工具类
 * @Author AlphaJunS
 * @Date 2020/3/18 20:37
 * @Version 1.0
 */
public class JudgeFileCharset {

    public static String getFileCharsetByICU4J(File file) {
        String encoding = null;

        try {
            Path path = Paths.get(file.getPath());
            byte[] data = Files.readAllBytes(path);
            CharsetDetector detector = new CharsetDetector();
            detector.setText(data);
            CharsetMatch match = detector.detect();
            if (match == null) {
                return encoding;
            }
            encoding = match.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encoding;
    }

}
