package com.hqk.recruitment.company.service;

import com.hqk.recruitment.model.company.Company;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.company.CompanyVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-08
 */
public interface CompanyService extends IService<Company> {

    R uploadAvatar(MultipartFile file, String authorization);

    R uploadCompanyAndCertifyImages(MultipartFile file, String authorization,boolean isCompany);

    R removeCompanyAndCertifyImage(Map map, String authorization,boolean isCompany);

    R removeAvatar(Map map, String authorization);


    R removeAll(String authorization);

    R saveCompanyDetail(CompanyVo companyVo, String authorization);

    boolean init(Long id);

    R getCompanyDetailById(Long id);

}
