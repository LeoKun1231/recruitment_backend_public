package com.hqk.recruitment.common.controller;


import com.hqk.recruitment.common.service.ArticleService;
import com.hqk.recruitment.common.service.CommentService;
import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/common/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    @PostMapping("/list")
    public R getArticleList(@RequestBody ArticlePageVo articlePageVo){
        return articleService.getArticleListByPage(articlePageVo);
    }

    @PostMapping("/add")
    public  R addArticle(@RequestBody ArticleVo articleVo){
        boolean isAdd=articleService.addArticle(articleVo);
        if(isAdd){
            return R.ok().message("添加成功");
        }else{
            return R.error().code(20000).message("添加失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    public R removeArticle(@PathVariable("id") Long id){
        boolean isRemove= articleService.removeArticle(id);
        if(isRemove){
            return R.ok().message("删除文章成功");
        }else{
            return R.error().code(20000).message("删除文章失败");
        }
    }

    @PostMapping("/search")
    public R searchArticle(@RequestBody ArticleSearchVo articleSearchVo){
        return articleService.searchArticleByParams(articleSearchVo);
    }

    @GetMapping("/detail/{id}")
    public R getArticleDetailById(@PathVariable Long id, @RequestHeader("Authorization") String authorization){
        return articleService.getArticleDetailById(id,authorization);
    }

    @GetMapping("/reationList/{id}")
    public R getArticleRelationList(@PathVariable Long id){
        return articleService.getArticleRelationList(id);
    }

    @GetMapping("/addWatchCount/{id}")
    public R addWatchCount(@PathVariable Long id){
        return articleService.addWatchCount(id);
    }




    /**
     * 点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/do")
    public R doLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return likeOrDislikeService.doLike(likeOrDisLikeVo,true);
    }

    /**
     * 取消点赞
     * @param likeOrDisLikeVo
     * @return
     */
    @PostMapping("/like/cancel")
    public R cancelLike(@RequestBody LikeOrDisLikeVo likeOrDisLikeVo){
        return likeOrDislikeService.cancelLike(likeOrDisLikeVo,true);
    }

    @GetMapping("/getMajorIds")
    public List<Long> getMajorIds(){
        return articleService.getMajorIdsInArticle();
    }

    @PostMapping("/upload")
    public Map uploadArticle(MultipartFile file){
        return articleService.uploadArticle(file);
    }


    @PostMapping("/getArtilceById")
    public R getArtilceById(@RequestBody PageVo pageVo, @RequestHeader("Authorization") String authorization){
        return articleService.getArticleById(pageVo,authorization);
    }

}

