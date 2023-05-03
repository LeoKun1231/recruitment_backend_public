package com.hqk.recruitment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "service-common",contextId = "common-article")
@RequestMapping("/common/article")
public interface CommonArticleClient {

    @GetMapping("/getMajorIds")
    public List<Long> getMajorIds();
}
