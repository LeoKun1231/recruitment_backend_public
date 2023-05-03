package com.hqk.recruitment.user.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
@ApiModel(description = "用户实体类")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "id")
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@ApiModelProperty(value = "帐号")
	private String account;

	@ApiModelProperty(value = "手机号")
	private String telephone;

    @ApiModelProperty(value = "真实姓名")
    private String userName;

	@ApiModelProperty(value = "密码")
	private String password;

}



