package com.hqk.recruitment.common.utils;

public class RedisKeyUtils {
    /**
     保存用户点赞内容数据的key
     */
    public static final String USER_LIKED_COMMENT = "USER_LIKED_COMMENT::";

    public static final String USER_LIKED_ARTICLE="USER_LIKED_ARTICLE::";
    /**
     保存内容被点踩数量的key
     */
    public static final String USER_DISLIKED_COMMENT = "USER_DISLIKED_COMMENT::";
    public static final String USER_DISLIKED_ARTICLE="USER_DISLIKED_ARTICLE::";
}