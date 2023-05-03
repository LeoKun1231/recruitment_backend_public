package com.hqk.recruitment.model.company;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Company对象", description="")
@TableName(value = "company",autoResultMap = true)
public class Company implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
      private String id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty(value = "简称")
    private String shortName;

    @ApiModelProperty(value = "全称")
    private String fullName;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @ApiModelProperty(value = "所在城市")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> city;

    @ApiModelProperty(value = "企业规模")
    private String size;

    @ApiModelProperty(value = "企业类别")
    private String category;

    @ApiModelProperty(value = "融资阶段")
    private String level;

    @ApiModelProperty(value = "公司性质")
    @TableField("companyType")
    private String companyType;

    @ApiModelProperty(value = "公司官网")
    private String govUrl;

    @ApiModelProperty(value = "公司福利")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> weal;

    @ApiModelProperty(value = "公司描述")
    @TableField("`desc`")
    private String desc;

    @ApiModelProperty(value = "公司头像")
    private String avatar;

    @ApiModelProperty(value = "浏览次数")
    private Long watchCount;

    @ApiModelProperty(value = "公司地址")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> address;
    private String addressName;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "公司照片")
    private List<String> companyUrl;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "公司证明材料")
    private List<String> certifyUrl;

    @ApiModelProperty(value = "逻辑删除")
    @JsonIgnore
    private Boolean isDeleted;

}
