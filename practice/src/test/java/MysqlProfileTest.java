import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @ClassName MysqlProfileTest
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/4/2 20:14
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-profile.xml"})
@ActiveProfiles("mysql")
public class MysqlProfileTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void mysqlTest() {
        String sql = "select * from test where id = 1";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        System.out.println(map);
    }

}
