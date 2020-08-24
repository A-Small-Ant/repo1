package com.alphajuns.service;

import com.alphajuns.pojo.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<Employee> findAllEmpByAnnotation();

    List<Map<String, ?>> queryPersonNumGroupByJob();

    Map<String, ?> getPieChart();

    List<Employee> getPieDetail(String param);

    List<Map<String, ?>> findEmpByMap(Map<String, ?> paramMap);

    List<Map<String, ?>> findEmpByList(List<Integer> paramList);

    void updateEmpByMapList(List<Map<String, Integer>> paramMapList);

    List<Map<String, ?>> findEmpByListMap(Map<String, List<Integer>> paramListMap);

    List<Employee> getEmpByPage();

    void mybatisBatch();

    Employee findEmpByEpmno();

    Employee invoke(String methodName, String param);
}
