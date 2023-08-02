package com.learn.springsecurity.core.social;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * autor:liman
 * createtime:2021/8/7
 * comment:app端用户信息存入Redis的工具类
 */
@Component
public class AppSignUpUtils {

    public static final String SOCIAL_REDIS_USER_PREFIX = "self:security:social:connectionData";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private UsersConnectionRepository usersConnectionRepository;
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    public void saveConnectionData(WebRequest webRequest, ConnectionData connectionData) {
        redisTemplate.opsForValue().set(getKey(webRequest), connectionData, 10, TimeUnit.MINUTES);
    }

    /**
     * 将用户与数据库中的信息进行绑定
     * @param request
     * @param userId
     */
    public void doPostSignUp(WebRequest request,String userId){
        String key = getKey(request);
        if(!redisTemplate.hasKey(key)){
            throw new RuntimeException("无法找到缓存的用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);

        //根据ConnectionData实例化创建一个Connection
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
                .createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
    }

    /**
     * 获取设备id作为key
     *
     * @param webRequest
     * @return
     */
    public String getKey(WebRequest webRequest) {
        String deviceId = webRequest.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new RuntimeException("设备id不能为空");
        }
        return SOCIAL_REDIS_USER_PREFIX + deviceId;
    }

}
