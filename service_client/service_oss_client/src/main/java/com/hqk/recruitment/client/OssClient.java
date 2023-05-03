package com.hqk.recruitment.client;

import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-oss")
@RequestMapping(("/oss"))
public interface OssClient {

    @PostMapping(value = "/uploadUrl",consumes = "multipart/form-data")
    public String uploadUrl(MultipartFile file);

}
