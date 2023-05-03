package com.hqk.recruitment.user.controller.login;


import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.user.LoginAccountVo;
import com.hqk.recruitment.vo.user.LoginPasswordReset;
import com.hqk.recruitment.vo.user.LoginPhnoeVo;
import com.hqk.recruitment.vo.user.PasswordResetByPassword;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/login")
public class LoginController {

    @Resource
    private UserService userService;

    @PostMapping("/phone")
    public R postPhoneLogin(@RequestBody LoginPhnoeVo loginPhnoeVo){
        Map<String,Object> loginByPhone=userService.loginByPhone(loginPhnoeVo);
        return R.ok().data(loginByPhone).message("登录成功！");
    }

    @PostMapping("/account")
    public R postAccountLogin(@RequestBody LoginAccountVo loginAccountVo){
        Map<String,Object> loginByAccount=userService.loginByAccount(loginAccountVo);
        return R.ok().data(loginByAccount).message("登录成功！");
    }

    @GetMapping("/checkPhone/{telephone}")
    public R checkPhone(@PathVariable String telephone){
        Boolean isExist= userService.checkPhone(telephone);
        if(isExist){
            return R.ok().message(null);
        }else{
            return R.error().code(20000).message("该手机号尚未绑定");
        }
    }

    @GetMapping("/checkPhoneNoMessage/{telephone}")
    public R checkPhoneNoMessage(@PathVariable String telephone){
        Boolean isExist= userService.checkPhone(telephone);
        if(isExist){
            return R.ok().message(null);
        }else{
            return R.error().code(20000).message(null);
        }
    }

    @PostMapping("/resetPassword")
    public R resetPassword(@RequestBody LoginPasswordReset loginPasswordReset) {
        Map<String, Object> map = userService.resetPassword(loginPasswordReset);
        return R.ok().data(map).message("修改密码成功！");
    };

    @PostMapping("/resetPassword/password")
    public R resetPasswordByPassword(@RequestBody PasswordResetByPassword password, @RequestHeader("Authorization") String authorization){
        return userService.resetPasswordByPassword(password,authorization);
    }


    @PostMapping("/resetPassword/telephone")
    public R resetPasswordByTelephone(@RequestBody LoginPasswordReset loginPasswordReset, @RequestHeader("Authorization") String authorization) {
        return userService.resetPasswordByTelephone(loginPasswordReset,authorization);
    };

    @GetMapping("/admin/checkPhone/{telephone}")
    public R checkPhoneForAdmin(@PathVariable String telephone){
        Boolean isExist= userService.checkPhone(telephone);
        if(isExist){
            return R.error().message("该手机号已经绑定");
        }else{
            return R.ok().message(null);
        }
    }
}
