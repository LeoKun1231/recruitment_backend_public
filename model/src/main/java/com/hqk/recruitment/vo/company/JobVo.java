package com.hqk.recruitment.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.hqk.recruitment.model.company.ResumeInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JobVo {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "职位名")
    private String jobName;

    @ApiModelProperty(value = "职位类别")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> jobType;

    @ApiModelProperty(value = "职位要求")
    private String jobRequire;

    @ApiModelProperty(value = "薪资")
    private String money;

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

    @ApiModelProperty(value = "职位标签")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> tag;

    @ApiModelProperty(value = "职位福利")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> weal;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "结束时间")
    private Date updateTime;

}
