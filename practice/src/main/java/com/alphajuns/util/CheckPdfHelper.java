package com.alphajuns.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import java.io.IOException;

/**
 * @ClassName CheckPdfHelper
 * @Description 校验PDF是否为指定文件
 * @Author AlphaJunS
 * @Date 2020/3/7 19:31
 * @Version 1.0
 */
public class CheckPdfHelper {
    // 定义PDF中关键字及其坐标位置，用于判断校验上传的PDF文件是否合法
    private static String KEY_WORD = "ANPQP";
    private static float POINT_X = (float) 36.0;
    private static float POINT_Y = (float) 810.2003;

    /**
     * 检验PDF是否合格
     * @param filePath
     * @return
     */
    public static int checkPdf(String filePath) {
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
        if ("pdf".equals(suffix.toLowerCase())) {
            return checkSinglePdf(filePath);
        } else {
            return 1;
        }
    }

    /**
     * 检验单个pdf是否合格
     * @param filePath
     * @return
     */
    private static int checkSinglePdf(String filePath) {
        float[] point = getKeyWordsByPath(filePath);
        if (point != null) {
            if(isEqualOfFloat(point[0], POINT_X) && isEqualOfFloat(point[1], POINT_Y)) {
                return 0;
            } else {
                return 1;
            }
        }
        return 1;
    }

    /**
     * @Author AlphaJunS
     * @Date 18:01 2020/3/7
     * @Description 根据路径获取pdf中关键字的坐标
     * @param  filepath
     * @return float[]
     */
    private static float[] getKeyWordsByPath(String filepath) {
        float[] coordinate = null;
        try{
            PdfReader pdfReader = new PdfReader(filepath);
            coordinate = getKeyWords(pdfReader);
            pdfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinate;
    }

    /**
     * @Author AlphaJunS
     * @Date 18:02 2020/3/7
     * @Description 根据pdfreader获取pdf中关键字的坐标
     * @param pdfReader
     * @return float[]
     */
    private static float[] getKeyWords(PdfReader pdfReader) {
        float[] coordinate = null;
        int page = 0;
        try{
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            CustomRenderListener renderListener = new CustomRenderListener();
            renderListener.setKeyWord(KEY_WORD);
            for (page = 1; page <= pageNum; page++) {
                renderListener.setPage(page);
                pdfReaderContentParser.processContent(page, renderListener);
                coordinate = renderListener.getPcoordinate();
                if (coordinate != null) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinate;
    }

    /**
     * @Author AlphaJunS
     * @Date 18:05 2020/3/7
     * @Description 判断两个浮点数是否一致
     * @param a
     * @param b
     * @return boolean
     */
    public static boolean isEqualOfFloat(float a, float b){
        if(Math.abs(Math.floor(a) - Math.floor(b)) <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
