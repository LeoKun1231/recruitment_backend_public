package com.hqk.recruitment.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BossVo {


    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "帐号")
    private String account;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "状态")
    private Long status;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @ApiModelProperty(value = "审核失败原因")
    private String reason;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否启用")
    private Integer active;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
