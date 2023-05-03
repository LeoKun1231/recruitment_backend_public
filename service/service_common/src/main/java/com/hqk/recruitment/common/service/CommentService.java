package com.hqk.recruitment.common.service;

import com.hqk.recruitment.model.common.Article;
import com.hqk.recruitment.model.common.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.CommentQueryVo;
import com.hqk.recruitment.vo.common.CommentVo;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-01
 */
public interface CommentService extends IService<Comment> {

    R addComment(CommentVo commentVo);

    R getCommentList(CommentQueryVo commentQueryVo);


    boolean removeComment(Long id);

    boolean removeByArticleId(List<Long> ids);

    R doCommentLikeVo(LikeOrDisLikeVo likeOrDisLikeVo);

    R cancelCommentLike(LikeOrDisLikeVo likeOrDisLikeVo);

    R getMoreChildren(CommentQueryVo commentQueryVo);
}
