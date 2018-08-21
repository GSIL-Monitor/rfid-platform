package com.casesoft.dmc.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis订阅类
 */
public class RedisMessageListener implements MessageListener {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
        Object channel = serializer.deserialize(message.getChannel());
        Object body = serializer.deserialize(message.getBody());

        System.out.println("接收［" + channel + "］频道成功");


        // 循环处理队列,key仅为测试
        while (redisTemplate.opsForList().size("stylehh17101") > 0) {
            String current = redisTemplate.opsForList().rightPop("stylehh17101");
            // 返回队头元素进行单据处理
            if (false) {
                // 如果失败，则将当前对象放到队尾中，直到执行完毕
                redisTemplate.opsForList().leftPush("stylehh17101", current);
            }
        }
    }


}
