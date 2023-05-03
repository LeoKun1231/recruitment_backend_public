package com.hqk.recruitment.user.controller.common;


import com.hqk.recruitment.client.CommonMajorClient;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.MajorVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/acl/common/major")
public class CommonMajorController {

    @Resource
    private CommonMajorClient commonMajorClient;

    @PostMapping("/add")
    public R addMajor(@RequestBody MajorVo majorVo){
        return commonMajorClient.addMajor(majorVo);
    };

    @DeleteMapping("/delete/{id}")
    public R deleteMajor(@PathVariable Long id){
        return commonMajorClient.deleteMajor(id);
    };

    @DeleteMapping("/batchDelete")
    public R deleteMajors(@RequestBody List<Long> ids){
        return commonMajorClient.deleteMajors(ids);
    };

    @PutMapping("/update")
    public R updateMajor(@RequestBody MajorVo majorVo){
        return commonMajorClient.updateMajor(majorVo);
    };
}
