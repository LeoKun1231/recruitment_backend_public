package com.hqk.recruitment.common.service.impl;

import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.common.utils.RedisKeyUtils;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.common.Sort;
import com.hqk.recruitment.model.common.UserLike;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LikeOrDislikeServiceImpl implements LikeOrDislikeService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @Override
    public R doLike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle) {
        this.checkInfo(likeOrDisLikeVo,isArticle);
        Long add=null;
        if(isArticle){
            add = redisTemplate.opsForSet().add(RedisKeyUtils.USER_LIKED_ARTICLE + likeOrDisLikeVo.getArticleId(), likeOrDisLikeVo.getUserId());
        }else{
            add = redisTemplate.opsForSet().add(RedisKeyUtils.USER_LIKED_COMMENT + likeOrDisLikeVo.getCommentId(), likeOrDisLikeVo.getUserId());
        }
        if(add==1){
            return R.ok().message("点赞成功");
        }else{
            return R.error().message("您已重复点赞");
        }
    }

    /**
     * 取消点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @Override
    public R cancelLike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle) {
        this.checkInfo(likeOrDisLikeVo,isArticle);
        Long remove=null;
        if(isArticle){
            remove = redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_ARTICLE + likeOrDisLikeVo.getArticleId(), likeOrDisLikeVo.getUserId());
        }else{
            remove = redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_COMMENT + likeOrDisLikeVo.getCommentId(), likeOrDisLikeVo.getUserId());
        }
        if(remove==1){
            return  R.ok().message("取消点赞成功");
        }else{
            return R.error().message("您已经重复取消点赞");
        }
    }


    /**
     * 获取点赞总数以及是否点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @Override
    public Map<String,Object> getLikeCountAndIslike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle) {
        this.checkInfo(likeOrDisLikeVo,isArticle);
        String key=null;
        if(isArticle){
            key=RedisKeyUtils.USER_LIKED_ARTICLE + likeOrDisLikeVo.getArticleId();
        }else{
            key=RedisKeyUtils.USER_LIKED_COMMENT + likeOrDisLikeVo.getCommentId();
        }
        Long size = redisTemplate.opsForSet().size(key);
        Boolean member = redisTemplate.opsForSet().isMember(key, likeOrDisLikeVo.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("count",size);
        map.put("isLike",member);
        return map;
    }

    /**
     * 获取点赞详情 有哪些人点赞
     * @param
     * @return
     */
    @Override
    public Sort getLikeDetails(Long articleId) {
        if(Objects.isNull(articleId)){
                throw  new MyCustomException(20000,"articleId不能为空");
        }
        Set members = null;
        members= redisTemplate.opsForSet().members(RedisKeyUtils.USER_LIKED_ARTICLE + articleId);
       return new Sort(articleId, (long) members.size());
    }

    @Override
    public List<Long> getTopLikeList(List<Long> ids) {
        List<Sort> sorts = new ArrayList<>();
        for (Long id : ids) {
            Sort details = getLikeDetails(id);
            sorts.add(details);
        }
        Stream<Sort> sorted = sorts.stream().sorted(new Comparator<Sort>() {
            @Override
            public int compare(Sort o1, Sort o2) {
                return (int) (o2.getCount() - o1.getCount());
            }
        });
        List<Long> collect = sorted.map(item -> item.getId()).collect(Collectors.toList());
        System.out.println("collect = " + collect);
        return collect;
    }


    private void checkInfo(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle){
        if(isArticle && Objects.isNull(likeOrDisLikeVo.getArticleId())){
            System.out.println("likeOrDisLikeVo = " + likeOrDisLikeVo);
           throw  new MyCustomException(20000,"articleId不能为空");
        }
        if(!isArticle &&Objects.isNull(likeOrDisLikeVo.getCommentId()) ){
            throw  new MyCustomException(20000,"commentId不能为空");
        }
        if(Objects.isNull(likeOrDisLikeVo.getUserId())){
            throw  new MyCustomException(20000,"userId不能为空");
        }
    }
}
