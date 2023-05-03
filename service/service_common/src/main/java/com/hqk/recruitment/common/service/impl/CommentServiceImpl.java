package com.hqk.recruitment.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.common.service.ArticleService;
import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.common.utils.RedisKeyUtils;
import com.hqk.recruitment.model.common.Article;
import com.hqk.recruitment.model.common.Comment;
import com.hqk.recruitment.common.mapper.CommentMapper;
import com.hqk.recruitment.common.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.*;
import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    @Resource
    private UserClient userClient;


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ArticleService articleService;

    /**
     * 增加评论
     *
     * @param commentVo
     * @return
     */
    @Override
    public R addComment(CommentVo commentVo) {
        Comment comment = new Comment();
        //对应文章评论数加一
        Article article = new Article();
        articleService.update(article, new UpdateWrapper<Article>().eq("id",commentVo.getArticleId()).setSql("comment_count = comment_count + 1"));
        BeanUtils.copyProperties(commentVo, comment);
        boolean save = this.save(comment);
        if (save) {
            return R.ok().data("id", comment.getId()).message("添加评论成功");
        } else {
            return R.error().message("添加评论失败");
        }
    }

    @Override
    public R getCommentList(CommentQueryVo commentQueryVo) {
        Long pageSize = commentQueryVo.getPageSize();
        Long articleId = commentQueryVo.getArticleId();
        Long userId = commentQueryVo.getUserId();
        Long currentPage = commentQueryVo.getCurrentPage();

        Page<Comment> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>().eq("article_id", articleId).eq("root_id", 0);

        queryWrapper.orderByDesc("create_time");
        this.page(page, queryWrapper);
        List<Comment> records = page.getRecords();
        List<CommentLikeVo> commentListParent = new ArrayList<>();
        for (Comment record : records) {
            Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(record.getId(), userId, null), false);
            Long likeCount = (Long) likeCountAndIslike.get("count");
            boolean isLike = (boolean) likeCountAndIslike.get("isLike");
            //进行组织结构
            CommentLikeVo commentLikeVo = new CommentLikeVo();
            BeanUtils.copyProperties(record, commentLikeVo);
            commentLikeVo.setIsLike(isLike);
            commentLikeVo.setLikeCount(likeCount);

            /**
             * 获取子评论前两条
             */
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("root_id", record.getId()).eq("article_id", articleId).orderByAsc("create_time").last("limit 2");

            /**
             * 获取评论数量
             */
            QueryWrapper<Comment> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("root_id", record.getId()).eq("article_id", articleId).orderByAsc("create_time");
            int count = this.count(queryWrapper1);
            commentLikeVo.setCommentCount(count);

            List<Comment> comments = this.list(commentQueryWrapper);
            List<CommentLikeVo> commentLikeVoList = new ArrayList<>();
            for (Comment comment : comments) {
                CommentLikeVo commentLikeVo1 = new CommentLikeVo();
                Map<String, Object> likeCountAndIslike1 = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(comment.getId(), userId, null), false);
                UserVo userInfo = userClient.getUserInfo(comment.getUserId());
                Long likeCount1 = (Long) likeCountAndIslike1.get("count");
                boolean isLike1 = (boolean) likeCountAndIslike1.get("isLike");
                BeanUtils.copyProperties(comment, commentLikeVo1);
                if(!Objects.isNull(comment.getTargetId())){
                    UserVo targetInfo = userClient.getUserInfo(comment.getTargetId());
                    commentLikeVo1.setTarget(targetInfo.getNickName());
                }
                commentLikeVo1.setAvatar(userInfo.getAvatar());
                commentLikeVo1.setNickname(userInfo.getNickName());
                commentLikeVo1.setIsLike(isLike1);
                commentLikeVo1.setLikeCount(likeCount1);
                commentLikeVoList.add(commentLikeVo1);
            }
            UserVo userInfo = userClient.getUserInfo(record.getUserId());
            if(!Objects.isNull(record.getTargetId())){
                UserVo targetInfo = userClient.getUserInfo(record.getTargetId());
                commentLikeVo.setTarget(targetInfo.getNickName());
            }
            commentLikeVo.setAvatar(userInfo.getAvatar());
            commentLikeVo.setNickname(userInfo.getNickName());
            commentLikeVo.setChildren(commentLikeVoList);
            commentListParent.add(commentLikeVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", page.getTotal());
        map.put("list", commentListParent);
        return R.ok().message(null).data(map);
    }

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeComment(Long id) {
        //对应文章评论数减一
        Comment comment = this.getOne(new QueryWrapper<Comment>().eq("id ", id));
        Long articleId = comment.getArticleId();

        //如果是第一层评论 则要删除底下所有的评论
        if (comment.getRootId() == 0) {
            List<Comment> comments = this.list(new QueryWrapper<Comment>().eq("root_id", comment.getId()));
            //把顶层添加进去
            comments.add(comment);
            List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
            this.removeByIds(commentIds);
            // 删除所有点赞
            for (Comment comment1 : comments) {
                Set members = redisTemplate.opsForSet().members(RedisKeyUtils.USER_LIKED_COMMENT + comment1.getId());
                if (members.size() > 0) {
                    for (Object member : members) {
                        redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_COMMENT + comment1.getId(), member);
                    }
                }
            }
            return articleService.update(new UpdateWrapper<Article>().eq("id", comment.getArticleId()).setSql("comment_count = comment_count - " + comments.size()));
        }else{
            this.remove(new QueryWrapper<Comment>().eq("id", id));
            // 删除点赞
            Set members = redisTemplate.opsForSet().members(RedisKeyUtils.USER_LIKED_COMMENT + comment.getId());
            if (members.size() > 0) {
                for (Object member : members) {
                    redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_COMMENT + comment.getId(), member);
                }
            }
            return articleService.update(new UpdateWrapper<Article>().eq("id", comment.getArticleId()).setSql("comment_count = comment_count - 1"));
        }
    }

    /**
     * 根据文章id进行删除评论
     *
     * @param ids
     * @return
     */
    @Override
    public boolean removeByArticleId(List<Long> ids) {
        Map<String, Object> map = new HashMap<>();
        //1。将id放入map中进行批量删除
        for (Long item : ids) {
            map.put("article_id", item);
        }
        if (ids.size() == 0) return true;
        List<Comment> comments = this.listByMap(map);
        //2.删除所有评论中的点赞/点踩
        if (comments.size() > 0) {
            for (Comment comment : comments) {
                //删除所有点赞
                Set members = redisTemplate.opsForSet().members(RedisKeyUtils.USER_LIKED_COMMENT + comment.getId());
                if (members.size() > 0) {
                    for (Object member : members) {
                        redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_COMMENT + comment.getId(), member);
                    }
                }
            }
        }
        //根据文章id删除所有评论
        return this.removeByMap(map);
    }

    @Override
    public R doCommentLikeVo(LikeOrDisLikeVo likeOrDisLikeVo) {
        return likeOrDislikeService.doLike(likeOrDisLikeVo, false);
    }

    @Override
    public R cancelCommentLike(LikeOrDisLikeVo likeOrDisLikeVo) {
        return likeOrDislikeService.cancelLike(likeOrDisLikeVo, false);
    }

    @Override
    public R getMoreChildren(CommentQueryVo commentQueryVo) {
        Long articleId = commentQueryVo.getArticleId();
        Long userId = commentQueryVo.getUserId();
        Long rootId = commentQueryVo.getRootId();
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("article_id", articleId)
                .eq("root_id", rootId)
                .orderByAsc("create_time");
        List<Comment> list = this.list(commentQueryWrapper);
        List<CommentLikeVo> likeVoList = new ArrayList<>();
        for (Comment record : list) {
            Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(record.getId(), userId, null), false);
            Long likeCount = (Long) likeCountAndIslike.get("count");
            boolean isLike = (boolean) likeCountAndIslike.get("isLike");
            //进行组织结构
            CommentLikeVo commentLikeVo = new CommentLikeVo();
            BeanUtils.copyProperties(record, commentLikeVo);
            UserVo userInfo = userClient.getUserInfo(record.getUserId());
           if(!Objects.isNull(record.getTargetId())){
               UserVo targetInfo = userClient.getUserInfo(record.getTargetId());
               log.warn("userInfo"+userInfo);
               commentLikeVo.setTarget(targetInfo.getNickName());
           }
            commentLikeVo.setAvatar(userInfo.getAvatar());
            commentLikeVo.setNickname(userInfo.getNickName());
            commentLikeVo.setIsLike(isLike);
            commentLikeVo.setLikeCount(likeCount);
            likeVoList.add(commentLikeVo);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", likeVoList);
        map.put("totalCount", likeVoList.size());
        return R.ok().message(null).data(map);
    }
}
