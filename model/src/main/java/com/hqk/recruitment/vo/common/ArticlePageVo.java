package com.hqk.recruitment.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticlePageVo implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "榜单id")
    private Integer typedId;

    @ApiModelProperty(value = "类型id")
    private Integer categoryId;

    @ApiModelProperty(value = "其他id")
    private Integer otherId;

    @ApiModelProperty(value = "文章浏览量")
    private Long watchCount;


    @ApiModelProperty("当前页数")
    private  Integer currentPage;

    @ApiModelProperty("当前页大小")
    private  Integer pageSize;

}
