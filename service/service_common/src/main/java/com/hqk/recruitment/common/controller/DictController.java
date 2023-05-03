package com.hqk.recruitment.common.controller;


import com.hqk.recruitment.common.entity.Dict;
import com.hqk.recruitment.common.service.DictService;
import com.hqk.recruitment.result.R;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.Cacheable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/common/dict")
public class DictController {

    @Resource
    DictService dictService;

    @GetMapping("/download")
    public void downloadExcel(HttpServletResponse httpServletResponse) throws IOException {
        dictService.download(httpServletResponse);
    }

    @GetMapping("/hello")
    public R hello(){
        return R.ok().message("hello world");
    }

    @PostMapping("/upload")
    public R upload(MultipartFile file){
        dictService.upload(file);
        return R.ok();
    }


    @GetMapping("/getData")
    public R getDictData(){
//        List<DictEeVo> allDictData = dictService.getAllDictData();
//        return R.ok().data("lists",'allDictData');
        return R.ok();
    }
}

