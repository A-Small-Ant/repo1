import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName HttpClientTest
 * @Description HttpClient测试
 * @Author AlphaJunS
 * @Date 2020/3/28 20:17
 * @Version 1.0
 */
public class HttpClientTest {

    @Test
    public void test() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // ip地址查询网站 https://www.ip.cn/
        HttpGet httpGet = new HttpGet("https://www.baidu.com/");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        // 获取响应行
        StatusLine statusLine = httpResponse.getStatusLine();
        System.out.println("响应行:" + statusLine);
        // 获取响应体
        HttpEntity entity = httpResponse.getEntity();
        System.out.println("响应体:" + entity);
        EntityUtils.consume(entity);
        httpResponse.close();
    }

}
