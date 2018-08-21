package com.casesoft.dmc.core.dao;

public interface RedisDao {
    /**
     * 设置频道
     */
    public static final String TOPIC_CHANNEL="topic.channel";

    public void sendMessage(String channel, String message);

}
