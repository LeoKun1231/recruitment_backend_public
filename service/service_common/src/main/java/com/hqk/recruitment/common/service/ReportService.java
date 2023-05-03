package com.hqk.recruitment.common.service;

import com.hqk.recruitment.common.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.ReportVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-04
 */
public interface ReportService extends IService<Report> {

    boolean addReportArticle(Report report);

    Map<String, Object> getArticleList(ReportVo reportVo);

    R deleteReport(List<Long> ids);

    R recoverReport(List<Long> ids);

    Map<String, Object> getCommentReportList(ReportVo reportVo);

    R deleteCommentReport(List<Long> ids);

    R recoverCommentReport(List<Long> ids);

}
