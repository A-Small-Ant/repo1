package com.alphajuns.service.impl;

import com.alphajuns.dao.EmpMapper;
import com.alphajuns.dao.IEmpDao;
import com.alphajuns.pojo.Employee;
import com.alphajuns.service.EmployeeService;
import com.alphajuns.util.MyBatisBatchHelper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EmployeeServiceImpl
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/25 19:51
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private IEmpDao empDao;

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Employee> findAllEmpByAnnotation() {
        List<Employee> allEmp = empDao.findAllEmpByAnnotation();
        return allEmp;
    }

    @Override
    public List<Map<String, ?>> queryPersonNumGroupByJob() {
        List<Map<String, ?>> mapList = empDao.queryPersonNumGroupByJob();
        return mapList;
    }

    @Override
    public Map<String, ?> getPieChart() {
        Map<String, ?> pieChartMap = empDao.getPieChart();
        return pieChartMap;
    }

    @Override
    public List<Employee> getPieDetail(String param) {// "800~2000"
        String[] split = param.split("~");
        // 800
        int min = Integer.parseInt(split[0]);
        // 2000
        int max = Integer.parseInt(split[1]);
        List<Employee> pieDetailList = empDao.getPieDetail(min, max);
        return pieDetailList;
    }

    @Override
    public List<Map<String, ?>> findEmpByMap(Map<String, ?> paramMap) {
        List<Map<String, ?>> empMapList = empMapper.findEmpByMap(paramMap);
        return empMapList;
    }

    @Override
    public List<Map<String, ?>> findEmpByList(List<Integer> paramList) {
        List<Map<String, ?>> empMapList = empMapper.findEmpByList(paramList);
        return empMapList;
    }

    @Override
    public void updateEmpByMapList(List<Map<String, Integer>> paramMapList) {
        empMapper.updateEmpByMapList(paramMapList);
    }

    @Override
    public List<Map<String, ?>> findEmpByListMap(Map<String, List<Integer>> paramListMap) {
        List<Map<String, ?>> empMapList = empMapper.findEmpByListMap(paramListMap);
        return empMapList;
    }

    @Override
    public List<Employee> getEmpByPage() {
        List<Employee> empList = empDao.getEmpByPage();
        return empList;
    }

    @Override
    public void mybatisBatch() {
        // 开启会话
        SqlSession sqlSession = MyBatisBatchHelper.openSession(sqlSessionFactory);
        // 获取接口代理对象
        EmpMapper batchEmpMapper = sqlSession.getMapper(EmpMapper.class);
        // 批量操作数据库数据
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("DEPTNO", "50");
            paramMap.put("DNAME", "DOCTOR");
            paramMap.put("LOC", "BEIJING");
            // 插入Scott下DEPT表
            batchEmpMapper.insertDept(paramMap);

            // 无异常时，事务提交，数据被更新到数据库
            // 用于模拟异常，捕获异常时，回滚事务，注释打开时，可测试异常，发现前面操作数据库的数据没有被更新到数据库中
            // int i = 1/0;

            Map<String, Integer> map = new HashMap<>();
            map.put("GRADE", 6);
            map.put("LOSAL", 4001);
            map.put("HISAL", 10000);
            // 更新Scott下SALGRADE
            batchEmpMapper.insertSalGrade(map);

            // 提交事务
            MyBatisBatchHelper.commit(sqlSession);
            System.out.println("事务提交了，去数据库看一看");
        } catch (Exception e) {
            e.printStackTrace();
            // 回滚事务
            MyBatisBatchHelper.rollback(sqlSession);
        } finally {
            // 关闭会话
            MyBatisBatchHelper.close(sqlSession);
        }
    }

    @Override
    public Employee findEmpByEpmno() {
        Employee employee = empMapper.findEmpByEpmno();
        return employee;
    }

    @Override
    public Employee invoke(String methodName, String param) {
        Employee employee = new Employee();
        // 利用反射获取mapper接口中指定方法，执行
        // 获取字节码文件对象
        Class<? extends EmpMapper> clazz = empMapper.getClass();
        try {
            // 获取方法
            Method method = clazz.getDeclaredMethod(methodName, String.class);
            // 执行
            employee = (Employee) method.invoke(empMapper, param);
            // 对某些方法做额外处理
            if ("findEmpByEname".equals(methodName)) {
                System.out.println("调用了通过名字查询数据方法");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }


}
