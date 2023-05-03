package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description="手机登录")
public class LoginPhnoeVo implements Serializable {

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "验证码")
    private String code;
}
