import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.rowset.JdbcRowSet;
import java.util.Map;

/**
 * @ClassName ProfileTest
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/4/2 20:06
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-profile.xml"})
@ActiveProfiles("dev")
public class OracleProfileTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void oracleTest() {
        String sql = "select * from emp where empno = 7900";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        System.out.println(map);
    }

}
