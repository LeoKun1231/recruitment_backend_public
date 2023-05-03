package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordResetByPassword {
    @ApiModelProperty(value = "新密码")
    private String oldPassword;

    @ApiModelProperty(value = "旧密码")
    private String newPassword;
}
