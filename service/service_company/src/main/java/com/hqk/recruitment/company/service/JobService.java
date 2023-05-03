package com.hqk.recruitment.company.service;

import com.hqk.recruitment.model.company.Job;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.company.JobQueryVo;
import com.hqk.recruitment.vo.company.JobUpdateVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-10
 */
public interface JobService extends IService<Job> {

    R addJob(JobUpdateVo jobUpdateVo,String authorization);

    R updateJob(JobUpdateVo jobUpdateVo);

    R getJobList(JobQueryVo jobQueryVo, String authorization);

    R getJobDetail(Long id);

    R deleteJobById(Long id);

    R deleteJobByIds(List<Long> ids);

    R changeStatus(Long id);
}
