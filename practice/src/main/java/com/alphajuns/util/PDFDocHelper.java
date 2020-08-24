package com.alphajuns.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author AlphaJunS
 * @Date 19:25 2020/3/7
 * @Description 文档帮助类
 */
public class PDFDocHelper {
    private static BaseFont base = null;
    // 获取基础文字
    public static BaseFont getBaseFont() throws DocumentException, IOException {
        if (base == null) {
            try {
                base = BaseFont.createFont("/u01/config/simsun.ttc,1",  BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } catch (Throwable th) {
                base = BaseFont.createFont("C:\\WINDOWS\\Fonts\\SIMSUN.TTC,1",  BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
        }
        return base;
    }

    //psw文件签名
    public static String signPsw(String oldPswFilePath, List<Map<String, ?>> reviews) throws IOException, DocumentException {
        int pos = oldPswFilePath.lastIndexOf('.');
        // 获取文件后缀
        String suffix = oldPswFilePath.substring(pos + 1);
        // 判断是否为pdf文件
        if (!"pdf".equals(suffix.toLowerCase())) {
            throw new RuntimeException("Not supported PSW file");
        }
        return signSinglePsw(oldPswFilePath, reviews);
    }

    //单个psw文件签名
    private static String signSinglePsw(String oldPswFilePath,List<Map<String, ?>> reviews) throws IOException, DocumentException {
        String newPswPath = oldPswFilePath;
        int pos = oldPswFilePath.lastIndexOf('.');
        // 获取文件后缀名
        String suffix = oldPswFilePath.substring(pos + 1);
        // 生成新的文件路径
        newPswPath = oldPswFilePath.substring(0, pos) + ".PSW." + suffix;
        System.out.println("单个psw文件签名生成的新路径：" + newPswPath);

        PdfReader reader = new PdfReader(oldPswFilePath);
        FileOutputStream fout = new FileOutputStream(newPswPath);
        PdfStamper stp = new PdfStamper(reader, fout);

        // 总页数
        System.out.println("PDF总页数：" + reader.getNumberOfPages());

        for (int i = 0; i < reader.getNumberOfPages(); ) {
            // 需要从第一页开始，i放在循环中会报空指针异常
            i++;
            PdfContentByte content = stp.getOverContent(i);
            content.beginText();

            // 设置字体及字号
            content.setFontAndSize(getBaseFont(), 10);

            Map<String, Object> review = (Map<String, Object>) reviews.get(reviews.size() - 1);
            addDeptReview(content, review);
            content.endText();
        }

        stp.close();
        // 将输出流关闭
        fout.close();
        reader.close();
        // 文件读写结束
        System.out.println("PSW文件读写完毕");

        return newPswPath;
    }

    /**
     * @Author AlphaJunS
     * @Date 18:48 2020/3/7
     * @Description 添加水印
     * @param content
     * @param review
     * @return void
     */
    private static void addDeptReview(PdfContentByte content, Map<String, Object> review) {
        if (Integer.parseInt(String.valueOf(review.get("type"))) == 1) {
            content.setColorFill(BaseColor.BLUE);
        } else {
            content.setColorFill(BaseColor.RED);
        }
        // 设置水印位置和内容
        String result = (String) review.get("result");
        System.out.println("水印内容：" + result);
        System.out.println("打印位置坐标：" + pswX[0] + "," + pswY[0]);
        content.showTextAligned(Element.ALIGN_LEFT, result, pswX[0], pswY[0], 0);
    }

    // 打印水印坐标
    private static float[] pswY = {128};
    private static float[] pswX = {337};

}
