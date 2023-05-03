package com.hqk.recruitment.oss.controller;

import com.hqk.recruitment.oss.service.OssService;
import com.hqk.recruitment.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("/upload")
    public R upload(MultipartFile file){
        return  ossService.upload(file);
    }

    @PostMapping("/uploadUrl")
    public String uploadUrl(MultipartFile file){
        return ossService.uploadReturnUrl(file);
    }
}
