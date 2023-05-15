package com.hqk.recruitment.company.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hqk.recruitment.company.service.CompanyService;
import com.hqk.recruitment.model.company.Company;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.company.CompanyVo;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/company/")
public class CompanyController {


    @Resource
    private CompanyService companyService;


    @PostMapping("/saveDetail")
    public R saveCompanyDetail(@RequestBody CompanyVo companyVo,@RequestHeader("Authorization") String authorization){
        return companyService.saveCompanyDetail(companyVo,authorization);
    }

    @PostMapping("/upload/avatar")
    public R UploadAvatar(MultipartFile file,@RequestHeader("Authorization") String authorization){
        return companyService.uploadAvatar(file,authorization);
    }


    @PostMapping("/upload/companyImages")
    public R uploadCompanyImages(MultipartFile file,@RequestHeader("Authorization") String authorization){
        return companyService.uploadCompanyAndCertifyImages(file,authorization,true);
    }

    @PostMapping("/upload/certifyImages")
    public R uploadCertifyImages(MultipartFile file,@RequestHeader("Authorization") String authorization){
        return companyService.uploadCompanyAndCertifyImages(file,authorization,false);
    }

    @GetMapping("/{id}")
    public R getCompanyDetailById(@PathVariable("id") Long id){
        return companyService.getCompanyDetailById(id);
    }

    @DeleteMapping("/remove/companyImages")
    public R remove(@RequestBody Map map, @RequestHeader("Authorization") String authorization){
        return companyService.removeCompanyAndCertifyImage(map,authorization,true);
    }

    @DeleteMapping("/remove/certifyImages")
    public R removeCertifyImages(@RequestBody Map map, @RequestHeader("Authorization") String authorization){
        return companyService.removeCompanyAndCertifyImage(map,authorization,false);
    }

    @DeleteMapping("/remove/avatar")
    public R removeAvatar(@RequestBody Map map, @RequestHeader("Authorization") String authorization){
        return companyService.removeAvatar(map,authorization);
    }

    @GetMapping("/removeAll")
    public  R removeAll(@RequestHeader("Authorization") String authorization){
        return companyService.removeAll(authorization);
    }

    @GetMapping("/init/{id}")
    public boolean initCompany(@PathVariable("id") Long id){
        return companyService.init(id);
    }


    @DeleteMapping("/company/delete/{id}")
    public boolean deleteCompanyByUserId(@PathVariable("id") Long id){
        return companyService.remove(Wrappers.<Company>lambdaQuery().eq(Company::getUserId,id));
    }

}

