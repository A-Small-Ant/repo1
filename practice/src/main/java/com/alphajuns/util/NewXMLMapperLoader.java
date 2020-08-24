package com.alphajuns.util;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName XMLMapperLoader
 * @Description 自动刷新mybait mapper.xml配置文件
 * @Author AlphaJunS
 * @Date 2020/4/4 21:51
 * @Version 1.0
 */
public class NewXMLMapperLoader implements DisposableBean, InitializingBean, ApplicationContextAware {

    private static Logger logger = Logger.getLogger(NewXMLMapperLoader.class);
    private SqlSessionFactory sqlSession;
    
    /**
     * @Author AlphaJunS
     * @Date 19:13 2020/4/5
     * @Description 一个mapper.xml
     * @param null
     * @return 
     */
    private String filePath = "D:/Develop/idea/practice/target/classes/com/alphajuns/dao/EmpMapper.xml";

    private HashMap<String, Long> fileMapping = new HashMap<String, Long>();

    private ScheduledExecutorService scheduledThreadPool;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSqlSession(SqlSessionFactory sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // this.context = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        try {
            // 获取定时执行任务对象
            ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
            // 调用定时执行任务方法
            scheduledThreadPool.scheduleAtFixedRate(new Task(new Scanner()), 5, 15, TimeUnit.SECONDS);
        } catch (Exception e1) {
            logger.error("mapper文件修改刷新启动异常 : {}", e1);
        }
    }

    class Task implements Runnable {

        private Scanner scanner;

        public Task(Scanner scanner) {
            this.scanner = scanner;
        }

        @Override
        public void run() {
            try {
                List<Resource> resources = scanner.getChangedFiles();
                if (resources.size() > 0) {
                    logger.debug("**********Mapper.xml文件改变,重新加载**************");
                    scanner.reloadXML(resources);
                    logger.debug("*****************加载完毕***********************");
                }
            } catch (Exception e) {
                logger.error("mapper文件修改重新加载失败: {}", e);
            }
        }

    }

    class Scanner {

        public Scanner() {
            try {
                scan();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 获取mapper.xml 资源
         *
         * @param basePackage
         * @return
         * @throws IOException
         */
        public Resource[] getResource(String basePackage) throws IOException {

            return new PathMatchingResourcePatternResolver().getResources(basePackage);
        }

        /**
         * 重新加载xml文件
         *
         * @param resources
         * @throws Exception
         */
        public void reloadXML(List<Resource> resources) throws Exception {

            Configuration configuration = sqlSession.getConfiguration();
            removeConfig(configuration);
            for (Resource resource : resources) {
                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration,
                            resource.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + resource + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }
            }
        }

        /**
         * 清除配置
         *
         * @param configuration
         * @throws Exception
         */
        private void removeConfig(Configuration configuration) throws Exception {

            clearValues(configuration, "mappedStatements");
            clearValues(configuration, "caches");
            clearValues(configuration, "resultMaps");
            clearValues(configuration, "parameterMaps");
            clearValues(configuration, "keyGenerators");
            clearValues(configuration, "sqlFragments");
            // 清理已加载的资源标识，方便让它重新加载。
            Field loadedResourcesField = configuration.getClass().getDeclaredField("loadedResources");
            loadedResourcesField.setAccessible(true);
            ((Set<?>) loadedResourcesField.get(configuration)).clear();
        }

        private void clearValues(Configuration configuration, String fieldName) throws Exception {

            Field field = configuration.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Map<?, ?> map = (Map<?, ?>) field.get(configuration);
            StrictMap<Object> newMap = new StrictMap<Object>();
            for (Object key : map.keySet()) {
                try {
                    newMap.put((String) key, map.get(key));
                } catch (IllegalArgumentException ex) {
                    newMap.put((String) key, ex.getMessage());
                }
            }
            field.set(configuration, newMap);
        }

        public void scan() throws IOException {

            if (!fileMapping.isEmpty()) {
                return;
            }
            File file = new File(filePath);
            String fileName = file.getName();
            long l = file.lastModified();
            fileMapping.put(fileName, l);
        }

        private String getValue(Resource resource) throws IOException {

            String contentLength = String.valueOf(resource.contentLength());
            String lastModified = String.valueOf(resource.lastModified());
            return new StringBuilder(contentLength).append(lastModified).toString();
        }

        public List<Resource> getChangedFiles() throws IOException {

            List<Resource> list = new ArrayList<Resource>();

            File file = new File(filePath);
            String fileName = file.getName();
            long l = file.lastModified();

            Long last = fileMapping.get(fileName);

            if (!(l == last)) {
                Resource resource = (Resource) new FileInputStream(file);
                list.add(resource);
                fileMapping.put(fileName, last);
                logger.debug(fileName + "文件修改************************");
            }
            
            logger.debug("mybatis mapper.xml 文件刷新结束************************");
            return list;
        }
    }

    @Override
    public void destroy() throws Exception {

        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdownNow();
        }
    }

    /**
     * 重写 org.apache.ibatis.session.Configuration.StrictMap 类 来自 MyBatis3.4.0版本，修改
     * put 方法，允许反复 put更新。
     */
    public static class StrictMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -4950446264854982944L;

        @SuppressWarnings("unchecked")
        public V put(String key, V value) {
            remove(key);
            if (key.contains(".")) {
                final String shortKey = getShortName(key);
                if (super.get(shortKey) == null) {
                    super.put(shortKey, value);
                } else {
                    super.put(shortKey, (V) new Ambiguity(shortKey));
                }
            }
            return super.put(key, value);
        }

        private String getShortName(String key) {
            final String[] keyparts = key.split("\\.");
            return keyparts[keyparts.length - 1];
        }

        protected static class Ambiguity {

            private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }
    }
}
