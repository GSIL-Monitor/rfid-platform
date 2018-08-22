package com.casesoft.dmc.core.service;

import com.casesoft.dmc.core.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RedisDaoService implements RedisDao {
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    /**
     * 程序中调用此方法向指定频道内发布消息
     * @param channel
     * @param message
     */
    @Override
    public void sendMessage(String channel, String message) {
        System.out.println("---- 开始发布消息 ----");

        // topic.channel为配置文件中注册的
        redisTemplate.convertAndSend(channel, message);

        System.out.println("---- 发布成功！----");
    }
}
