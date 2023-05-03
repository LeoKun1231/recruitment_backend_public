package com.hqk.recruitment.user.controller.roleController;

import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.vo.user.AdminQueryVo;
import com.hqk.recruitment.vo.user.AdminUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/acl/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
    public R addAdminUser(@RequestBody AdminUpdateVo adminVo){
        return userService.addAdmin(adminVo);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('admin')")
    public R updateUser(@RequestBody AdminUpdateVo adminUpdateVo){
        return userService.updateUser(adminUpdateVo);
    }

    /**
     * 获取管理者列表
     * @param adminQueryVo
     * @return
     */
    @PostMapping("/list")
    public R getUserList(@RequestBody AdminQueryVo adminQueryVo) throws ParseException {
        return userService.getAdminAndTeacherList(adminQueryVo);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public R deleteAdminAndTeacherById(@PathVariable Long id){
        boolean b = userService.removeById(id);
        if(b){
            return R.ok().message("删除用户成功");
        }else{
            return R.error().message("删除用户失败");
        }
    }

    @DeleteMapping("/batchDelete")
    @PreAuthorize("hasRole('admin')")
    public R batchDeleteAdminAndTeacher(@RequestBody List<Long> ids){
        boolean b = userService.deleteUserByIds(ids);
        if(b){
            return R.ok().message("删除用户成功");
        }else{
            return R.error().message("删除用户失败");
        }
    }
}
