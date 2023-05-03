package com.hqk.recruitment.company.service;

import com.hqk.recruitment.model.company.Banner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.vo.company.BannerQueryVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-15
 */
public interface BannerService extends IService<Banner> {

    R getAdminBannerList(BannerQueryVo bannerQueryVo);

    RList getHomeBannerList();

    R addBanner(Map map,String authorization);

    R upload(MultipartFile file, String authorization);

    R removeUpload(String authorization);

    R getBannerDetail(String authorization);

    R changeStatus(Long id);

}
