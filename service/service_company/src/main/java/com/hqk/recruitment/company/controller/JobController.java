package com.hqk.recruitment.company.controller;


import com.hqk.recruitment.company.service.JobService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.company.JobQueryVo;
import com.hqk.recruitment.vo.company.JobUpdateVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/company/job")
public class JobController {

    @Resource
    private JobService jobService;

    @PostMapping("/add")
    public R addJob(@RequestBody JobUpdateVo jobUpdateVo,@RequestHeader("Authorization") String authorization){
        return jobService.addJob(jobUpdateVo,authorization);
    }

    @PutMapping("/update")
    public R updateJob(@RequestBody JobUpdateVo jobUpdateVo){
        return jobService.updateJob(jobUpdateVo);
    }

    @PostMapping("/list")
    public R getJobList(@RequestBody JobQueryVo jobQueryVo,@RequestHeader("Authorization") String authorization){
        return jobService.getJobList(jobQueryVo,authorization);
    }

    @DeleteMapping("/delete/{id}")
    public R getJobList(@PathVariable("id") Long id){
        return jobService.deleteJobById(id);
    }

    @DeleteMapping("/batchDelete")
    public R batchDeleteJob(@RequestBody List<Long> ids){
        return  jobService.deleteJobByIds(ids);
    }

    @GetMapping("/{id}")
    public R getJobDetail(@PathVariable("id") Long id){
        return jobService.getJobDetail(id);
    }

    @GetMapping("/status/{id}")
    public R changeStatus(@PathVariable("id") Long id){
        return jobService.changeStatus(id);
    }



}

