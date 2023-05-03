package com.hqk.recruitment.common.controller;

import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/common")
public class LikeOrDisLikeController {

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    /**
     * 点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/do")
    public R doLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return likeOrDislikeService.doLike(likeOrDisLikeVo,false);
    }

    /**
     * 取消点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/cancel")
    public R cancelLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return likeOrDislikeService.cancelLike(likeOrDisLikeVo,false);
    }


//    /**
//     * 获取点赞次数以及是否点赞
//     * @param likeOrDisLikeVo
//     * @return
//     */
//    @PostMapping("/like/detail")
//    public R getLikeDetail(LikeOrDisLikeVo likeOrDisLikeVo){
//        return likeOrDislikeService.getLikeCountAndIslike(likeOrDisLikeVo,false);
//    }



//    /**
//     * 获取点踩次数以及是否点踩
//     * @param likeOrDisLikeVo
//     * @return
//     */
//    @PostMapping("/dislike/detail")
//    public R getDisLikeDetail(LikeOrDisLikeVo likeOrDisLikeVo){
//        return likeOrDislikeService.getDisLikeCountAndIsDislike(likeOrDisLikeVo);
//    }
}