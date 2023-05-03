package com.hqk.recruitment.user.controller;


import com.hqk.recruitment.result.R;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.vo.user.EditUserVo;
import com.hqk.recruitment.vo.user.UserVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/acl/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public UserVo getUserInfo(@PathVariable Long id){
       return userService.getUserInfoById(id);
    }

    @DeleteMapping("/delete/{id}")
    public R deleteUserById(@PathVariable Long id){
        boolean b = userService.removeById(id);
        if(b){
            return R.ok().message("删除用户成功");
        }else{
            return R.error().message("删除用户失败");
        }
    }

    @DeleteMapping("/batchDelete")
    public R batchDeleteUser(@RequestBody List<Long> ids){
        boolean b = userService.deleteUserByIds(ids);
        if(b){
            return R.ok().message("删除用户成功");
        }else{
            return R.error().message("删除用户失败");
        }
    }

    @PutMapping("/edit")
    public R editUser(@RequestBody EditUserVo editUserVo, @RequestHeader("Authorization") String authorization){
     return userService.editUser(editUserVo,authorization);
    }

    @PostMapping("/updateAvatar")
    public R updateAvatar(MultipartFile file,@RequestHeader("Authorization") String authorization){
        return userService.updateAvatar(file,authorization);
    }

    @PostMapping("/updateTempAvatar")
    public R updateTempAvatar(MultipartFile file,@RequestHeader("Authorization") String authorization){
        return userService.updateTempAvatar(file,authorization);
    }


}

