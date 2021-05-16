


// import com.any.BiaoBaiApp;
// import com.any.pojo.Miss;
// import com.any.send.SendMessage;
// import com.fasterxml.jackson.core.JsonParser;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
// import net.minidev.json.writer.BeansMapper;
// import org.apache.http.HttpEntity;
// import org.apache.http.HttpResponse;
// import org.apache.http.client.HttpClient;
// import org.apache.http.client.methods.HttpGet;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.util.EntityUtils;
// import org.json.JSONException;
// import org.json.JSONObject;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.json.JacksonJsonParser;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;


// import java.io.IOException;
// import java.sql.Time;
// import java.util.Date;


/**
 * @describe 测试代码可用性
 * @author: AnyWhere
 * @date 2020/12/15 12:21
 */

// @SpringBootTest(classes = {BiaoBaiApp.class})
// @RunWith(SpringRunner.class)
// public class TestSendMessage {

//     @Autowired
//     private SendMessage sendMessage;

//     @Test
//     public void Test() {
//         String oneS = sendMessage.getOneS();
//         System.out.println(oneS);
//         System.out.println(new Date().toLocaleString() + " 发送消息：" + oneS);
//         sendMessage.sendMessage("来自Sometimes的消息！❤", oneS);
//     }



//     @Test
//     public void test2() {
//         try {
//             //创建客户端对象
//             HttpClient client = HttpClients.createDefault();
//             //创建地址    https://chp.shadiao.app/api.php
//             HttpGet get = new HttpGet("http://open.iciba.com/dsapi/");
//             //发起请求，接收响应对象
//             HttpResponse response = client.execute(get);
//             //获取响应体，响应数据是一种基于HTTP协议标准字符串的对象
//             //响应体和响应头，都是封装HTTP协议数据。直接使用可能出现乱码或解析错误
//             HttpEntity entity = response.getEntity();
//             //通过HTTP实体工具类，转换响应体数据
//             String string = EntityUtils.toString(entity, "utf-8");
//             ObjectMapper mapper = new ObjectMapper();
//             // json字符串转换为java对象
//             Miss miss = mapper.readValue(string, Miss.class);
//             System.out.println(miss.getContent() + "\n\n" + miss.getNote());
//             System.out.println(miss.toString());
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

