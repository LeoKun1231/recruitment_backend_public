package com.hqk.recruitment.company.service;

import com.hqk.recruitment.model.company.UserJobChat;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.company.CompanyListVo;
import com.hqk.recruitment.vo.company.CompanyTypeVo;
import com.hqk.recruitment.vo.company.JobTypeVo;
import com.hqk.recruitment.vo.company.RegisterToChat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface HomeService {
    R addWatchCount(Long id, Integer type);

    R getCompanyHotList(PageVo pageV);

    R getHotJobList(PageVo pageVo);

    RList getCompanyGategoryList();


    R getCompanyList(CompanyTypeVo companyTypeVo);

    RList getJobTypeList();

    R getJobList(JobTypeVo jobTypeVo);

    R getJobListWithType(JobTypeVo jobTypeVo);

    RList getRelationJobList(Map map);

    R getCompanyDetail(Long id);

    RList getCompanyHasType(Long id);

    R getCompanyDetailJobList(JobTypeVo jobTypeVo );

    R getJobDetailById(Long id,String authorization);

    R addResumeToJob(Long id, Long userId);

    R uploadResume(MultipartFile file, String authorization);


    R saveUserJobChat(UserJobChat userJobChat);


    R getResumeByToken(String authorization);

    R getChattingJobs(PageVo pageVo,String authorization);

    RList getPhoneJobType();


    R registerUserToIM(RegisterToChat registerToChat);

    R checkIsChat(UserJobChat userJobChat);

    RList getSearch(Map map);
}
