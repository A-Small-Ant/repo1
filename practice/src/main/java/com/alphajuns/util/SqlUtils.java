package com.alphajuns.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alphajuns.common.Constants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @ClassName SqlUtils
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/17 20:45
 * @Version 1.0
 */
public class SqlUtils {

    private static Logger logger = Logger.getLogger(SqlUtils.class);

    /**
     * 分页方法
     *
     * @param originalSql originalSql
     * @param orderBy orderBy
     * @param start start
     * @param end end
     * @return String
     */

    public static String addPageSplitSql(String originalSql, String orderBy, int start, int end) {
        int pos;
        pos = originalSql.toUpperCase().indexOf("FROM");
        if (pos == -1) {
            throw new RuntimeException("Unrecoginzed SQL");
        }
        String strS1;
        strS1 = originalSql.substring(0, pos).trim();
        String strS2;
        strS2 = originalSql.substring(pos);

        strS1 += ", row_number() over (order by " + orderBy + ") as rownumber ";

        String result = "with\r\n\tallResult as (\r\n" + strS1 + strS2 + "\r\n\t),\r\n";
        result += "\tTN as (select count(*) as TotalCount from allResult)\r\n";
        result += "select mn.TotalCount, ar.* from allResult ar, TN mn\r\nwhere ";

        result += "ar.rownumber > " + start + " and ar.rownumber <= " + end;
        return result;
    }

    /**
     * <p>
     * Description: getSqlConditionByParams like模糊查询
     * </p>
     *
     * @param params params
     * @param tableName tableName
     * @return string
     */
    public static String getSqlConditionLikesByParams(Map<String, Object> params, String tableName) {
        String c = "";
        for (String key : params.keySet()) {
            if (params.get(key) != null && !"".equals(params.get(key))) {
                if (tableName != null) {
                    c += " and " + tableName + "." + key + " like '%" + params.get(key) + "%'";
                } else {
                    c += " and " + key + " like '%" + params.get(key) + "%'";
                }
            }
        }
        return c;
    }

    /**
     * <p>
     * Description: 排序字段
     * </p>
     *
     * @param sortName sortName
     * @param sortOrder sortOrder
     * @return string
     */
    public static String getOrderBy(String sortName, String sortOrder) {
        if (StringUtils.isBlank(sortOrder) && StringUtils.isBlank(sortName)) {
            return "";
        }
        String tableOrder;
        tableOrder = "  order by " + sortName + "  " + sortOrder;
        return tableOrder;
    }

    /**
     * 分页查询
     * @param sql sql
     * @param params params
     * @return String
     */
    public static String pageRowsParam(String sql, Map<String, Object> params) {

        int page = 0;
        page = MapUtils.getIntValue(params, "page");
        int rows = 0;
        rows = MapUtils.getIntValue(params, "rows");
        if (page < 1) {
            page = 1;
        }
        int startIndex = 0;
        startIndex = (page - 1) * rows;
        int endIndex = 0;
        endIndex = startIndex + rows;

        sql += " and rownumber > " + startIndex + " and rownumber  <= " + endIndex;

        return sql;
    }

    /**
     * select * from(SELECT ROWNUM as rownumber,T1.PART_NUM AS
     * teilNum,T2.PART_NAME_ZH AS teilNameZh,T2.PART_NAME_DE AS teilNameDe FROM
     * TCM_TT_PART T1, TCM_TT_PART_NAME T2 WHERE T1.TCM_TT_PART_NAME_ID =
     * T2.TCM_TT_PART_NAME_ID) where 1=1 and rownumber > 0 and rownumber <= 10
     * order by teilNum asc;
     *
     * 查找所有
     *
     * @param sql sql
     * @param params params
     * @return String String
     */
    public static String getSearch(String sql, Map<String, Object> params) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        String select;
        select = "select * from(" + sql + ") where 1=1 ";
        String sqlRow;
        sqlRow = pageRowsParam(select, params);

        return sqlRow;
    }

    /**
     * select * from(SELECT ROWNUM as rownumber,T1.PART_NUM AS
     * teilNum,T2.PART_NAME_ZH AS teilNameZh,T2.PART_NAME_DE AS teilNameDe FROM
     * TCM_TT_PART T1, TCM_TT_PART_NAME T2 WHERE T1.TCM_TT_PART_NAME_ID =
     * T2.TCM_TT_PART_NAME_ID) where 1=1 and rownumber > 0 and rownumber <= 10
     * order by teilNum asc;
     *
     * 查询结果按指定字段排序
     *
     * @param sql sql
     * @param params params
     * @return String String
     */
    public static String getSearchOderBy(String sql, Map<String, Object> params) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        final String SORTNAME = (String) params.get(Constants.SORT);
        final String SORTORDER = (String) params.get(Constants.ORDER);
        logger.info(" sortName= " + SORTNAME + "-----sortOrder=" + SORTORDER);
        String select;
        select = "select * from(" + sql + ") where 1=1 ";
        logger.info("select sql=" + select);
        String sqlRow;
        sqlRow = pageRowsParam(select, params) + getOrderBy(SORTNAME, SORTORDER);
        logger.info("all select sql=" + sqlRow);
        return sqlRow;
    }

    /**
     * select count(*) from(SELECT ROWNUM as rownumber,T1.PART_NUM AS
     * teilNum,T2.PART_NAME_ZH AS teilNameZh,T2.PART_NAME_DE AS teilNameDe FROM
     * TCM_TT_PART T1, TCM_TT_PART_NAME T2 WHERE T1.TCM_TT_PART_NAME_ID =
     * T2.TCM_TT_PART_NAME_ID)
     *
     * @param sql sql
     * @return String String
     */
    public static String getSearchTotal(String sql) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        String sqlTotal;
        sqlTotal = "select count(*) as total from(" + sql + ") ";

        return sqlTotal;
    }

    /**
     * 统计
     * @param jdbcTemplate JdbcTemplate
     * @param sql String
     * @return int int
     */
    public static int getCount(JdbcTemplate jdbcTemplate, String sql) {
        final Object[] PARAMS = new Object[0];
        final List<Integer> RESLIST;
        RESLIST = new ArrayList<Integer>();
        jdbcTemplate.query(sql, PARAMS, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                RESLIST.add(rs.getInt("count"));
            };
        });
        int count;
        count = RESLIST.get(0);
        return count;
    }

    /**
     * 批量更新
     * @param jdbcTemplate jdbcTemplate
     * @param sql sql
     * @param params params
     */
    public static void updateBatch(JdbcTemplate jdbcTemplate, String sql, final List<Object[]> params) {
        final BatchPreparedStatementSetter PSS = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object[] key;
                key = params.get(i);
                for (int j = 0; j < key.length; j++) {
                    if (key[j] instanceof String) {
                        ps.setString(j + 1, key[j].toString());
                    } else if (key[j] instanceof Long) {
                        ps.setLong(j + 1, Long.valueOf(key[j].toString()));
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return params.size();
            }
        };
        jdbcTemplate.batchUpdate(sql, PSS);
    }

    public static String calcTotalCountSql(String originalSql) {
        String result = "";
        if(originalSql!=null && originalSql.startsWith("SELECT T.*")){
            result = originalSql.replace("SELECT T.*", "SELECT COUNT(1) AS TOTALCOUNT");
        }else{
            result = "SELECT COUNT(1) AS TOTALCOUNT FROM (" + originalSql + ")";
        }
        return result;
    }

    public static String addPageSplitInWithSql(String originalSql, String orderBy, int start, int end) {
        int pos;
        pos = originalSql.toUpperCase().indexOf("FROM");
        if (pos == -1) {
            throw new RuntimeException("Unrecoginzed SQL");
        }
        String strS1;
        strS1 = originalSql.substring(0, pos).trim();
        String strS2;
        strS2 = originalSql.substring(pos);

        strS1 += ", row_number() over (order by " + orderBy + ") as rownumber ";
        String result = "SELECT * FROM (" + strS1 + strS2 + ") where ";

        result += "rownumber > " + start + " and rownumber <= " + end;
        return result;
    }

    public static String addPageSplitInWithSqlByFastMethod(String originalSql, String orderBy, int start, int end,String startToken) {
        int pos;
        pos = originalSql.toUpperCase().indexOf("FROM");
        if (pos == -1) {
            throw new RuntimeException("Unrecoginzed SQL");
        }
        String strS1;
        strS1 = originalSql.substring(0, pos).trim();
        String strS2;
        strS2 = originalSql.substring(pos);

        strS1 += ", rownum as rown ";
        String result = strS1 + strS2 + startToken + " rownum <= " + end;
        if(start>0){
            result = "SELECT * FROM (" + result+ ") where rown>"+start;
        }
        return result;
    }
}
