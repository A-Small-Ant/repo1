import com.alphajuns.pojo.Employee;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName JavaTest
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/4/2 21:49
 * @Version 1.0
 */
public class JavaTest {

    @Test
    public void printList() {
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("name", "lisi");
        mapList.add(map);
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "zhangsan");
        mapList.add(map1);
        System.out.println(mapList);
    }

    @Test
    public void printEmployeeList() {
        List<Employee> empList = new ArrayList<>();
        Employee employee = new Employee();
        employee.setEmpno(1);
        employee.setEname("AlphaJunS");
        Employee employee1 = new Employee();
        employee.setEmpno(2);
        employee.setEname("MaFei");
        empList.add(employee);
        empList.add(employee1);
        System.out.println(empList);
    }

    @Test
    public void getLastModifyTime() {
        File file = new File("D:\\Develop\\idea\\practice\\src\\main\\java\\com\\alphajuns\\dao\\EmpMapper.xml");
        long l = file.lastModified();
        Date date = new Date(l);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);
        System.out.println(formatDate);
    }

}
