package com.hqk.recruitment.model.company;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @since 2023-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Job对象", description="")
@TableName(value = "job",autoResultMap = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
      private String id;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "公司id")
    private String companyId;

    @ApiModelProperty(value = "职位名")
    private String jobName;

    @ApiModelProperty(value = "职位类别")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> jobType;

    @ApiModelProperty(value = "职位要求")
    private String jobRequire;

    @ApiModelProperty(value = "起始薪资")
    private Integer startMoney;

    @ApiModelProperty(value = "结束薪资")
    private Integer endMoney;

    @ApiModelProperty(value = "薪资月份")
    private Integer moneyMonth;

    @ApiModelProperty(value = "所在城市")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> city;

    @ApiModelProperty(value = "地址")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String>  address;

    @ApiModelProperty(value = "地址名称")
    private String addressName;

    @ApiModelProperty(value = "简历存储地址")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<ResumeInfo> resume;

    @ApiModelProperty(value = "浏览次数")
    private Long watchCount;

    @ApiModelProperty(value = "职位标签")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> tag;

    @ApiModelProperty(value = "职位福利")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> weal;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态")
    @JsonIgnore
    private Integer isDeleted;

    @ApiModelProperty(value = "职位描述")
    private String jobDesc;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "结束时间")
    private Date updateTime;

}
