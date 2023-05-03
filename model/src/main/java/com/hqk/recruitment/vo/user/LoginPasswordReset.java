package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("密码重置")
@Data
public class LoginPasswordReset implements Serializable {
    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "密码")
    private String password;

}
