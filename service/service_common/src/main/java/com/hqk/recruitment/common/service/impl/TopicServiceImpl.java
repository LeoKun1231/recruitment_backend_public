package com.hqk.recruitment.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.common.service.ArticleService;
import com.hqk.recruitment.common.service.CommentService;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.common.Article;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.model.common.Topic;
import com.hqk.recruitment.common.mapper.TopicMapper;
import com.hqk.recruitment.common.service.TopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.utils.TreeUtils;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.TopicUpdateVo;
import com.hqk.recruitment.vo.common.TopicVo;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    private CommentService commentService;

    @Resource
    private ArticleService articleService;


    /**
     * 添加话题
     * @param content
     * @return
     */
    @Override
    public R addTopic(String  content) {
        if(Objects.isNull(content) || content.length()==0 || "".equals(content)){
            throw new MyCustomException(20000,"话题内容不能为空");
        }
        Topic topic = new Topic();
        topic.setContent(content);
        topic.setCount(0L);
        boolean save = this.save(topic);
        if(save){
            return R.ok().message("添加话题成功");
        }else{
            return R.error().message("添加话题失败");
        }
    }

    /**
     * 删除话题
     * @param id
     * @return
     */
    @Override
    public R deleteTopById(Long id) {
        // 1.删除所有带此话题的文章
        List<Article> ids = articleService.list(new QueryWrapper<Article>().eq("topic_id", id).select("id"));
        List<Long> longList = ids.stream().map(Article::getId).collect(Collectors.toList());
        boolean isRemoveArticle = articleService.remove(new QueryWrapper<Article>().eq("topic_id", id));
        //2.删除所有此文章底下的评论
        boolean isRemoveComment = commentService.removeByArticleId(longList);
        //3.删除话题
        boolean b = this.removeById(id);
        if(b){
            return R.ok().message("删除话题成功");
        }else{
            return R.error().message("删除话题失败");
        }
    }

    @Override
    public boolean updateTopic(TopicUpdateVo topicUpdateVo) {
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicUpdateVo,topic);
        return this.updateById(topic);
    }

    @Override
    public R listByPage(PageVo pageVo) {
        Integer currentPage = pageVo.getCurrentPage();
        Integer pageSize = pageVo.getPageSize();
        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }
        Page<Topic> page=new Page<>(currentPage,pageSize);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id",0);
        this.page(page,queryWrapper.orderByDesc("count"));
        return R.ok().message(null).data("records", page.getRecords()).data("totalCount",page.getTotal());
    }

    @Override
    public R getRandomList() {
        // 总记录数
        int count = this.count();
        // 随机数起始位置
        int randomCount =(int) (Math.random()*count);
        // 保证能展示10个数据
        if (randomCount > count-10) {
            randomCount = count-10;
        }

        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.ne("id",0);
        wrapper.last("limit "+String.valueOf(randomCount)+", 10");
        List<Topic> randomList = this.list(wrapper);
        if(randomList.size()!=10){
            List<Long> ids = randomList.stream().map(Topic::getId).collect(Collectors.toList());
            List<Topic> list = this.list(Wrappers.<Topic>lambdaQuery().notIn(Topic::getId, ids).last("limit " + (10 - randomList.size())));
            randomList.addAll(list);
        }
        return R.ok().message(null).data("records",randomList);
    }


    //获取话题详情
    @Override
    public R getTopicDetailById(Long id) {
        List<Article> articles = articleService.list(new QueryWrapper<Article>().eq("topic_id", id).select("watch_count"));
        //相关文章数
        int relationCount = articles.size();
        //观看数
        Long watchCount =articles.stream().map(Article::getWatchCount).reduce(0L, Long::sum);
        //话题名
        Topic topic = this.getById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("relationCount",relationCount);
        map.put("watchCount",watchCount);
        map.put("name",topic.getContent());
        map.put("id",topic.getId());
        return R.ok().message(null).data(map);
    }
}
