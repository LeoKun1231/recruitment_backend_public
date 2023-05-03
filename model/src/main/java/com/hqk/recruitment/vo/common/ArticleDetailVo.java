package com.hqk.recruitment.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleDetailVo implements Serializable {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "文章评论数")
    private Long commentCount;

    @ApiModelProperty(value = "文章浏览量")
    private Long watchCount;

    @ApiModelProperty(value = "文章点赞量")
    private Long likeCount;

    @ApiModelProperty(value = "文章是否点赞")
    private Boolean isLike;

    @ApiModelProperty(value = "文章类型")
    private String type;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户名称")
    private String nickname;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

    @ApiModelProperty(value = "话题内容")
    private String topicContent;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
