package com.hqk.recruitment.user.controller.roleController;

import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.vo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/acl/hr")
public class HRController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('boss')")
    public R addHR(@RequestBody HrUpdateVo hrUpdateVo,@RequestHeader("Authorization") String authorization){
        return userService.addHR(hrUpdateVo,authorization);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('boss')")
    public R updateHR(@RequestBody HrUpdateVo hrUpdateVo,@RequestHeader("Authorization") String authorization){
        return userService.updateHR(hrUpdateVo,authorization);
    }

    /**
     * 获取管理者列表
     * @param hrQueryVo
     * @return
     */
    @PostMapping("/list")
    public R getHRList(@RequestBody HrQueryVo hrQueryVo, @RequestHeader("Authorization") String authorization) {
        return userService.getHRList(hrQueryVo,authorization);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('boss')")
    public R deleteHRById(@PathVariable Long id){
        boolean b = userService.removeById(id);
        if(b){
            return R.ok().message("删除HR成功");
        }else{
            return R.error().message("删除HR失败");
        }
    }

    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasRole('boss')")
    public R batchDeleteHR(@RequestBody List<Long> ids){
        boolean b = userService.deleteUserByIds(ids);
        if(b){
            return R.ok().message("删除HR成功");
        }else{
            return R.error().message("删除HR失败");
        }
    }
}
