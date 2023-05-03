package com.hqk.recruitment.common.service;

import com.hqk.recruitment.model.common.Sort;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;

import java.util.List;
import java.util.Map;

public interface LikeOrDislikeService {

    R doLike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle);

    R cancelLike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle);

    Map<String,Object> getLikeCountAndIslike(LikeOrDisLikeVo likeOrDisLikeVo,boolean isArticle);

    Sort getLikeDetails(Long articleId);

    List<Long> getTopLikeList(List<Long> ids);

}
