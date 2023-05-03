package com.hqk.recruitment.sms.controller;


import com.hqk.recruitment.result.R;
import com.hqk.recruitment.sms.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Resource
    private SmsService smsService;


    @GetMapping("/{phone}")
    public R getPhoneSmsCode(@PathVariable String phone){
        boolean isSendCode=smsService.sendCode(phone);
        if(isSendCode){
            return R.ok().code(200).message("发送短信验证码成功");
        }else{
            return R.error().message("发送失败");
        }
    }
}
