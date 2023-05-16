package com.hqk.recruitment.company.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.OssClient;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.company.service.*;
import com.hqk.recruitment.company.utils.HttpUtils;
import com.hqk.recruitment.model.company.*;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.company.*;
import com.hqk.recruitment.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Resource
    private JobService jobService;

    @Resource
    private CompanyService companyService;

    @Resource
    private UserClient userClient;

    @Resource
    private UserResumeService userResumeService;

    @Resource
    private OssClient ossClient;

    @Resource
    private UserJobService userJobService;

    @Resource
    private UserJobChatService userJobChatService;

    /**
     * 为公司或者职位添加浏览量
     * @param id
     * @param type
     * @return
     */
    @Override
    public R addWatchCount(Long id, Integer type) {
        if (1 == type) {
            companyService.update(Wrappers.<Company>lambdaUpdate().eq(Company::getId, id).setSql("watch_count = watch_count + 1"));
        } else {
            jobService.update(Wrappers.<Job>lambdaUpdate().eq(Job::getId, id).setSql("watch_count = watch_count + 1"));
        }
        return R.ok().message(null);
    }


    /**
     * 获取热门公司
     * @param pageVo
     * @return
     */
    @Override
    public R getCompanyHotList(PageVo pageVo) {
        return this.getCompanys(pageVo, null);
    }


    /**
     * 获取热门职位
     * @param pageVo
     * @return
     */
    @Override
    public R getHotJobList(PageVo pageVo) {
        return this.getJobs(pageVo, null, null, null);
    }


    /**
     * 获取处于上线的公司分类列表
     * @return
     */
    @Override
    public RList getCompanyGategoryList() {
        List<Long> activeCompanyIds = userClient.getActiveCompany();
        List<String> category = companyService.list(new QueryWrapper<Company>().select("Distinct category").in("user_id", activeCompanyIds).isNotNull("category")).stream().map(Company::getCategory).collect(Collectors.toList());
        return RList.ok().data(category).message(null);
    }


    /**
     * 获取公司列表
     * @param companyTypeVo
     * @return
     */
    @Override
    public R getCompanyList(CompanyTypeVo companyTypeVo) {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(companyTypeVo, pageVo);
        return this.getCompanys(pageVo, companyTypeVo.getCategory());
    }


    /**
     * 获取职位列表
     * @param jobTypeVo
     * @return
     */
    @Override
    public R getJobList(JobTypeVo jobTypeVo) {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(jobTypeVo, pageVo);
        return this.getJobs(pageVo, jobTypeVo.getType(), null, null);
    }


    /**
     * 首页根据类别获取职位列表
     * @param jobTypeVo
     * @return
     */
    @Override
    public R getJobListWithType(JobTypeVo jobTypeVo) {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(jobTypeVo, pageVo);
        return this.getJobs(pageVo, null, jobTypeVo.getType(), null);
    }


    /**
     * 获取相关职位
     * @param map
     * @return
     */
    @Override
    public RList getRelationJobList(Map map) {
        String firstLabel = (String) map.get("firstLabel");
        String secondLabel = (String) map.get("secondLabel");
        String thirdLabel = (String) map.get("thirdLabel");

        //判断是否是有职业详情进入的
        boolean isThird = (boolean) map.get("isThird");
        String jobId = (String) map.get("jobId");

        //先获取上架的公司
        List<Long> activeCompanyIds = userClient.getActiveCompany();

        List<String> companyIds = companyService.list(Wrappers.<Company>lambdaQuery().in(Company::getUserId, activeCompanyIds).select(Company::getId)).stream().map(Company::getId).collect(Collectors.toList());

        //获取第二层type
        List<List<String>> job_type = jobService.list(new QueryWrapper<Job>().select("distinct job_type")).stream().map(Job::getJobType).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for (List<String> stringList : job_type) {
            if (!isThird) {
                //如果不是是职业详情进来的 则只查找第二层type不查找第三层
                if (stringList.get(1).equals(secondLabel) && !stringList.get(2).equals(thirdLabel)) {
                    list.add(JSON.toJSON(stringList).toString());
                }
            } else {
                //如果是职业详情进来的 则优先查找与岗位相关的职位 也就是第三个type
                if (stringList.get(1).equals(secondLabel) && stringList.get(2).equals(thirdLabel)) {
                    list.add(JSON.toJSON(stringList).toString());
                }
            }
        }
        List<Job> finalList = new ArrayList<>(5);
        if (list.size() != 0) {
            List<Job> list1 = null;
            //如果是职业详情进来的 先判断是否长度大于等于5 如果没有则查找第二层但是不包括第三层的
            if (isThird) {
                list1 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1)
                        .ne(Job::getId, jobId).in(Job::getJobType, list)
                        .in(Job::getCompanyId, companyIds).last("limit 5"));
                if (list1.size() < 5) {
                    int count = 5 - list1.size();
                    List<String> stringList1 = new ArrayList<>(count);
                    for (List<String> stringList : job_type) {
                        if (stringList.get(1).equals(secondLabel) && !stringList.get(2).equals(thirdLabel)) {
                            stringList1.add(JSON.toJSON(stringList).toString());
                        }
                    }
                    if (stringList1.size() != 0) {
                        List<Job> list2 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).in(Job::getJobType, stringList1).in(Job::getCompanyId, companyIds).last("limit " + count));
                        list1.addAll(list2);
                    }
                }
            } else {
                list1 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).in(Job::getJobType, list).in(Job::getCompanyId, companyIds).last("limit 5"));
            }
            finalList.addAll(list1);
        }

        //获取第一层type
        //如果数量少于5
        if (finalList.size() < 5) {
            List<String> list2 = new ArrayList<>();
            for (List<String> stringList : job_type) {
                if (stringList.get(0).equals(firstLabel) && !stringList.get(1).equals(secondLabel) && !stringList.get(2).equals(thirdLabel)) {
                    list2.add(JSON.toJSON(stringList).toString());
                }
            }
            if (list2.size() != 0) {
                int count = 5 - finalList.size();
                List<Job> list3 = null;
                if (isThird) {
                    list3 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).ne(Job::getId, jobId).in(Job::getJobType, list2).in(Job::getCompanyId, companyIds).last(" limit " + count));
                } else {
                    list3 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).in(Job::getJobType, list2).in(Job::getCompanyId, companyIds).last(" limit " + count));

                }
                finalList.addAll(list3);
            }
        }
        //获取其他敢为
        if (finalList.size() < 5) {
            int count = 5 - finalList.size();
            List<String> strings = new ArrayList<>(3);
            strings.add(firstLabel);
            strings.add(secondLabel);
            strings.add(thirdLabel);
            List<Job> list3 = null;
            if (isThird) {
                list3 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).ne(Job::getId, jobId).ne(Job::getJobType, strings.toString()).in(Job::getCompanyId, companyIds).last(" limit " + count));
            } else {
                list3 = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getStatus, 1).ne(Job::getJobType, strings.toString()).in(Job::getCompanyId, companyIds).last(" limit " + count));
            }
            finalList.addAll(list3);
        }
        List<JobRelationVo> jobRelationVos = new ArrayList<>(finalList.size());
        for (Job job : finalList) {
            JobRelationVo jobRelationVo = new JobRelationVo();
            Company company = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, job.getCompanyId()).select(Company::getId, Company::getAvatar, Company::getShortName));
            BeanUtils.copyProperties(job, jobRelationVo);
            jobRelationVo.setJobId(job.getId());
            jobRelationVo.setAvatar(company.getAvatar());
            jobRelationVo.setCompanyId(company.getId());
            jobRelationVo.setCompanyName(company.getShortName());
            jobRelationVos.add(jobRelationVo);
        }
        //其他的
        return RList.ok().message(null).data(jobRelationVos);
    }


    /**
     * 获取首页公司详情
     * @param id
     * @return
     */
    @Override
    public R getCompanyDetail(Long id) {
        Company company = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, id));

        int count = jobService.count(Wrappers.<Job>lambdaQuery().eq(Job::getCompanyId, id).eq(Job::getStatus, 1));

        List<Long> list = jobService.list(new QueryWrapper<Job>().eq("company_id", id).eq("status", 1).select("distinct user_id")).stream().map(Job::getUserId).collect(Collectors.toList());

        CompanyDetailVo companyDetailVo = new CompanyDetailVo();
        BeanUtils.copyProperties(company, companyDetailVo);
        companyDetailVo.setHrCount((long) list.size());
        companyDetailVo.setJobCount((long) count);
        return R.ok().message(null).data("data", companyDetailVo);
    }


    /**
     * 获取 首页下面 的全部公司类别
     * @param id
     * @return
     */
    @Override
    public RList getCompanyHasType(Long id) {
        List<List<String>> job_type = jobService.list(new QueryWrapper<Job>()
                        .eq("status", 1)
                        .eq("company_id", id)
                        .select("distinct Job_type"))
                .stream().map(Job::getJobType).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for (List<String> stringList : job_type) {
            if (!list.contains(stringList.get(1))) {
                list.add(stringList.get(1));
            }
        }
        return RList.ok().message(null).data(list);
    }

    /**
     * 获取 首页下面 的全部职位类别
     * @param
     * @return
     */
    @Override
    public RList getJobTypeList() {
        List<Long> activeCompanyIds = userClient.getActiveCompany();
        List<String> companyIds = companyService.list(Wrappers.<Company>lambdaQuery().in(Company::getUserId, activeCompanyIds).select(Company::getId)).stream().map(Company::getId).collect(Collectors.toList());
        List<List<String>> job_type = jobService.list(new QueryWrapper<Job>().eq("status", 1).select("distinct Job_type").in("company_id", companyIds)).stream().map(Job::getJobType).collect(Collectors.toList());
        List<String> list = new ArrayList<>();

        for (List<String> stringList : job_type) {
            if (!list.contains(stringList.get(1))) {
                list.add(stringList.get(1));
            }
        }

        return RList.ok().message(null).data(list);
    }


    /**
     *获取公司详情下的职位列表
     * @param jobTypeVo
     * @return
     */
    @Override
    public R getCompanyDetailJobList(JobTypeVo jobTypeVo) {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(jobTypeVo, pageVo);
        String type = jobTypeVo.getType();
        String companyId = jobTypeVo.getCompanyId();
        return this.getJobs(pageVo, type, null, companyId);
    }


    /**
     * 获取职位详情
     * @param id
     * @param authorization
     * @return
     */
    @Override
    public R getJobDetailById(Long id, String authorization) {

        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);

        Job job = jobService.getOne(Wrappers.<Job>lambdaQuery().eq(Job::getId, id));
        Company company = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, job.getCompanyId()).select(Company::getShortName, Company::getAvatar, Company::getSize, Company::getDesc, Company::getGovUrl, Company::getLinkman));

        UserJob userJob = userJobService.getOne(Wrappers.<UserJob>lambdaQuery().eq(UserJob::getUserId, userId).eq(UserJob::getJobId,id));

        JobDetailVo jobDetailVo = new JobDetailVo();
        if (!Objects.isNull(userJob)) {
            jobDetailVo.setIsSend(true);
        } else {
            jobDetailVo.setIsSend(false);
        }
        jobDetailVo.setAvatar(company.getAvatar());
        jobDetailVo.setSize(company.getSize());
        jobDetailVo.setHrId(job.getUserId());
        jobDetailVo.setCompanyName(company.getShortName());
        jobDetailVo.setDesc(company.getDesc());
        jobDetailVo.setGovUrl(company.getGovUrl());
        jobDetailVo.setLinkMan(company.getLinkman());
        BeanUtils.copyProperties(job, jobDetailVo);
        return R.ok().data("data", jobDetailVo).message(null);
    }


    /**
     * 把简历投递到职位
     * @param id
     * @param userId
     * @return
     */
    @Override
    public R addResumeToJob(Long id, Long userId) {

        UserResume resume = userResumeService.getOne(Wrappers.<UserResume>lambdaQuery().eq(UserResume::getUserId, userId));
        if (Objects.isNull(resume)) {
            return R.error().message(null);
        }
        Job job = jobService.getOne(Wrappers.<Job>lambdaQuery().eq(Job::getId, id));

        List<ResumeInfo> resume1 = job.getResume();
        if (Objects.isNull(resume1)) {
            resume1 = new ArrayList<>();
        }
        ResumeInfo resumeInfo = new ResumeInfo();
        resumeInfo.setFileName(resume.getResumeName());
        resumeInfo.setUrl(resume.getResumeUrl());
        resume1.add(resumeInfo);
        job.setResume(resume1);
        boolean b = jobService.updateById(job);
        if (b) {
            UserJob userJob = new UserJob();
            userJob.setJobId(job.getId());
            userJob.setUserId(userId);
            userJobService.save(userJob);
            return R.ok().message("投递成功");
        } else {
            return R.error().message("投递失败");
        }
    }


    /**
     * 上传简历
     * @param file
     * @param authorization
     * @return
     */
    @Override
    public R uploadResume(MultipartFile file, String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        String url = ossClient.uploadUrl(file);
        //先删除 后保存
        userResumeService.remove(Wrappers.<UserResume>lambdaQuery().eq(UserResume::getUserId, userId));
        UserResume userResume = new UserResume();
        userResume.setUserId(userId);
        userResume.setResumeUrl(url);
        String filename = file.getOriginalFilename();
        userResume.setResumeName(filename);
        boolean save = userResumeService.save(userResume);
        if (save) {
            return R.ok().message("保存简历成功");
        } else {
            return R.error().message(null);
        }
    }

    /**
     * 注册IM user用户
     *
     * @param registerToChat
     * @return
     */
    @Override
    public R registerUserToIM(RegisterToChat registerToChat) {
        Long toId = registerToChat.getToId();
        /**
         * 先查询是否有注册过
         */
        String host = "https://console.tim.qq.com";
        String path = "/v4/im_open_login_svc/account_check";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("sdkappid", "1400805337");
        querys.put("identifier", "administrator");
        querys.put("usersig", "eJwtzF0LgjAYBeD-suuI19nUhC6ElJLow6Jod8KmvYZrbCOE6L9n6uV5zuF8yGV3nr*lITGhcyCzIaOQymGFA5eiRYXWmdK9zDSw4llqjYLE3gIgAub74djITqORvTPGKACM6rD9WxB5LABK2fSCdf*fca47dqfZifNtUNyUVx1yq0I-v6bLpNkc0*SxL*p1k9gV*f4ARrY0Mw__");
        querys.put("random", toId + "");
        querys.put("contenttype", "json");
        querys.put("smsSignId", "5b813ab5b78c4e77baff86046581b3f9");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, Object> bodys = new HashMap<String, Object>();
        List<Map> list = new ArrayList<>();
        Map<String, String> userIds = new HashMap<>();
        userIds.put("UserID", toId + "");
        list.add(userIds);
        bodys.put("CheckItem", list);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, JSON.toJSONString(bodys));
            IMRegisterBody imRegisterBody = JSON.parseObject(EntityUtils.toString(response.getEntity()), IMRegisterBody.class);
            List<ResultItem> resultItem = imRegisterBody.getResultItem();
            ResultItem resultItem1 = resultItem.get(0);

            /**
             * 判读是否有导入
             * 没有 导入进行注册并修改资料
             *
             */
            if ("NotImported".equals(resultItem1.getAccountStatus())) {
                UserVo userInfo = userClient.getUserInfo(toId);
                String path2 = "/v4/im_open_login_svc/account_import";
                Map<String, Object> bodys2 = new HashMap<String, Object>();
                bodys2.put("UserID", toId + "");
                /**
                 * 注册
                 */
                HttpUtils.doPost(host, path2, method, headers, querys, JSON.toJSONString(bodys2));
                String path3 = "/v4/profile/portrait_set";
                Map<String, Object> bodys3 = new HashMap<String, Object>();
                List<Map> list2 = new ArrayList<>();
                Map<String, Object> profileItem = new HashMap<>();
                profileItem.put("Tag", "Tag_Profile_IM_Nick");
                if (!Objects.isNull(userInfo.getUserName())) {
                    profileItem.put("value", userInfo.getUserName());
                } else {
                    profileItem.put("value", userInfo.getNickName());
                }
                list2.add(profileItem);
                bodys3.put("From_Account", toId + "");
                bodys3.put("ProfileItem", list2);
                /**
                 * 修改资料
                 */
                HttpUtils.doPost(host, path3, method, headers, querys, JSON.toJSONString(bodys3));
                return R.ok().message(null);
            } else if ("Imported".equals(resultItem1.getAccountStatus())) {
                return R.ok().message(null);
            }
            return R.ok().message(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断是否是第一次谈论
     * @param userJobChat
     * @return
     */
    @Override
    public R checkIsChat(UserJobChat userJobChat) {
        Long userId = userJobChat.getUserId();
        String jobId = userJobChat.getJobId();
        UserJobChat one = userJobChatService.getOne(Wrappers.<UserJobChat>lambdaQuery().eq(UserJobChat::getUserId, userId).eq(UserJobChat::getJobId, jobId));
        if(Objects.isNull(one)){
            return R.ok().message(null);
        }else{
            return R.error().message(null);
        }
    }

    /**
     * 根据岗位名进行模糊搜索
     * @param map
     * @return
     */
    @Override
    public RList getSearch(Map map) {
      String text = (String) map.get("text");
      List<Job> list = jobService.list(Wrappers.<Job>lambdaQuery().like(Job::getJobName, text).last("limit 10").select(Job::getJobType, Job::getId, Job::getStartMoney,Job::getEndMoney,Job::getMoneyMonth,Job::getJobName));
      return RList.ok().message(null).data(list);
    }


    /**
     * 保存沟通记录
     * @param userJobChat
     * @return
     */
    @Override
    public R saveUserJobChat(UserJobChat userJobChat) {
        boolean save = userJobChatService.save(userJobChat);
        return R.ok().message(null);
    }


    /**
     * 获取本人保存的简历
     * @param authorization
     * @return
     */
    @Override
    public R getResumeByToken(String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        UserResume userResume = userResumeService.getOne(Wrappers.<UserResume>lambdaQuery().eq(UserResume::getUserId, userId));
        if (Objects.isNull(userResume)) {
            return R.error().message(null);
        } else {
            return R.ok().message(null).data("url", userResume.getResumeUrl());
        }
    }


    /**
     * 获取沟通过的职位列表
     * @param pageVo
     * @param authorization
     * @return
     */
    @Override
    public R getChattingJobs(PageVo pageVo, String authorization) {

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

        Page<UserJobChat> page = new Page<>(currentPage, pageSize);

        userJobChatService.page(page, Wrappers.<UserJobChat>lambdaQuery().eq(UserJobChat::getUserId, userId));
        List<UserJobChat> jobs = page.getRecords();
        List<JobListVo> jobListVos = new ArrayList<>();
        if (jobs.size() != 0) {
            List<String> jobIds = jobs.stream().map(UserJobChat::getJobId).collect(Collectors.toList());
            for (String jobId : jobIds) {
                Job job = jobService.getOne(Wrappers.<Job>lambdaQuery().eq(Job::getId, jobId));
                Company company = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, job.getCompanyId()));
                JobListVo jobListVo = new JobListVo();
                BeanUtils.copyProperties(job, jobListVo);
                jobListVo.setWeal(company.getWeal());
                jobListVo.setJobId(jobId);
                jobListVo.setCompanyId(job.getCompanyId());
                jobListVo.setAvatar(company.getAvatar());
                jobListVo.setCategory(company.getCategory());
                jobListVo.setCompanyName(company.getShortName());
                jobListVo.setSize(company.getSize());
                jobListVo.setLevel(company.getLevel());
                jobListVos.add(jobListVo);
            }
        }
        return R.ok().message(null).data("records", jobListVos).data("totalCount", page.getTotal());
    }

    /**
     * 获取手机端的职位分类列表
     *
     * @return
     */
    @Override
    public RList getPhoneJobType() {
        List<Long> activeCompanyIds = userClient.getActiveCompany();
        List<String> companyIds = companyService.list(Wrappers.<Company>lambdaQuery()
                        .in(Company::getUserId, activeCompanyIds)
                        .select(Company::getId))
                .stream()
                .map(Company::getId)
                .collect(Collectors.toList());
        List<List<String>> job_type = jobService.list(new QueryWrapper<Job>()
                        .eq("status", 1)
                        .select("distinct Job_type")
                        .in("company_id", companyIds))
                .stream()
                .map(Job::getJobType)
                .collect(Collectors.toList());

        List<JobPhoneTypeVo> list = new ArrayList<>();
        for (List<String> stringList : job_type) {
            JobPhoneTypeVo jobPhoneTypeVo = new JobPhoneTypeVo();
            jobPhoneTypeVo.setId(stringList.get(0));
            jobPhoneTypeVo.setText(stringList.get(0));
            jobPhoneTypeVo.setChildren(new ArrayList<JobPhoneTypeVo>());
            if (!list.contains(jobPhoneTypeVo)) {
                list.add(jobPhoneTypeVo);
            }
        }

        for (List<String> stringList : job_type) {
            String str = stringList.get(0);
            String str2 = stringList.get(1);
            for (JobPhoneTypeVo jobPhoneTypeVo : list) {
                if (jobPhoneTypeVo.getText().equals(str)) {
                    JobPhoneTypeVo jobPhoneTypeVo1 = new JobPhoneTypeVo();
                    jobPhoneTypeVo1.setId(str2);
                    jobPhoneTypeVo1.setText(str2);
                    if (!jobPhoneTypeVo.getChildren().contains(jobPhoneTypeVo1)) {
                        jobPhoneTypeVo.getChildren().add(jobPhoneTypeVo1);
                    }
                }
            }
        }
        return RList.ok().message(null).data(list);
    }


    private R getCompanys(PageVo pageVo, String type) {
        List<Long> activeCompanyIds = userClient.getActiveCompany();
        Integer currentPage = pageVo.getCurrentPage();
        Integer pageSize = pageVo.getPageSize();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 6;
        }

        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", activeCompanyIds);
        queryWrapper.orderByDesc("watch_count");
        if (!Objects.isNull(type)) {
            queryWrapper.eq("category", type);
        }
        Page<Company> page = new Page<>(currentPage, pageSize);
        companyService.page(page, queryWrapper);
        List<Company> records = page.getRecords();
        List<CompanyListVo> companyListVos = new ArrayList<>();

        for (Company record : records) {
            CompanyListVo companyListVo = new CompanyListVo();
            List<Job> list = jobService.list(Wrappers.<Job>lambdaQuery().eq(Job::getCompanyId, record.getId()).last("limit 2"));
            List<JobWithCompanyVo> jobWithCompanyVos = new ArrayList<>();
            for (Job job : list) {
                JobWithCompanyVo jobWithCompanyVo = new JobWithCompanyVo();
                BeanUtils.copyProperties(job, jobWithCompanyVo);
                jobWithCompanyVo.setJobId(job.getId());
                jobWithCompanyVos.add(jobWithCompanyVo);
            }
            BeanUtils.copyProperties(record, companyListVo);
            companyListVo.setJobList(jobWithCompanyVos);
            companyListVo.setCompanyId(record.getId());
            companyListVos.add(companyListVo);
        }
        return R.ok().data("records", companyListVos).data("totalCount", page.getTotal()).message(null);
    }

    private R getJobs(PageVo pageVo, String type, String thirdType, String companyId) {
        List<Long> activeCompanyIds = userClient.getActiveCompany();
        Integer currentPage = pageVo.getCurrentPage();
        Integer pageSize = pageVo.getPageSize();
        //如果没有传则传设置默认currentPage=1 pageSize=10
        if (currentPage == null || currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 6;
        }
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();

        List<String> companyIds = companyService.list(Wrappers.<Company>lambdaQuery().in(Company::getUserId, activeCompanyIds).select(Company::getId)).stream().map(Company::getId).collect(Collectors.toList());

        if (Objects.isNull(companyId)) {
            queryWrapper.in("company_id", companyIds);
        } else {
            queryWrapper.eq("company_id", companyId);
        }

        queryWrapper.eq("status", 1);
        if (Objects.isNull(thirdType)) {
            queryWrapper.orderByDesc("watch_count", "id");
        }

        if (!Objects.isNull(type)) {
            queryWrapper.orderByDesc("id");
            List<List<String>> job_type = jobService.list(new QueryWrapper<Job>().select("distinct job_type")).stream().map(Job::getJobType).collect(Collectors.toList());
            List<String> list = new ArrayList<>();
            for (List<String> stringList : job_type) {
                if (stringList.get(1).equals(type)) {
                    list.add(JSON.toJSON(stringList).toString());
                }
            }
            if (list.size() != 0) {
                queryWrapper.in("job_type", list);
            } else {
                return R.ok().data("records", new ArrayList<>()).data("totalCount", 0).message(null);
            }
        }

        if (!Objects.isNull(thirdType)) {
            List<List<String>> job_type = jobService.list(new QueryWrapper<Job>().select("distinct job_type")).stream().map(Job::getJobType).collect(Collectors.toList());
            List<String> list = new ArrayList<>();
            for (List<String> stringList : job_type) {
                if (stringList.get(2).equals(thirdType)) {
                    list.add(JSON.toJSON(stringList).toString());
                }
            }
            if (list.size() != 0) {
                queryWrapper.in("job_type", list);
            } else {
                return R.ok().data("records", new ArrayList<>()).data("totalCount", 0).message(null);
            }
        }

        Page<Job> page = new Page<>(currentPage, pageSize);
        jobService.page(page, queryWrapper);
        List<Job> jobs = page.getRecords();

        List<JobListVo> jobListVos = new ArrayList<>();
        for (Job job : jobs) {
            Company company = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getId, job.getCompanyId()));
            JobListVo jobListVo = new JobListVo();
            BeanUtils.copyProperties(job, jobListVo);
            if (Objects.isNull(thirdType)) {
                jobListVo.setWeal(null);
            } else {
                jobListVo.setWeal(company.getWeal());
            }

            if (!Objects.isNull(companyId)) {
                jobListVo.setWeal(job.getWeal());
            }

            jobListVo.setJobId(job.getId());
            jobListVo.setCompanyId(job.getCompanyId());
            jobListVo.setAvatar(company.getAvatar());
            jobListVo.setCategory(company.getCategory());
            jobListVo.setCompanyName(company.getShortName());
            jobListVo.setSize(company.getSize());
            jobListVo.setLevel(company.getLevel());

            jobListVos.add(jobListVo);
        }
        return R.ok().message(null).data("records", jobListVos).data("totalCount", page.getTotal());
    }

}
