package test.controller;

import com.learn.springsecurity.demo.SpringSecurityDemoApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * autor:liman
 * createtime:2021/6/29
 * comment: 模拟mvc的controller测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringSecurityDemoApplication.class)
@Slf4j
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 简单的模拟mvc的测试
     * @throws Exception
     */
    @Test
    public void whenQuerySuccess() throws Exception {
        String responseData = mockMvc.perform(get("/user")
                .param("username", "coderMan")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn().getResponse().getContentAsString();

        log.info("返回的数据为:{}",responseData);
    }

    @Test
    public void whenGetUserInfoSuccess() throws Exception {
        String resultResponse = mockMvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();
        log.info("返回的数据为:{}",resultResponse);
    }

    @Test
    public void testGetInfoFail() throws Exception {
        mockMvc.perform(get("/user/a")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    /**
     * 创建用户的MVC测试
     */
    @Test
    public void whenCreateSuccess() throws Exception {
        Date date = new Date();
        log.info("前端上送的时间:{}",date.getTime());
        String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":"+date.getTime()+"}";
        String responseStr = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();

        log.info("服务端返回结果为:{}",responseStr);
    }

    /**
     * 更新用户的MVC测试
     */
    @Test
    public void whenUpdateSuccess() throws Exception {
        Date date = new Date();
        log.info("前端上送的时间:{}",date.getTime());
        String content = "{\"username\":\"tom\",\"password\":null,\"birthday\":"+date.getTime()+"}";
        String responseStr = mockMvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();

        log.info("服务端返回结果为:{}",responseStr);
    }

    /**
     * 删除用户的MVC测试
     */
    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * 文件上传测试
     * @throws Exception
     */
    @Test
    public void whenUploadSuccess() throws Exception {
        String result = mockMvc.perform(fileUpload("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hello upload".getBytes("UTF-8"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info("返回结果:{}",result);

    }
}
