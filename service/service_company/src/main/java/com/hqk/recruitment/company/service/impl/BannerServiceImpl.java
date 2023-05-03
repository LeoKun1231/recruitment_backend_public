package com.hqk.recruitment.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.client.OssClient;
import com.hqk.recruitment.company.service.CompanyService;
import com.hqk.recruitment.model.company.Banner;
import com.hqk.recruitment.company.mapper.BannerMapper;
import com.hqk.recruitment.company.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.company.Company;
import com.hqk.recruitment.model.user.User;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.company.BannerQueryVo;
import com.hqk.recruitment.vo.user.BossVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-15
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    private OssClient ossClient;

    @Resource
    private CompanyService companyService;

    @Override
    public R getAdminBannerList(BannerQueryVo bannerQueryVo) {
        Integer currentPage = bannerQueryVo.getCurrentPage();
        Integer pageSize = bannerQueryVo.getPageSize();

        //如果没有传则传设置默认currentPage=1 pageSize=10
        if(currentPage==null || currentPage<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize==0){
            pageSize=10;
        }
        Page<Banner> page=new Page<>(currentPage,pageSize);
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();

        Boolean status = bannerQueryVo.getStatus();

        if(!Objects.isNull(status)){
            queryWrapper.eq("status",status);
        }

        if(!StringUtils.isEmpty(bannerQueryVo.getCreator())){
            queryWrapper.like("creator",bannerQueryVo.getCreator());
        }

        if(!StringUtils.isEmpty(bannerQueryVo.getGovUrl())){
            queryWrapper.like("gov_url",bannerQueryVo.getGovUrl());
        }

        if(!Objects.isNull(bannerQueryVo.getCreateTime())){
            List<String> createTime = bannerQueryVo.getCreateTime();
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
        if(!Objects.isNull(bannerQueryVo.getUpdateTime())){
            List<String> updateTime = bannerQueryVo.getUpdateTime();
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
        List<Banner> records = this.page(page, queryWrapper).getRecords();
        return R.ok().message(null).data("records",records).data("totalCount",page.getTotal());
    }

    @Override
    public RList getHomeBannerList() {
        List<Banner> list = this.list(Wrappers.<Banner>lambdaQuery().eq(Banner::getStatus, true).select(Banner::getImgUrl,Banner::getGovUrl));
        return RList.ok().message(null).data(list);
    }

    @Override
    public R addBanner(Map map,String authorization) {
        String url = (String) map.get("govUrl");
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Banner banner = this.getOne(Wrappers.<Banner>lambdaQuery().eq(Banner::getUserId, userId));

        banner.setGovUrl(url);

        boolean b = this.updateById(banner);

        if(b){
            return R.ok().message("提交成功");
        }else{
            return R.error().message("提交失败");
        }
    }

    @Override
    public R upload(MultipartFile file, String authorization) {

        String s = ossClient.uploadUrl(file);
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        String shortName = companyService.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getUserId, userId).select(Company::getShortName)).getShortName();

        Banner one = this.getOne(Wrappers.<Banner>lambdaQuery().eq(Banner::getUserId, userId));
        if(Objects.isNull(one)){
            Banner banner = new Banner();
            banner.setCreator(shortName);
            banner.setImgUrl(s);
            banner.setUserId(userId);

            boolean save = this.save(banner);

            if(save){
                return R.ok().message("上传成功");
            }else{
                return R.error().message("上传失败");
            }
        }else{
            one.setImgUrl(s);
            boolean b = this.updateById(one);
            if(b){
                return R.ok().message("上传成功");
            }else{
                return R.error().message("上传失败");
            }
        }
    }

    @Override
    public R removeUpload(String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Banner banner = this.getOne(Wrappers.<Banner>lambdaQuery().eq(Banner::getUserId, userId));
        banner.setImgUrl(null);
        boolean b = this.updateById(banner);
        if(b){
            return R.ok().message("移除成功");
        }else{
            return R.error().message("移除失败");
        }

    }

    @Override
    public R getBannerDetail(String authorization) {
        String token = JwtHelper.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Banner banner = this.getOne(Wrappers.<Banner>lambdaQuery().eq(Banner::getUserId, userId));
        return R.ok().message(null).data("data",banner);
    }

    @Override
    public R changeStatus(Long id) {
        Banner banner = this.getOne(Wrappers.<Banner>lambdaQuery().eq(Banner::getId, id));

        banner.setStatus(!banner.getStatus());
        boolean b = this.updateById(banner);
        if(b){
            return R.ok().message(null);
        }else{
            return R.error().message("下架失败");
        }
    }
}
