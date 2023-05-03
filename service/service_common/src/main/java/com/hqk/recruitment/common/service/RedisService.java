package com.hqk.recruitment.common.service;

import com.hqk.recruitment.model.common.CommentLikeCount;
import com.hqk.recruitment.model.common.UserLike;

import java.util.List;

public interface RedisService {

    /**
     * 获取点赞状态
     * @param commentId
     * @param userId
     */
    Integer getLikeStatus(Long commentId, Long userId);

    /**
     * 点赞。状态为1
     * @param commentId
     * @param userId
     */
    void saveLiked2Redis(Long commentId, Long userId);

    /**
     * 取消点赞。将状态改变为0
     * @param commentId
     * @param userId
     */
    void unlikeFromRedis(Long commentId, Long userId);

    /**
     * 从Redis中删除一条点赞数据
     * @param commentId
     * @param userId
     */
    void deleteLikedFromRedis(Long commentId, Long userId);

    /**
     * 该内容的点赞数变化Δdelta
     * @param commentId
     */
    void in_decrementLikedCount(Long commentId, Long delta);

    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<UserLike> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<CommentLikeCount> getLikedCountFromRedis();
}
