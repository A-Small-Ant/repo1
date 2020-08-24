import com.alphajuns.service.EmployeeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MapperTest
 * @Description mapper接口测试
 * @Author AlphaJunS
 * @Date 2020/3/30 20:47
 * @Version 1.0
 */
public class MapperTest extends JunitSuperTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testFindEmpByMap() {
        Map<String, String> pramaMap = new HashMap<>();
        pramaMap.put("JOB", "CLERK");
        List<Map<String, ?>> empMapList = employeeService.findEmpByMap(pramaMap);
        System.out.println("empMapList:" + empMapList);
    }

    @Test
    public void testFindEmpByList() {
        List<Integer> empnoList = new ArrayList<>();
        empnoList.add(7788);
        empnoList.add(7900);
        List<Map<String, ?>> empMapList = employeeService.findEmpByList(empnoList);
        System.out.println("empMapList:" + empMapList);
    }

    @Test
    public void testUpdateEmpByMapList() {
        Map<String, Integer> pramaMap = new HashMap<>();
        pramaMap.put("EMPNO", 7902);
        pramaMap.put("SAL", 4000);
        Map<String, Integer> map = new HashMap<>();
        map.put("EMPNO", 7934);
        map.put("SAL", 2300);
        List<Map<String, Integer>> paramMapList = new ArrayList<>();
        paramMapList.add(pramaMap);
        paramMapList.add(map);
        employeeService.updateEmpByMapList(paramMapList);
    }

    @Test
    public void testFindEmpByListMap() {
        Map<String, List<Integer>> pramaListMap = new HashMap<>();
        List<Integer> empnoList = new ArrayList<>();
        empnoList.add(7369);
        empnoList.add(7499);
        pramaListMap.put("empnoList", empnoList);
        List<Map<String, ?>> empMapList = employeeService.findEmpByListMap(pramaListMap);
        System.out.println("empMapList:" + empMapList);
    }

}
