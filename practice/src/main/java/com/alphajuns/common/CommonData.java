package com.alphajuns.common;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName CommonData
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/17 21:06
 * @Version 1.0
 */
public class CommonData {
    private static Logger logger = Logger.getLogger(CommonData.class);

    /**
     * @Author AlphaJunS
     * @Date 21:10 2020/3/17
     * @Description 开始页
     * @param params
     * @return int
     */
    public static int startPage(Map<String, Object> params) {
        int start;
        int page = MapUtils.getIntValue(params, Constants.PAGE);
        int rows = MapUtils.getIntValue(params, Constants.ROWS);
        if(page==0){
            logger.error(Constants.PAGE+" is 0");
        }
        start = (page - 1) * rows;
        return start;
    }

    /**
     * @Author AlphaJunS
     * @Date 21:11 2020/3/17
     * @Description 结束页
     * @param params
     * @return int
     */
    public static int endPage(Map<String, Object> params) {
        int end;
        end = MapUtils.getIntValue(params, Constants.ROWS);

        if (end == 0) {
            final int POS = 10;
            end = POS;
        }

        return startPage(params) + end;
    }

}
