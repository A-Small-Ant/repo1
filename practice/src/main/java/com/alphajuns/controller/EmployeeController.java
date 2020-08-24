package com.alphajuns.controller;

import com.alphajuns.pojo.Employee;
import com.alphajuns.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EmployeeController
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/25 19:50
 * @Version 1.0
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @RequestMapping("/findAllEmp")
    public ModelAndView findAllEmp() {
        ModelAndView mv = new ModelAndView();
        List<Employee> allEmp = employeeService.findAllEmpByAnnotation();
        mv.addObject("allEmp", allEmp);
        System.out.println(allEmp);
        mv.setViewName("report");
        return mv;
    }

    @RequestMapping("/queryPersonNumGroupByJob")
    public ModelAndView queryPersonNumGroupByJob() {
        ModelAndView mv = new ModelAndView();
        List<Map<String, ?>> mapList = employeeService.queryPersonNumGroupByJob();
        mv.addObject("mapList", mapList);
        mv.setViewName("report");
        return mv;
    }

    @RequestMapping("/getPieChart")
    @ResponseBody
    public Map getPieChart() {
        Map pieChartMap = new HashMap();
        pieChartMap = employeeService.getPieChart();
        return pieChartMap;
    }

    @RequestMapping("/getPieDetail")
    @ResponseBody
    public List<Employee> getPieDetail(@RequestParam String param) {
        List<Employee> pieDetailList = employeeService.getPieDetail(param);
        return pieDetailList;
    }

    @RequestMapping("/mybatisBatch")
    @ResponseBody
    public String mybatisBatch() {
        employeeService.mybatisBatch();
        return "mybatisBatch";
    }

    @RequestMapping("/findEmpByEpmno")
    @ResponseBody
    public String findEmpByEpmno() {
        Employee employee = employeeService.findEmpByEpmno();
        System.out.println("查询结果：" + employee);
        return employee.toString();
    }

    @RequestMapping("/invoke/{methodName}/{param}")
    @ResponseBody
    public String invoke(@PathVariable("methodName") String methodName, @PathVariable("param") String param) {
        Employee employee = employeeService.invoke(methodName, param);
        System.out.println("查询结果：" + employee);
        return employee.toString();
    }

}
