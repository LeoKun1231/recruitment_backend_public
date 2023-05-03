package com.hqk.recruitment.vo.user;

import com.hqk.recruitment.vo.base.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BossQueryVo extends PageVo {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value="是否启用")
    private Integer active;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "状态")
    private Long status;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;

    @ApiModelProperty(value = "更新时间")
    private List<String> updateTime;
}
