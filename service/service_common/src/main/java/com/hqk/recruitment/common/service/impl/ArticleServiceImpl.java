package com.hqk.recruitment.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.OssClient;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.common.service.CommentService;
import com.hqk.recruitment.common.service.LikeOrDislikeService;
import com.hqk.recruitment.common.service.TopicService;
import com.hqk.recruitment.common.utils.RedisKeyUtils;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.common.Article;
import com.hqk.recruitment.common.mapper.ArticleMapper;
import com.hqk.recruitment.common.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.common.Comment;
import com.hqk.recruitment.model.common.Topic;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.*;
import com.hqk.recruitment.vo.user.UserVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-03
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    //0 1 2 3 4
    public static final String [] types={"全部", "最热", "最新", "最多评论","最多点赞"};

    //0 1 2 3
    public static final String [] category={"全部", "闲聊", "提问题", "提建议"};


    @Resource
    private UserClient userClient;


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    @Resource
    private CommentService commentService;

    @Resource
    private TopicService topicService;

    @Resource
    private OssClient ossClient;


    //添加文章
    @Override
    public boolean addArticle(ArticleVo articleVo) {
        Article article = new Article();
        UserVo userInfo = userClient.getUserInfo(articleVo.getUserId());
        int maxLength = 100;
        //截取前一百个字符作为预览
        Document document = Jsoup.parse(articleVo.getContent());
        String preViewContent = document.text().substring(0, Math.min(document.text().length(), maxLength));

        BeanUtils.copyProperties(articleVo, article);
        //设置默认话题
        if(Objects.isNull(articleVo.getTopicId())){
            article.setTopicId(0L);
        }

        if(Objects.isNull(userInfo.getMajorId())){
            article.setMajorId(0L);
        }else{
            article.setMajorId(userInfo.getMajorId());
        }

        article.setType(category[Math.toIntExact(articleVo.getTypeId())]);
        article.setContentPreview(preViewContent);
        boolean save = this.save(article);
        if(save){
            if(!Objects.isNull(articleVo.getTopicId())){
                topicService.update(new UpdateWrapper<Topic>().eq("id",articleVo.getTopicId()).setSql("count = count + 1"));
            }
        }
        return save;
    }

    //删除文章
    @Override
    public boolean removeArticle(Long articleId) {
        //删除redis中的缓存数据
        Set members = redisTemplate.opsForSet().members(RedisKeyUtils.USER_LIKED_ARTICLE + articleId);
        if(members.size()>0){
            for (Object member : members) {
                redisTemplate.opsForSet().remove(RedisKeyUtils.USER_LIKED_ARTICLE+articleId,member);
            }
        }
        Article article = this.getOne(new QueryWrapper<Article>().eq("id", articleId));
        boolean b = this.removeById(articleId);
        if(b){
            commentService.remove(new QueryWrapper<Comment>().eq("article_id",articleId));
            topicService.update(new UpdateWrapper<Topic>().eq("id",article.getTopicId()).setSql("count = count -1"));
            return true;
        }else{
            return false;
        }
    }

    @Override
    public R getArticleListByPage(ArticlePageVo articlePageVo) {
        Integer typedId = articlePageVo.getTypedId();
        Integer categoryId = articlePageVo.getCategoryId();
        Integer otherId = articlePageVo.getOtherId();
        Long topicId = articlePageVo.getTopicId();
        Integer currentPage = articlePageVo.getCurrentPage();
        Integer pageSize = articlePageVo.getPageSize();
        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();


        if(!Objects.isNull(typedId) && typedId!=0){

            //查询最热的文章
            if(typedId==1){
                //根据浏览量排序
                queryWrapper.orderByDesc("watch_count");
            }
            //查询最新的文章
            if(typedId==2){
                queryWrapper.orderByDesc("create_time");
            }

            //查询最多评论
            if(typedId==3){
                queryWrapper.orderByDesc("comment_count");
            }

            //查询最多点赞
            if(typedId==4){
                List<Article> articles = this.list(new QueryWrapper<Article>().select("id"));
                List<Long> list = articles.stream().map(item -> item.getId()).collect(Collectors.toList());
                //获取所有文章id并按点赞数排序
                List<Long> topLikeList = likeOrDislikeService.getTopLikeList(list);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("order by field(id,");
                int length=topLikeList.size();
                for (int i = 0; i < length; i++) {
                    if(i==0){
                        stringBuilder.append(topLikeList.get(i));
                    }else{
                        stringBuilder.append(",").append(topLikeList.get(i));
                    }
                    if(i==length-1){
                        stringBuilder.append(")");
                    }
                }
                queryWrapper.last(String.valueOf(stringBuilder));
            }
        }


        //根据分类查询
        if(!Objects.isNull(categoryId) && categoryId!=0){
            queryWrapper.eq("type",category[categoryId]);
        }

        //根据系别排序
        if(!Objects.isNull(otherId) && otherId!=0){
            queryWrapper.eq("major_id",otherId);
        }

        //根据话题查询
        if(!Objects.isNull(topicId)){
            queryWrapper.eq("topic_id",topicId);
        }


        Page<Article> page=new Page<>(currentPage,pageSize);

        this.page(page,queryWrapper);
        List<Article> records = page.getRecords();
        List<ArticleLikeVo> articleLikeVoList = new ArrayList<>();
        for (Article recrord : records) {
            //获取本人是否点赞
            Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(null, articlePageVo.getUserId(), recrord.getId()), true);
            Long likeCount = (Long) likeCountAndIslike.get("count");
            boolean isLike = (boolean) likeCountAndIslike.get("isLike");

            //获取话题内容
            Topic topic = topicService.getById(recrord.getTopicId());

            //获取文章用户信息
            UserVo userInfo = userClient.getUserInfo(recrord.getUserId());

            String majorNameById = userClient.getMajorNameById(recrord.getMajorId());

            //进行组织结构
            ArticleLikeVo articleLikeVo = new ArticleLikeVo();
            BeanUtils.copyProperties(recrord, articleLikeVo);

            if(!Objects.isNull(topic)){
                articleLikeVo.setTopicContent(topic.getContent());
            }

            articleLikeVo.setIsLike(isLike);
            articleLikeVo.setMajorName(majorNameById);
            articleLikeVo.setLikeCount(likeCount);
            if(!Objects.isNull(userInfo)){
                articleLikeVo.setAvatar(userInfo.getAvatar());
                articleLikeVo.setNickname(userInfo.getNickName());
            }
            articleLikeVoList.add(articleLikeVo);
        }
        return R.ok().message(null).data("records",articleLikeVoList).data("totalCount",page.getTotal());
    }

    @Override
    public R searchArticleByParams(ArticleSearchVo  articleSearchVo) {
        if(StringUtils.isEmpty(articleSearchVo.getTitle())){
           return R.ok().message(null).data("list",new ArrayList());
        }
        if(Objects.isNull(articleSearchVo)){
            throw  new MyCustomException(20000,"搜索参数不能为空");
        }
        List<ArticleSearchVo> list = this.list(new QueryWrapper<Article>().like("title", articleSearchVo.getTitle()).select("id", "title")).stream().map(item -> new ArticleSearchVo(item.getTitle(), item.getId())).collect(Collectors.toList());
        return R.ok().message(null).data("list",list);
    }

    @Override
    public R getArticleDetailById(Long id, String authorization) {

        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);

        Article article = this.getById(id);
        Topic topic = topicService.getById(article.getTopicId());
        UserVo userInfo = userClient.getUserInfo(article.getUserId());
        Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(null,userId, article.getId()), true);
        Long likeCount = (Long) likeCountAndIslike.get("count");
        boolean isLike = (boolean) likeCountAndIslike.get("isLike");

        ArticleDetailVo articleDetailVo = new ArticleDetailVo();
        BeanUtils.copyProperties(article,articleDetailVo);
        articleDetailVo.setTopicContent(topic.getContent());
        articleDetailVo.setAvatar(userInfo.getAvatar());
        articleDetailVo.setNickname(userInfo.getNickName());
        articleDetailVo.setLikeCount(likeCount);
        articleDetailVo.setIsLike(isLike);
        HashMap<String, Object> map = new HashMap<>();
        map.put("data",articleDetailVo);
        return R.ok().message(null).data(map);
    }

    @Override
    public R getArticleRelationList(Long id) {

        //先查询该篇文章信息
        Article article = this.getOne(new QueryWrapper<Article>().eq("id", id));

        //1.先从topic_id找
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id",article.getTopicId());
        queryWrapper.ne("id",id);
        int count = this.count(queryWrapper);
        log.warn("count"+count);
        //够5条
        if(count>=5){
            List<Article> list = this.list(queryWrapper.last("limit 5"));
            return this.getArticleRelationVoList(list);
        }
        //不够五条,根据type查找
        List<Article> list = this.list(queryWrapper);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("type",article.getType());
        wrapper.ne("id",id);
        int count1 = this.count(wrapper);
        log.warn("count1"+count1);
        if(list.size()>0){
            wrapper.notIn("id",list.stream().map(Article::getId).collect(Collectors.toList()));
        }
        //两者够5条
        if(count1+count>=5){
            int finalCount=5-count;
            List<Article> list1 = this.list(wrapper.last("limit " + finalCount));
            list.addAll(list1);
            return this.getArticleRelationVoList(list);
        }
        //两者之和不够五条
        //根据major_id在查
        List<Article> list1 = this.list(wrapper);
        list.addAll(list1);
        QueryWrapper<Article> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("major_id",article.getMajorId());
        wrapper1.ne("id",id);
        int count2 = this.count(wrapper1);
        log.warn("count2"+count2);
        if(list.size()>0){
            wrapper1.notIn("id",list.stream().map(Article::getId).collect(Collectors.toList()));
        }
        //够五条
        if(count1+count2+count>=5){
            int finalCount=5-count-count1;
            List<Article> list2 = this.list(wrapper1.last("limit " + finalCount));
            list.addAll(list2);
            return this.getArticleRelationVoList(list);
        }
        //不够五条，直接默认给几条观看最多的
        List<Article> list2 = this.list(wrapper1);
        list.addAll(list2);
        QueryWrapper<Article> wrapper2 = new QueryWrapper<>();
        int finalCount=5-count1-count2-count;
        if(list.size()>0){
            wrapper2.notIn("id",list.stream().map(Article::getId).collect(Collectors.toList()));
        }
        wrapper2.orderByDesc("watch_count").last("limit "+finalCount);
        wrapper2.ne("id",id);
        List<Article> list3 = this.list(wrapper2);
        list.addAll(list3);
        return this.getArticleRelationVoList(list);
    }

    @Override
    public R addWatchCount(Long id) {
        boolean id1 = this.update(new UpdateWrapper<Article>().eq("id", id).setSql("watch_count = watch_count + 1"));
        if(id1){
            return R.ok().message(null);
        }else{
            return R.error().message("访问量增加错误");
        }
    }

    @Override
    public List<Long> getMajorIdsInArticle() {
        List<Long> major_id = this.list(new QueryWrapper<Article>().select("major_id")).stream().map(Article::getMajorId).collect(Collectors.toList());
        return major_id;
    }

    @Override
    public Map uploadArticle(MultipartFile file) {

        Map<String, Object> map = new HashMap<>();

        Map<String, Object> map1 = new HashMap<>();
        String s = ossClient.uploadUrl(file);
        map1.put("url",s);
        map.put("errno",0);
        map.put("data",map1);
        return map;
    }

    @Override
    public R getArticleById(PageVo pageVo, String authorization) {

        Integer currentPage = pageVo.getCurrentPage();
        Integer pageSize = pageVo.getPageSize();
        //如果没有传则传设置默认currentPage=1 pageSize=6
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 6;
        }
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Page<Article> page = new Page<>(currentPage, pageSize);
        this.page(page,Wrappers.<Article>lambdaQuery().eq(Article::getUserId,userId));
        List<Article> articleList = page.getRecords();
        log.warn(currentPage+"="+"page"+pageSize);
        log.warn(page.getRecords()+"");

        List<ArticleLikeVo> articleLikeVoList = new ArrayList<>();
        for (Article record : articleList) {
            //获取本人是否点赞
            Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(null, record.getUserId(), record.getId()), true);
            Long likeCount = (Long) likeCountAndIslike.get("count");
            boolean isLike = (boolean) likeCountAndIslike.get("isLike");

            //获取话题内容
            Topic topic = topicService.getById(record.getTopicId());

            //获取文章用户信息
            UserVo userInfo = userClient.getUserInfo(record.getUserId());

            String majorNameById = userClient.getMajorNameById(record.getMajorId());

            //进行组织结构
            ArticleLikeVo articleLikeVo = new ArticleLikeVo();
            BeanUtils.copyProperties(record, articleLikeVo);

            if(!Objects.isNull(topic)){
                articleLikeVo.setTopicContent(topic.getContent());
            }

            articleLikeVo.setIsLike(isLike);
            articleLikeVo.setMajorName(majorNameById);
            articleLikeVo.setLikeCount(likeCount);
            articleLikeVo.setAvatar(userInfo.getAvatar());
            articleLikeVo.setNickname(userInfo.getNickName());
            articleLikeVoList.add(articleLikeVo);
        }
        return R.ok().message(null).data("records",articleLikeVoList).data("totalCount",page.getTotal());
    }


    private R getArticleRelationVoList(List<Article> list){
        List<ArticleRelationVo> relationList = new ArrayList<>();
        for (Article article1 : list) {
            ArticleRelationVo articleRelationVo = new ArticleRelationVo();
            BeanUtils.copyProperties(article1,articleRelationVo);
            relationList.add(articleRelationVo);
        }
        return R.ok().message(null).data("list",relationList);
    }
}
