package com.hqk.recruitment.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

    @ApiModelProperty(value = "组织id")
    private Long majorId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "公司Id")
    private String CompanyId;

    @ApiModelProperty("头像")
    private String avatar;
}
