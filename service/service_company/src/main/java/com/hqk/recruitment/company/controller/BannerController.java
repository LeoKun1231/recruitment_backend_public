package com.hqk.recruitment.company.controller;


import com.hqk.recruitment.company.service.BannerService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.vo.company.BannerQueryVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/company/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;


    @PostMapping("/admin/list")
    public R getAdminBannerList(@RequestBody BannerQueryVo bannerQueryVo){
        return  bannerService.getAdminBannerList(bannerQueryVo);
    }

    @GetMapping("/home/list")
    public RList getHomeBannerList(){
        return bannerService.getHomeBannerList();
    }

    @PostMapping("/admin/add")
    public R addBanner(@RequestBody Map map, @RequestHeader("Authorization") String authorization){
        return bannerService.addBanner(map,authorization);
    }

    @PostMapping("/upload")
    public R uploadBanner(MultipartFile file, @RequestHeader("Authorization") String authorization){
        return bannerService.upload(file,authorization);
    }

    @GetMapping("/remove")
    public R uploadBanner( @RequestHeader("Authorization") String authorization){
        return bannerService.removeUpload(authorization);
    }


    @GetMapping("/detail")
    public R getBannerDetail( @RequestHeader("Authorization") String authorization){
        return bannerService.getBannerDetail(authorization);
    }

    @GetMapping("/changeStatus/{id}")
    public R changeStatus(@PathVariable("id") Long id ){
        return bannerService.changeStatus(id);
    }

}

