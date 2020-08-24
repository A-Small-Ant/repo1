import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName JunitSuperTest
 * @Description TODO
 * @Author AlphaJunS
 * @Date 2020/3/25 20:39
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class JunitSuperTest {

    @Before
    public void setUp() {
        System.out.println("======进入Junit方法======");
    }

}
