package com.hqk.recruitment.client;


import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.MajorVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-common",contextId = "common-major")
@RequestMapping("/common/major")
public interface CommonMajorClient {

    @PostMapping("/add")
    public R addMajor(@RequestBody MajorVo majorVo);

    @DeleteMapping("/delete/{id}")
    public R deleteMajor(@PathVariable Long id);

    @DeleteMapping("/batchDelete")
    public R deleteMajors(@RequestBody List<Long> ids);

    @PutMapping("/update")
    public R updateMajor(@RequestBody MajorVo majorVo);

}
