package com.hqk.recruitment.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("分页对象")
public class PageVo implements Serializable {

    @ApiModelProperty("当前页数")
    private Integer currentPage;

    @ApiModelProperty("当前页大小")
    private  Integer pageSize;
}
