package com.hqk.recruitment.common.controller;


import com.hqk.recruitment.model.common.Report;
import com.hqk.recruitment.common.service.ReportService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.ReportVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-04
 */
@RestController
@RequestMapping("/common/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping("/article/add")
    public R report(@RequestBody Report report){
        boolean save = reportService.addReportArticle(report);
        if(save){
            return R.ok().message("举报成功");
        }else {
            return R.error().message("举报失败");
        }
    }

    @PostMapping("/article/list")
    public R reportByList(@RequestBody ReportVo reportVo){
        Map<String, Object> map = reportService.getArticleList(reportVo);
        return R.ok().data(map).message(null);
    }

    @DeleteMapping("/article/delete")
    public R deleteReport(@RequestBody List<Long> ids){
        return reportService.deleteReport(ids);
    }

    @DeleteMapping("/article/recover")
    public R recoverReport(@RequestBody List<Long> ids){
        return reportService.recoverReport(ids);
    }


    @PostMapping("/comment/add")
    public R reportComment(@RequestBody Report report){
        boolean save = reportService.addReportArticle(report);
        if(save){
            return R.ok().message("举报成功");
        }else {
            return R.error().message("举报失败");
        }
    }

    @PostMapping("/comment/list")
    public R reportCommentList(@RequestBody ReportVo reportVo){
        Map<String, Object> map = reportService.getCommentReportList(reportVo);
        return R.ok().data(map).message(null);
    }

    @DeleteMapping("/comment/delete")
    public R deleteCommentReport(@RequestBody List<Long> ids){
        return reportService.deleteCommentReport(ids);
    }

    @DeleteMapping("/comment/recover")
    public R recoverCommentReport(@RequestBody List<Long> ids){
        return reportService.recoverCommentReport(ids);
    }


}

