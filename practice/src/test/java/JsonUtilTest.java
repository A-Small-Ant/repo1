import com.alphajuns.util.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JsonUtilTest
 * @Description JsonUtil工具类测试
 * @Author AlphaJunS
 * @Date 2020/3/27 21:10
 * @Version 1.0
 */
public class JsonUtilTest{

    @Test
    public void testJsonToMap() throws Exception {
        String json = "{\"sex\":\"male\",\"name\":\"Steven\"}";
        Map<String, Object> map = JsonUtil.jsonToMap(json);
        // {sex=male, name=Steven}
        System.out.println(map);
    }

    @Test
    public void testJsonToMapList() throws Exception {
        String jsonMapList = "[{\"sex\":\"male\",\"name\":\"Steven\"},{\"sex\":\"female\",\"name\":\"Allen\"}]";
        List<Map<String, ?>> mapList = JsonUtil.jsonToMapList(jsonMapList);
        // [{sex=male, name=Steven}, {sex=female, name=Allen}]
        System.out.println(mapList);
    }

    @Test
    public void testMapToJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Steven");
        map.put("sex", "male");
        String json = JsonUtil.mapToJson(map);
        // {"sex":"male","name":"Steven"}
        System.out.println(json);
    }

    @Test
    public void testMapListToJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Steven");
        map.put("sex", "male");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "Allen");
        map1.put("sex", "female");
        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList.add(map);
        mapList.add(map1);
        String s = JsonUtil.mapListToJson(mapList);
        // [{"sex":"male","name":"Steven"},{"sex":"female","name":"Allen"}]
        System.out.println(s);
    }

    @Test
    public void testArrayToJson() throws Exception {
        String[] array = {"Hello", "5", "World", "6"};
        String json = JsonUtil.arrayToJson(array);
        // {"0":"Hello","1":"5","2":"World","3":"6"}
        System.out.println(json);
    }

    @Test
    public void testObjectToJson() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Steven");
        map.put("sex", "male");
        String s = JsonUtil.objectToJson(map);
        // {"sex":"male","name":"Steven"}
        System.out.println(s);
    }

}
