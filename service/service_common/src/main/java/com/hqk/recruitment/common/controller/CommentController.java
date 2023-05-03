package com.hqk.recruitment.common.controller;


import com.hqk.recruitment.common.service.CommentService;
import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.model.common.Comment;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.CommentQueryVo;
import com.hqk.recruitment.vo.common.CommentVo;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-01
 */
@RestController
@RequestMapping("/common/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    @PostMapping("/list")
    public R getCommentList(@RequestBody CommentQueryVo commentQueryVo){
        return commentService.getCommentList(commentQueryVo);
    }

    @PostMapping("/add")
    public  R addComment(@RequestBody CommentVo commentVo){
        return commentService.addComment(commentVo);
    }

    @DeleteMapping("/delete/{id}")
    public R removeComment(@PathVariable("id") Long id){
        boolean isRemove= commentService.removeComment(id);
        if(isRemove){
            return R.ok().message("删除评论成功");
        }else{
            return R.error().message("删除评论失败");
        }
    }


    /**
     * 点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/do")
    public R doLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return commentService.doCommentLikeVo(likeOrDisLikeVo);
    }

    /**
     * 取消点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/cancel")
    public R cancelLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return commentService.cancelCommentLike(likeOrDisLikeVo);
    }


    @PostMapping("/children")
    public R getMoreChildren(@RequestBody CommentQueryVo commentQueryVo){
        return commentService.getMoreChildren(commentQueryVo);
    }


}

