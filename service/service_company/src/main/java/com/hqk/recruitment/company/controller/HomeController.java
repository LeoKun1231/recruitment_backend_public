package com.hqk.recruitment.company.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.company.service.HomeService;
import com.hqk.recruitment.model.company.UserJobChat;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.company.CompanyListVo;
import com.hqk.recruitment.vo.company.CompanyTypeVo;
import com.hqk.recruitment.vo.company.JobTypeVo;
import com.hqk.recruitment.vo.company.RegisterToChat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HomeService homeService;


    @GetMapping("/watch/{id}/{type}")
    public R addWatchCount(@PathVariable("id") Long id,@PathVariable("type") Integer type){
        return homeService.addWatchCount(id,type);
    }


    @PostMapping("/company/hot")
    public R getHotCompanyList(@RequestBody PageVo pageVo){
      return   homeService.getCompanyHotList(pageVo);
    }

    @PostMapping("/job/hot")
    public R getHotJobList(@RequestBody PageVo pageVo){
        return homeService.getHotJobList(pageVo);
    }

    @GetMapping("/company/category")
    public RList getCompanyGategoryList(){
        return homeService.getCompanyGategoryList();
    }

    @GetMapping("/job/type")
    public RList getJobType(){
        return homeService.getJobTypeList();
    }

    @PostMapping("/company/list")
    public R getHomeCompanyList(@RequestBody CompanyTypeVo companyTypeVo){
        return   homeService.getCompanyList(companyTypeVo);
    }

    @PostMapping("/job/list")
    public R getHomeJobList(@RequestBody JobTypeVo jobTypeVo){
        return   homeService.getJobList(jobTypeVo);
    }

    @PostMapping("/job/list/type")
    public R getJobListWithType(@RequestBody JobTypeVo jobTypeVo){
        return homeService.getJobListWithType(jobTypeVo);
    }


    @PostMapping("/job/relation")
    public RList getRelationJobList(@RequestBody Map map){
        return homeService.getRelationJobList(map);
    }


    @GetMapping("/company/detail/{id}")
    public R getCompanyDetail(@PathVariable("id") Long id){
        return homeService.getCompanyDetail(id);
    }

    @GetMapping("/company/deatil/type/{id}")
    public RList getCompanyHasType(@PathVariable("id") Long id){
        return homeService.getCompanyHasType(id);
    }


    @PostMapping("/company/detail/job/list")
    public R getCompanyDetailJobList(@RequestBody JobTypeVo jobTypeVo ){
        return homeService.getCompanyDetailJobList(jobTypeVo);
    }


    @GetMapping("/job/detail/{id}")
    public R getJobDetail(@PathVariable("id") Long id,@RequestHeader("Authorization") String authorization){
        return homeService.getJobDetailById(id,authorization);
    }

    @GetMapping("/resume/{id}/{userId}")
    public R addResumeToJob(@PathVariable("id") Long id,@PathVariable("userId") Long userId){
        return homeService.addResumeToJob(id,userId);
    }


    @PostMapping("/upload/resume")
    public R uploadResume(MultipartFile file, @RequestHeader("Authorization") String authorization){
        return homeService.uploadResume(file,authorization);
    }


    @PostMapping("/registerToIM")
    public R registerUserToIM(@RequestBody RegisterToChat registerToChat){
        return homeService.registerUserToIM(registerToChat);
    }

    @PostMapping("/saveChatRecord")
    public R saveUserJobChat(@RequestBody UserJobChat userJobChat){
        return homeService.saveUserJobChat(userJobChat);
    }

    @PostMapping("/checkIsChat")
    public R checkIsChat(@RequestBody UserJobChat userJobChat){
        return homeService.checkIsChat(userJobChat);
    }

    @GetMapping("/resume/url")
    public R getResume( @RequestHeader("Authorization") String authorization){
        return homeService.getResumeByToken(authorization);
    }

    @PostMapping("/chatedJobList")
    public R getChattingJobs(@RequestBody PageVo pageVo,@RequestHeader("Authorization") String authorization){
        return homeService.getChattingJobs(pageVo,authorization);
    }

    @GetMapping("/phone/job/type")
    public RList getPhoneJobType(){
        return homeService.getPhoneJobType();
    }

    @PostMapping("/search")
    public RList getSearch(@RequestBody Map map){
        return homeService.getSearch(map);
    }



}
