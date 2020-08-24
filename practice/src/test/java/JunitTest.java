import com.alphajuns.pojo.Employee;
import com.alphajuns.service.EmployeeService;
import com.alphajuns.util.XMLMapperLoader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JunitTest
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/25 20:44
 * @Version 1.0
 */
public class JunitTest extends JunitSuperTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testFindAllEmp() {
        List<Employee> allEmp = employeeService.findAllEmpByAnnotation();
        System.out.println(allEmp);
    }

    @Test
    public void testJdbcTemplate() {
        String sql = "select * from emp";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        System.out.println(mapList);
    }

    @Test
    public void testPageHelper() {
        // 设置分页查询
        PageHelper.startPage(1, 3);
        List<Employee> empList = employeeService.getEmpByPage();
        for (Employee employee: empList) {
            System.out.println(employee);
        }
        // 此处输出的结果调用的PageHelper分页插件中Page类中重写的toString方法，其中Page类继承自ArrayList
        System.out.println(empList);
        PageInfo pageInfo = new PageInfo(empList);
        System.out.println(pageInfo);
    }

    @Test
    public void testGetUrl() {
        URL url = XMLMapperLoader.class.getClassLoader().getResource("./com/alphajuns/dao/EmpMapper.xml");
        System.out.println("url:" + url);
    }

    @Test
    public void getUrl() {
        URL url = XMLMapperLoader.class.getClassLoader().getResource("./com/alphajuns/dao/EmpMapper.class");
        System.out.println("url:" + url);
    }

}
