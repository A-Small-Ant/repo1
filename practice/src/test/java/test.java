import com.alphajuns.util.*;
import com.itextpdf.text.DocumentException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class test {

    /**
     * @Author AlphaJunS
     * @Date 19:41 2020/3/2
     * @Description
     */
    @Test
    public void run1() {
        int i = HttpRequestUtil.sendGet("https://www.baidu.com");
        System.out.println(i);
    }

    @Test
    public void run2() {
        String s = HttpRequestUtil.sendGetAndRetrieveContent("https://www.baidu.com");
        System.out.println(s);
    }

    /**
     * @Author AlphaJunS
     * @Date 12:51 2020/3/7
     * @Description
     * @param   
     * @return void
     */
    @Test
    public void testPDF() throws IOException, DocumentException {
        String oldPswFilePath = "D:\\pdfTest\\生产件批准申请书.pdf";
        List<Map<String, ?>> reviews = new ArrayList<>();
        Map<String, String> review = new HashMap<>();
        review.put("result", "approved");
        review.put("type", "1");
        reviews.add(review);
        PDFDocHelper.signPsw(oldPswFilePath, reviews);
    }

    @Test
    public void testGetKeyWordCoordinate() {
        String filePath = "D:\\pdfTest\\生产件批准申请书.pdf";

        String keyWords1 = "乘用车采购部";
        float[] keyWordsCoordinate1 = PdfHelper.getKeyWordsByPath(filePath, keyWords1);
        System.out.println(keyWords1);
        for (int i = 0; i < keyWordsCoordinate1.length; i++) {
            System.out.println("坐标值：" + keyWordsCoordinate1[i]);
        }

        String keyWords2 = "确认意见";
        float[] keyWordsCoordinate2 = PdfHelper.getKeyWordsByPath(filePath, keyWords2);
        System.out.println(keyWords2);
        for (int i = 0; i < keyWordsCoordinate2.length; i++) {
            System.out.println("坐标值：" + keyWordsCoordinate2[i]);
        }
    }

    @Test
    public void testCheckPdf() {
        String filePath = "D:\\pdfTest\\生产件批准申请书.pdf";
        int i = CheckPdfHelper.checkPdf(filePath);
        System.out.println((i == 0)?"合法PDF":"不合法PDF");

        String filePath1 = "D:\\pdfTest\\关键字坐标不匹配文件.pdf";
        int j = CheckPdfHelper.checkPdf(filePath1);
        System.out.println((j == 0)?"合法PDF":"不合法PDF");
    }

    @Test
    public void testZip() {
        String filePath = "D:\\pdfTest\\合格压缩文件.zip";
        // 获取原文所在目录
        // 服务器上时，文件路径为“/”，此处测试需要换成filePath中的“\\”
        //String oldFilePath = filePath.substring(0, filePath.lastIndexOf("/"));
        String oldFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
        System.out.println("原文件路径：" + oldFilePath);
        // 临时目录，原压缩文件解压目录
        String destDirPath = oldFilePath + "\\tmp\\";
        System.out.println("临时路径：" + destDirPath);
        // 将原压缩文件解压到临时目录
        ZipUtil.unzipFile(filePath, destDirPath);

        // 临时目录文件对象
        File destDir = new File(destDirPath);
        // 获取临时目录下的所有文件
        File[] files = destDir.listFiles();
        // 定义变量，保存校验结果
        List<Integer> list = new ArrayList<>();
        // 遍历文件，进行校验
        for (File file: files) {
            String absolutePath = file.getAbsolutePath();
            System.out.println(absolutePath);
            int i = CheckPdfHelper.checkPdf(absolutePath);
            list.add(i);
            // 压缩包中存在不合格PDF文件时
            if (i != 0) {
                break;
            }
        }
        // 判断是否包含不合格PDF文件
        if (list.contains(1)) {
            System.out.println("压缩文件中包含不合格PDF文件");
            // 删除解压缩的文件和临时目录
            ZipUtil.deletefile(destDirPath);
            // 不合格时，不生成新的压缩包文件
            return;
        } else {
            System.out.println("压缩文件PDF文件均符合要求");
        }

        // 获取原压缩文件后缀
        int pos = filePath.lastIndexOf('.');
        String suffix = filePath.substring(pos + 1);
        // 新生成压缩文件路径
        String newFilePath = filePath.substring(0, pos) + ".PSW." + suffix;
        System.out.println("新的压缩文件路径：" + newFilePath);

        // 将检验成功的文件压缩成一个新的压缩包
        ZipUtil.zipFile(newFilePath, files);
        // 删除临时目录
        ZipUtil.deletefile(destDirPath);
    }

    @Test
    public void testDelete() {
        String filePath = "D:\\pdfTest\\tmp";
        ZipUtil.deletefile(filePath);
    }

    @Test
    public void testZipFile() throws IOException {
        String filePath = "D:\\pdfTest\\不合格压缩文件.zip";
        ZipFile zipFile = new ZipFile(filePath);
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        ZipEntry zipEntry;
        while (entries.hasMoreElements()) {
            zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            System.out.println(name);
        }
    }

    @Test
    public void testGetDateFromTimestampByPattern() {
        long seconds = new Date().getTime();
        Timestamp timestamp = new Timestamp(seconds);
        System.out.println(timestamp);
        String pattern = "yyyy-MM-dd";
        String dateFromTimestampByPattern = DateUtils.getDateFromTimestampByPattern(timestamp, pattern);
        System.out.println(dateFromTimestampByPattern);
    }

    @Test
    public void testGetStrFromDateByPattern() {
        String pattern = "yyyy-MM-dd";
        String strFromDateByPattern = DateUtils.getStrFromDateByPattern(new Date(), pattern);
        System.out.println(strFromDateByPattern);
    }


    @Test
    public void testJudgeFileCharset() {
        String path = "D:\\software\\QQ\\QQFile\\2092428070\\FileRecv\\SpringContextUtil.java";
        File file = new File(path);
        String fileCharset = JudgeFileCharset.getFileCharsetByICU4J(file);
        System.out.println(fileCharset);
    }
}
