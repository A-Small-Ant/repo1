package com.alphajuns.dao;

import com.alphajuns.pojo.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface IEmpDao {

    @Select("select * from emp")
    List<Employee> findAllEmpByAnnotation();

    @Select("SELECT job, count(*) AS NUM FROM EMP group by job")
    List<Map<String, ?>> queryPersonNumGroupByJob();

    @Select("SELECT\n" +
            "\tcount( a1 ) AS A1,\n" +
            "\tcount( a2 ) AS A2,\n" +
            "\tcount( a3 ) AS A3 \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\t( CASE WHEN sal <= 2000 AND sal >= 800 THEN SAL END ) AS a1,\n" +
            "\t\t( CASE WHEN sal <= 4000 AND sal > 2000 THEN SAL END ) AS a2,\n" +
            "\t\t( CASE WHEN sal <= 6000 AND sal > 4000 THEN SAL END ) AS a3 \n" +
            "\tFROM\n" +
            "\temp \n" +
            "\t) t")
    Map<String, ?> getPieChart();

    @Select("select * from emp where sal >= #{min} and sal <= #{max}")
    List<Employee> getPieDetail(@Param("min") int min, @Param("max") int max);

    @Select("select * from emp")
    List<Employee> getEmpByPage();

}
