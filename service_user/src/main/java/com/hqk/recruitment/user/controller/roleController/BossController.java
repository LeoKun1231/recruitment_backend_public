package com.hqk.recruitment.user.controller.roleController;


import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.vo.user.BossQueryVo;
import com.hqk.recruitment.vo.user.BossUpdateVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/acl/boss")
public class BossController {

    @Resource
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public R addBoss(@RequestBody BossUpdateVo bossVo){
        return userService.addBoss(bossVo);
    }

    @PostMapping("/list")
    public R getBossList(@RequestBody BossQueryVo bossQueryVo){
        return userService.getBossList(bossQueryVo);
    }

    @PutMapping("/update")
    public R updateUser(@RequestBody BossUpdateVo bossVo){
        return userService.updateBoss(bossVo);
    }

    @PutMapping("/changeActive/{id}")
    public R changeActive(@PathVariable("id") Long id){
        return userService.changeActive(id);
    }

    @PutMapping("/closeCompany/{id}")
    public boolean closeCompany(@PathVariable("id") Long id){
        return userService.closeCompany(id);
    }

    @GetMapping("/changeStatus/{id}")
    public boolean changeCompanyStatus(@PathVariable("id") Long id){
        return userService.changeCompanyStatus(id);
    }

    @GetMapping("/status/{id}")
    public R getStatus(@PathVariable("id") Long id){
        return userService.getStatus(id);
    }

    @PostMapping("/check")
    public R checkCompany(@RequestBody Map map){
        return userService.checkCompany(map);
    }

    @GetMapping("/getActiveCompany")
    public List<Long> getActiveCompany(){
        return userService.getActiveCompany();
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public R deleteBossById(@PathVariable Long id){
        boolean b = userService.removeById(id);
        if(b){
            return R.ok().message("删除公司成功");
        }else{
            return R.error().message("删除公司失败");
        }
    }

    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasRole('admin')")
    public R batchDeleteBoss(@RequestBody List<Long> ids){
        boolean b = userService.deleteUserByIds(ids);
        if(b){
            return R.ok().message("删除公司成功");
        }else{
            return R.error().message("删除公司失败");
        }
    }
}
