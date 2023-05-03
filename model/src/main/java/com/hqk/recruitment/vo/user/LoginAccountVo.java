package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value = "帐号登录")
public class LoginAccountVo  implements Serializable {

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;
}
