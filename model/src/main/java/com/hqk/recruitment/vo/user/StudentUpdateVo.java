package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StudentUpdateVo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "专业Id")
    private Long majorId;

    @ApiModelProperty(value = "密码")
    private String password;
}
