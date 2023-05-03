package com.hqk.recruitment.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.common.entity.Report;
import com.hqk.recruitment.common.entity.ReportReason;
import com.hqk.recruitment.common.mapper.ReportMapper;
import com.hqk.recruitment.common.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.common.Article;
import com.hqk.recruitment.model.common.Comment;
import com.hqk.recruitment.model.common.Topic;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.ArticleLikeVo;
import com.hqk.recruitment.vo.common.CommentReportVo;
import com.hqk.recruitment.vo.common.LikeOrDisLikeVo;
import com.hqk.recruitment.vo.common.ReportVo;
import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.beans.BeanUtils;
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
 * @since 2023-04-04
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Resource
    private ArticleService articleService;

    @Resource
    private CommentService commentService;

    @Resource
    private TopicService topicService;

    @Resource
    private LikeOrDislikeService likeOrDislikeService;

    @Resource
    private UserClient userClient;

    @Resource
    private ReportReasonService reportReasonService;

    @Override
    public boolean addReportArticle(Report report) {
        Long commentId = report.getCommentId();
        Long articleId = report.getArticleId();
        String reason = report.getReason();
        if(!Objects.isNull(articleId)){
            return this.addReport("article_id",articleId,reason);
        }else{
            return this.addReport("comment_id",commentId,reason);
        }
    }

    @Override
    public Map<String, Object> getArticleList(ReportVo reportVo) {
        Integer type = reportVo.getType();
        Integer currentPage = reportVo.getCurrentPage();
        Integer pageSize = reportVo.getPageSize();
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Page<Report> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        queryWrapper.isNotNull("article_id");
        if (type == 1) {//被举报最多
            queryWrapper.orderByDesc("report_count");
            this.page(page, queryWrapper.select("id,article_id,comment_id,report_count,update_time"));
            List<Report> list = page.getRecords();
            List<Long> collect = list.stream().map(Report::getArticleId).collect(Collectors.toList());
            if (collect.size() == 0) {
                map.put("records", new ArrayList<>());
                map.put("totalCount", 0);
                return map;
            }
            List<Article> records = articleService.list(new QueryWrapper<Article>().in("id", collect));
            List<ArticleLikeVo> articleLikeVoList = new ArrayList<>();
            for (Article record : records) {
                //获取本人是否点赞
                Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(null, -1L, record.getId()), true);
                Long likeCount = (Long) likeCountAndIslike.get("count");

                //获取话题内容
                Topic topic = topicService.getById(record.getTopicId());

                //获取文章用户信息
                UserVo userInfo = userClient.getUserInfo(record.getUserId());

                //进行组织结构
                ArticleLikeVo articleLikeVo = new ArticleLikeVo();
                BeanUtils.copyProperties(record, articleLikeVo);

                if (!Objects.isNull(topic)) {
                    articleLikeVo.setTopicContent(topic.getContent());
                }
                articleLikeVo.setLikeCount(likeCount);
                articleLikeVo.setAvatar(userInfo.getAvatar());
                articleLikeVo.setNickname(userInfo.getNickName());

                //设置举报数量
                Report report = this.getOne(new QueryWrapper<Report>().select("report_count,id").eq("article_id", record.getId()));
                Long count = report.getReportCount();
                Long reportId = report.getId();
                List<String> reasons = reportReasonService.list(new QueryWrapper<ReportReason>().eq("report_id", reportId)).stream().map(ReportReason::getReason).collect(Collectors.toList());
                articleLikeVo.setReportId(reportId);
                //设置举报内容
                articleLikeVo.setReason(reasons);
                articleLikeVo.setReportCount(count);
                articleLikeVoList.add(articleLikeVo);
            }

            map.put("records", articleLikeVoList.stream().sorted(new Comparator<ArticleLikeVo>() {
                @Override
                public int compare(ArticleLikeVo o1, ArticleLikeVo o2) {
                    return (int) -(o1.getReportCount() - o2.getReportCount());
                }
            }).collect(Collectors.toList()));
            map.put("totalCount", page.getTotal());
            return map;
        } else if (type == 2) {//最新举报
            queryWrapper.orderByDesc("update_time");
            this.page(page, queryWrapper.select("id,article_id,comment_id,report_count,update_time"));
            List<Report> list = page.getRecords();
            List<Long> collect = list.stream().map(Report::getArticleId).collect(Collectors.toList());
            if (collect.size() == 0) {
                map.put("records", new ArrayList<>());
                map.put("totalCount", 0);
                return map;
            }
            List<Article> records = articleService.list(new QueryWrapper<Article>().in("id", collect));
            List<ArticleLikeVo> articleLikeVoList = new ArrayList<>();
            for (Article record : records) {
                //获取本人是否点赞
                Map<String, Object> likeCountAndIslike = likeOrDislikeService.getLikeCountAndIslike(new LikeOrDisLikeVo(null, -1L, record.getId()), true);
                Long likeCount = (Long) likeCountAndIslike.get("count");

                //获取话题内容
                Topic topic = topicService.getById(record.getTopicId());

                //获取文章用户信息
                UserVo userInfo = userClient.getUserInfo(record.getUserId());

                //进行组织结构
                ArticleLikeVo articleLikeVo = new ArticleLikeVo();
                BeanUtils.copyProperties(record, articleLikeVo);

                if (!Objects.isNull(topic)) {
                    articleLikeVo.setTopicContent(topic.getContent());
                }
                articleLikeVo.setLikeCount(likeCount);
                articleLikeVo.setAvatar(userInfo.getAvatar());
                articleLikeVo.setNickname(userInfo.getNickName());

                //设置举报数量
                Report report = this.getOne(new QueryWrapper<Report>().select("report_count,update_time,id").eq("article_id", record.getId()));
                List<String> reasons = reportReasonService.list(new QueryWrapper<ReportReason>().eq("report_id", report.getId())).stream().map(ReportReason::getReason).collect(Collectors.toList());
                //设置举报内容
                articleLikeVo.setReportId(report.getId());
                articleLikeVo.setReason(reasons);
                articleLikeVo.setReportCount(report.getReportCount());
                articleLikeVo.setUpdateTime(report.getUpdateTime());
                articleLikeVoList.add(articleLikeVo);
            }
            map.put("records", articleLikeVoList.stream().sorted(new Comparator<ArticleLikeVo>() {
                @Override
                public int compare(ArticleLikeVo o1, ArticleLikeVo o2) {
                    Date o1UpdateTime = o1.getUpdateTime();
                    Date o2UpdateTime = o2.getUpdateTime();
                    return -o1UpdateTime.compareTo(o2UpdateTime);
                }
            }).collect(Collectors.toList()));
            map.put("totalCount", page.getTotal());
            return map;
        }
        map.put("records", new ArrayList<>());
        map.put("totalCount", 0);
        return map;
    }

    @Override
    public R deleteReport(List<Long> ids) {
        List<Report> list = this.list(new QueryWrapper<Report>().in("id", ids).select("id,article_id,report_count"));
        List<Long> reportIds = list.stream().map(Report::getId).collect(Collectors.toList());
        List<Long> articleIds = list.stream().map(Report::getArticleId).collect(Collectors.toList());
        this.removeByIds(ids);
        List<Long> reportReasonIds = reportReasonService.list(new QueryWrapper<ReportReason>().in("report_id", reportIds).select("id")).stream().map(ReportReason::getId).collect(Collectors.toList());
        reportReasonService.removeByIds(reportReasonIds);
        for (Long articleId : articleIds) {
            articleService.removeArticle(articleId);
        }
        return R.ok().message("删除成功！");
    }

    @Override
    public R recoverReport(List<Long> ids) {
        this.removeByIds(ids);
        List<Long> reportReasonIds = reportReasonService.list(new QueryWrapper<ReportReason>().in("report_id", ids).select("id")).stream().map(ReportReason::getId).collect(Collectors.toList());
        reportReasonService.removeByIds(reportReasonIds);
        return R.ok().message("取消举报成功！");
    }

    @Override
    public Map<String, Object> getCommentReportList(ReportVo reportVo) {
        Integer type = reportVo.getType();
        Integer currentPage = reportVo.getCurrentPage();
        Integer pageSize = reportVo.getPageSize();
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Page<Report> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        queryWrapper.isNotNull("comment_id");
        if(type==1){
            queryWrapper.orderByDesc("report_count");
            this.page(page, queryWrapper.select("id,article_id,comment_id,report_count,update_time"));
            List<Report> list = page.getRecords();
            List<Long> collect = list.stream().map(Report::getCommentId).collect(Collectors.toList());
            if (collect.size() == 0) {
                map.put("records", new ArrayList<>());
                map.put("totalCount", 0);
                return map;
            }

            List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().in("id", collect));

            List<CommentReportVo> commentReportList = new ArrayList<>(commentList.size());

            for (Comment comment : commentList) {
                CommentReportVo commentReportVo = new CommentReportVo();
                UserVo userInfo = userClient.getUserInfo(comment.getUserId());
                commentReportVo.setAvatar(userInfo.getAvatar());
                commentReportVo.setNickName(userInfo.getNickName());
                //设置举报数量
                Report report = this.getOne(new QueryWrapper<Report>().select("report_count,update_time,id").eq("comment_id", comment.getId()));

                List<String> reasons = reportReasonService.list(new QueryWrapper<ReportReason>().eq("report_id", report.getId())).stream().map(ReportReason::getReason).collect(Collectors.toList());
                //设置举报内容
                commentReportVo.setId(report.getId());
                commentReportVo.setReason(reasons);
                commentReportVo.setComment(comment.getContent());
                commentReportVo.setReportCount(report.getReportCount());
                commentReportVo.setUpdateTime(report.getUpdateTime());
                commentReportList.add(commentReportVo);
            }

            map.put("records", commentReportList.stream().sorted(new Comparator<CommentReportVo>() {
                @Override
                public int compare(CommentReportVo o1, CommentReportVo o2) {
                    return (int) -(o1.getReportCount() - o2.getReportCount());
                }
            }).collect(Collectors.toList()));
            map.put("totalCount", page.getTotal());
            return map;
        }else{
            queryWrapper.orderByDesc("update_time");
            this.page(page, queryWrapper.select("id,article_id,comment_id,report_count,update_time"));
            List<Report> list = page.getRecords();
            List<Long> collect = list.stream().map(Report::getCommentId).collect(Collectors.toList());
            if (collect.size() == 0) {
                map.put("records", new ArrayList<>());
                map.put("totalCount", 0);
                return map;
            }

            List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().in("id", collect));

            List<CommentReportVo> commentReportList = new ArrayList<>(commentList.size());

            for (Comment comment : commentList) {
                CommentReportVo commentReportVo = new CommentReportVo();
                UserVo userInfo = userClient.getUserInfo(comment.getUserId());
                commentReportVo.setAvatar(userInfo.getAvatar());
                commentReportVo.setNickName(userInfo.getNickName());
                //设置举报数量
                Report report = this.getOne(new QueryWrapper<Report>().select("report_count,update_time,id").eq("comment_id", comment.getId()));
                List<String> reasons = reportReasonService.list(new QueryWrapper<ReportReason>().eq("report_id", report.getId())).stream().map(ReportReason::getReason).collect(Collectors.toList());
                //设置举报内容
                commentReportVo.setId(report.getId());
                commentReportVo.setReason(reasons);
                commentReportVo.setComment(comment.getContent());
                commentReportVo.setReportCount(report.getReportCount());
                commentReportVo.setUpdateTime(report.getUpdateTime());
                commentReportList.add(commentReportVo);
            }

            map.put("records", commentReportList.stream().sorted(new Comparator<CommentReportVo>() {
                @Override
                public int compare(CommentReportVo o1, CommentReportVo o2) {
                    Date o1UpdateTime = o1.getUpdateTime();
                    Date o2UpdateTime = o2.getUpdateTime();
                    return -o1UpdateTime.compareTo(o2UpdateTime);
                }
            }).collect(Collectors.toList()));
            map.put("totalCount", page.getTotal());
            return map;
        }
    }

    @Override
    public R deleteCommentReport(List<Long> ids) {
        List<Report> list = this.list(new QueryWrapper<Report>().in("id", ids).select("id,comment_id,report_count"));
        List<Long> reportIds = list.stream().map(Report::getId).collect(Collectors.toList());
        List<Long> commentIds = list.stream().map(Report::getCommentId).collect(Collectors.toList());
        this.removeByIds(ids);
        List<Long> reportReasonIds = reportReasonService.list(new QueryWrapper<ReportReason>().in("report_id", reportIds).select("id")).stream().map(ReportReason::getId).collect(Collectors.toList());
        reportReasonService.removeByIds(reportReasonIds);
        for (Long commentId : commentIds) {
            commentService.removeComment(commentId);
        }
        return R.ok().message("删除成功！");
    }

    @Override
    public R recoverCommentReport(List<Long> ids) {
        this.removeByIds(ids);
        List<Long> reportReasonIds = reportReasonService.list(new QueryWrapper<ReportReason>().in("report_id", ids).select("id")).stream().map(ReportReason::getId).collect(Collectors.toList());
        reportReasonService.removeByIds(reportReasonIds);
        return R.ok().message("取消举报成功！");
    }


    public boolean addReport(String columnName,Long id,String reason){
        Report report = new Report();
        if("article_id".equals(columnName)){
            report.setArticleId(id);
        }else{
            report.setCommentId(id);
        }
        Report report2 = this.getOne(new QueryWrapper<Report>().eq(columnName, id).select("id"));
        if (Objects.isNull(report2)) {
            report.setReportCount(1L);
            this.save(report);
            ReportReason reportReason = new ReportReason();
            reportReason.setReportId(report.getId());
            reportReason.setReason(reason);
            return reportReasonService.save(reportReason);
        }else{
            this.update(new UpdateWrapper<Report>().eq(columnName, id).setSql("report_count = report_count + 1"));
            ReportReason reportReason = new ReportReason();
            reportReason.setReportId(report2.getId());
            reportReason.setReason(reason);
            return  reportReasonService.save(reportReason);
        }
    }
}
