package com.alphajuns.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName MyBatisBatchHelper
 * @Description MyBatis批处理帮助类
 * @Author AlphaJunS
 * @Date 2020/4/1 19:42
 * @Version 1.0
 */
public class MyBatisBatchHelper {

    private static Logger logger = Logger.getLogger(MyBatisBatchHelper.class);

    /**
     * open session
     * @param sqlSessionFactoryBiz
     * @return
     */
    static public SqlSession openSession(SqlSessionFactory sqlSessionFactoryBiz){
        SqlSession sqlSession = null;
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        String targetEnv = wac.getServletContext().getInitParameter("spring.profiles.default");
        if("dev".equals(targetEnv)){
            sqlSession = sqlSessionFactoryBiz.openSession(ExecutorType.BATCH, false);
        }else{
            sqlSession = sqlSessionFactoryBiz.openSession(ExecutorType.BATCH, true);
        }
        return sqlSession;
    }

    /**
     * commit
     * @param sqlSession
     */
    public static void  commit(SqlSession sqlSession){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        String targetEnv = wac.getServletContext().getInitParameter("spring.profiles.default");
        try{
            if("dev".equals(targetEnv)){
                sqlSession.commit();
                sqlSession.clearCache();
            }else{
                sqlSession.commit();
            }
        }catch(Exception e){
            if(e.getMessage().contains("自动提交")){
                logger.warn(e.getMessage().substring(0,20));
            }else{
                logger.error("commits error",e);
            }
        }
    }

    /**
     * rollback
     * @param sqlSession
     */
    public static void rollback(SqlSession sqlSession){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        String targetEnv = wac.getServletContext().getInitParameter("spring.profiles.default");
        try{
            if("dev".equals(targetEnv)){
                sqlSession.rollback();
            }
        }catch(Exception e){
            if(e.getMessage().contains("自动提交")){
                logger.warn(e.getMessage().substring(0,20));
            }else{
                logger.error("rollback error",e);
            }
        }
    }

    /**
     * close
     * @param sqlSession
     */
    public static void close(SqlSession sqlSession){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        String targetEnv = wac.getServletContext().getInitParameter("spring.profiles.default");
        try{
            if("dev".equals(targetEnv)){
                sqlSession.close();
            }else{
                sqlSession.close();
            }
        }catch(Exception e){
            if(e.getMessage().contains("自动提交")){
                logger.warn(e.getMessage().substring(0,20));
            }else{
                logger.error("close error",e);
            }
        }
    }

}
