package com.learn.springsecurity.demo.wiremock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * autor:liman
 * createtime:2021/7/2
 * comment:
 */
public class MockServer {

    public static void main(String[] args) throws IOException {
//        configureFor(9099);
//        removeAllMappings();
//
//        stubFor(get(urlPathEqualTo("/order/1"))
//                .willReturn(aResponse()
//                        .withBody("{\"order\":\"1\"}")
//                        .withStatus(200)));

        configureFor(9099);
        removeAllMappings();

        mock("/order/1", "01");
        mock("/order/2", "02");
    }

    private static void mock(String url, String file) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/" + file + ".txt");
        String content = StringUtils.join(FileUtils.readLines(resource.getFile(), "UTF-8").toArray(), "\n");
        stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));
    }

}
