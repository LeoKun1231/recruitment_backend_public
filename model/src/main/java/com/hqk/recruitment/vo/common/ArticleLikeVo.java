package com.hqk.recruitment.vo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ArticleLikeVo implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "文章预览内容")
    private String contentPreview;

    @ApiModelProperty(value = "文章评论数")
    private Long commentCount;

    @ApiModelProperty(value = "文章浏览量")
    private Long watchCount;

    @ApiModelProperty(value = "文章类型")
    private String type;


    @ApiModelProperty(value = "专业类型")
    private String majorName;


    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "组织id")
    private Long majorId;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户名称")
    private String nickname;

    @ApiModelProperty(value = "赞数")
    private Long likeCount;

    @ApiModelProperty(value = "是否已赞")
    private Boolean isLike;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

    @ApiModelProperty(value = "话题内容")
    private String topicContent;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long reportCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long reportId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updateTime;
}
