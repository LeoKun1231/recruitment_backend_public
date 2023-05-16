package com.hqk.recruitment.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.company.service.CompanyService;
import com.hqk.recruitment.company.service.UserJobService;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.company.Company;
import com.hqk.recruitment.model.company.Job;
import com.hqk.recruitment.company.mapper.JobMapper;
import com.hqk.recruitment.company.service.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.company.JobQueryVo;
import com.hqk.recruitment.vo.company.JobUpdateVo;
import com.hqk.recruitment.vo.company.JobVo;
import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-10
 */
@Service
public  class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Resource
    private UserClient userClient;

    @Resource
    private CompanyService companyService;

    @Resource
    private UserJobService userJobService;

    /**
     * 添加岗位
     * @param jobUpdateVo
     * @param authorization
     * @return
     */
    @Override
    public R addJob(JobUpdateVo jobUpdateVo,String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Job job = new Job();
        BeanUtils.copyProperties(jobUpdateVo,job);
        job.setUserId(userId);
        boolean save = this.save(job);
        if(save){
            return R.ok().message("添加岗位成功");
        }else{
            return R.error().message("添加岗位失败");
        }
    }


    /**
     * 更新岗位
     * @param jobUpdateVo
     * @return
     */
    @Override
    public R updateJob(JobUpdateVo jobUpdateVo) {
        Job job = new Job();
        BeanUtils.copyProperties(jobUpdateVo,job);
        boolean b = this.updateById(job);
        if(b){
            return R.ok().message("修改岗位成功");
        }else {
            return R.error().message("修改岗位失败");
        }
    }


    /**
     * 获取岗位列表 - 后台
     * @param jobQueryVo
     * @param authorization
     * @return
     */
    @Override
    public R getJobList(JobQueryVo jobQueryVo, String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Integer currentPage = jobQueryVo.getCurrentPage();
        Integer pageSize = jobQueryVo.getPageSize();
        String jobName = jobQueryVo.getJobName();
        Integer status = jobQueryVo.getStatus();
        String addressName = jobQueryVo.getAddressName();
        String jobRequire = jobQueryVo.getJobRequire();
        List<String> jobType = jobQueryVo.getJobType();
        List<String> city = jobQueryVo.getCity();


        UserVo userInfo = userClient.getUserInfo(userId);
        String companyId = userInfo.getCompanyId();


        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }

        Page<Job> page=new Page<>(currentPage,pageSize);
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("company_id",companyId);
        Long userId1 = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, companyId).select(Company::getUserId)).getUserId();

        //如果是hr，则查询自己创建的岗位 否则查询所有
        if(!userId1.equals(userId)){
            queryWrapper.eq("user_id",userId);
        }

        if(!Objects.isNull(status)){
            queryWrapper.eq("status",status);
        }

        if(!Objects.isNull(jobName)){
            queryWrapper.like("job_name",jobName);
        }

        if(!Objects.isNull(jobRequire)){
            queryWrapper.eq("job_require",jobRequire);
        }
        if(!Objects.isNull(addressName)){
            queryWrapper.like("address_name",addressName);
        }

        if(!Objects.isNull(city) && city.size()>0){
            queryWrapper.eq("city", JSON.toJSON(city).toString());
        }

        if(!Objects.isNull(jobType) && jobType.size()>0){
            queryWrapper.eq("job_type",JSON.toJSON(jobType).toString());
        }


        if(!Objects.isNull(jobQueryVo.getCreateTime())){
            List<String> createTime = jobQueryVo.getCreateTime();
            if(!Objects.isNull(createTime.get(0))&&!Objects.isNull(createTime.get(1))){
                String date = createTime.get(0);
                String date1 = createTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("create_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!Objects.isNull(jobQueryVo.getUpdateTime())){
            List<String> updateTime = jobQueryVo.getUpdateTime();
            if(!Objects.isNull(updateTime.get(0))&&!Objects.isNull(updateTime.get(1))){
                String date = updateTime.get(0);
                String date1 = updateTime.get(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startTime = simpleDateFormat.parse(date);
                    Date endTime = simpleDateFormat.parse(date1);
                    queryWrapper.between("update_time",startTime,endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        queryWrapper.orderByDesc("id");
        List<Job> records = this.page(page, queryWrapper).getRecords();
        List<JobVo> list = new ArrayList<>(records.size());
        for (Job record : records) {
            JobVo jobVo = new JobVo();
            BeanUtils.copyProperties(record,jobVo);
            String money=record.getStartMoney()+"K~"+record.getEndMoney()+"K  "+record.getMoneyMonth()+"薪";
            jobVo.setMoney(money);
            list.add(jobVo);
        }
        return R.ok().data("records", list).data("totalCount",page.getTotal()).message(null);
    }


    /**
     * 获取职位详情 - 后台
     * @param id
     * @return
     */
    @Override
    public R getJobDetail(Long id ) {
        Job one = this.getOne(Wrappers.<Job>lambdaQuery().eq(Job::getId, id));
        return R.ok().message(null).data("data",one);
    }


    /**
     *
     * @param id
     * @return
     */
    @Override
    public R deleteJobById(Long id) {
        boolean b = this.removeById(id);
        if(b){
            return R.ok().message("删除岗位成功");
        }else{
            return R.error().message("删除岗位失败");
        }
    }

    @Override
    public R deleteJobByIds(List<Long> ids) {
        if(ids.size()==0){
            throw new MyCustomException(20000,"删除用户数组不能为空");
        }
        boolean b = this.removeByIds(ids);
        if(b){
            return R.ok().message("删除岗位成功");
        }else{
            return R.error().message("删除岗位失败");
        }
    }

    @Override
    public R changeStatus(Long id) {
        Job job = this.getOne(Wrappers.<Job>lambdaQuery().eq(Job::getId, id));
        Integer status = job.getStatus();
        job.setStatus(status==1?0:1);
        boolean b = this.updateById(job);
        if(b){
            return R.ok().message(null);
        }else{
            return R.error().message("下架失败");
        }
    }
}
