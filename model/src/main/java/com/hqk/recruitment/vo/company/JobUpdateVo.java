package com.hqk.recruitment.vo.company;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName(value = "job",autoResultMap = true)
public class JobUpdateVo {
    @ApiModelProperty(value = "id")
    private String id;

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
    private List<String>  city;

    @ApiModelProperty(value = "地址")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String>  address;

    @ApiModelProperty(value = "地址名称")
    private String addressName;


    @ApiModelProperty(value = "职位标签")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> tag;

    @ApiModelProperty(value = "职位福利")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> weal;

    @ApiModelProperty(value = "职位描述")
    private String jobDesc;

}
