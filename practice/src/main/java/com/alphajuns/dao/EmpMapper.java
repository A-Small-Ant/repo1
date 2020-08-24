package com.alphajuns.dao;

import com.alphajuns.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmpMapper {

    List<Map<String, ?>> findEmpByMap(@Param("paramMap") Map<String,?> paramMap);

    List<Map<String, ?>> findEmpByList(@Param("paramList") List<Integer> paramList);

    void updateEmpByMapList(@Param("paramMapList") List<Map<String, Integer>> paramMapList);

    List<Map<String, ?>> findEmpByListMap(@Param("paramListMap") Map<String, List<Integer>> paramListMap);

    void insertDept(@Param("paramMap") Map<String, String> paramMap);

    void insertSalGrade(@Param("paramMap") Map<String, Integer> map);

    Employee findEmpByEpmno();

    Employee findEmpById(String empno);

    Employee findEmpByEname(String ename);
}
