package com.hqk.recruitment.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hqk.recruitment.client.OssClient;
import com.hqk.recruitment.client.UserClient;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.company.Company;
import com.hqk.recruitment.company.mapper.CompanyMapper;
import com.hqk.recruitment.company.service.CompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.utils.JwtHelper;
import com.hqk.recruitment.vo.company.CompanyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-08
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Resource
    private OssClient ossClient;

    @Resource
    private UserClient userClient;

    @Override
    public R uploadAvatar(MultipartFile file, String authorization) {

        String token = this.getToken(authorization);
        //获取url
        String url = ossClient.uploadUrl(file);
        //获取userId
        Long userId = JwtHelper.getUserId(token);

        Company company = new Company();
        Company company1 = this.getOne(new QueryWrapper<Company>().eq("user_id", userId));
        if(!Objects.isNull(company1)){
            company1.setAvatar(url);
            this.updateById(company1);
            return R.ok().data("url",url).message(null);
        }else{
            company.setAvatar(url);
            company.setUserId(userId);
            this.save(company);
            return R.ok().data("url",url).message(null);
        }
    }

    @Override
    public R uploadCompanyAndCertifyImages(MultipartFile file, String authorization,boolean isCompany) {
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Company company = this.getOne(new QueryWrapper<Company>().eq("user_id", userId));
        String url = ossClient.uploadUrl(file);
        if(isCompany){
            List<String> companyUrl = company.getCompanyUrl();
            if(Objects.isNull(companyUrl)){
                companyUrl=new ArrayList<String>();
            }
            companyUrl.add(url);
            company.setCompanyUrl(companyUrl);
        }else{
            List<String> certifyUrl = company.getCertifyUrl();
            if(Objects.isNull(certifyUrl)){
                certifyUrl=new ArrayList<String>();
            }
            certifyUrl.add(url);
            company.setCertifyUrl(certifyUrl);
        }
        this.updateById(company);
        return R.ok().message(null).data("url",url);
    }

    @Override
    public R removeCompanyAndCertifyImage(Map map, String authorization,boolean isCompany) {
        String url = (String) map.get("url");
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Company company = this.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getUserId,userId));
        if(isCompany){
            List<String> companyUrl = company.getCompanyUrl();
            List<String> list = companyUrl.stream().filter(item -> !item.equals(url)).collect(Collectors.toList());
            company.setCompanyUrl(list);
        }else{
            List<String> certifyUrl = company.getCertifyUrl();
            List<String> list = certifyUrl.stream().filter(item -> !item.equals(url)).collect(Collectors.toList());
            company.setCertifyUrl(list);
        }
        boolean b = this.updateById(company);
        if(b){
            return R.ok().message(null);
        }else{
            return R.error().message(null);
        }
    }

    @Override
    public R removeAvatar(Map map, String authorization) {
        String url = (String) map.get("url");
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);

        boolean b = this.update(null, Wrappers.<Company>lambdaUpdate().set(Company::getAvatar,null).eq(Company::getUserId,userId));
        if(b){
            return R.ok().message(null);
        }else{
            return R.error().message(null);
        }
    }

    @Override
    public R removeAll(String authorization) {
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        boolean update = this.update(Wrappers.<Company>lambdaUpdate()
                .set(Company::getCompanyUrl, null)
                .set(Company::getCertifyUrl, null)
                .set(Company::getAvatar, null)
                .eq(Company::getUserId, userId));
        if(update){
            return R.ok().message(null);
        }else{
            return R.error().message("重置失败");
        }

    }

    @Override
    public R saveCompanyDetail(CompanyVo companyVo, String authorization) {
        String token = this.getToken(authorization);
        Long userId = JwtHelper.getUserId(token);
        Company company = new Company();
        BeanUtils.copyProperties(companyVo,company);

        boolean update = this.update(company, Wrappers.<Company>lambdaUpdate().eq(Company::getUserId, userId));
        //修该审核状态
        userClient.changeCompanyStatus(userId);
        //修改上线状态
        userClient.closeCompany(userId);
        if(update){
            return R.ok().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }
    }

    @Override
    public boolean init(Long id) {
        Company company = new Company();
        company.setUserId(id);
        return this.save(company);
    }

    @Override
    public String getCompanyNameByUserId(Long id) {
        Company company = this.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getUserId, id).select(Company::getShortName));
        if(Objects.isNull(company)){
            return null;
        }
        return company.getShortName();
    }

    @Override
    public R getCompanyDetailById(Long id) {
        Company company = this.getOne(Wrappers.<Company>lambdaQuery().eq(Company::getUserId, id));
        return R.ok().data("data",company).message(null);
    }


    String getToken(String authorization){
        if(authorization.length()<=7){
            throw new MyCustomException(20000,"token不存在");
        }
        String token= authorization.substring(7, authorization.length());
        boolean isNotExpire = JwtHelper.checkToken(token);
        if(!isNotExpire){
            throw new MyCustomException(20000,"token过期");
        }
        return token;
    }
}
