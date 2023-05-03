package com.hqk.recruitment.vo.common;

import com.hqk.recruitment.model.base.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class CommentChildrenVo implements Serializable {
    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "用户名称")
    private String nickname;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "赞数")
    private Long likeCount;

    @ApiModelProperty(value = "是否已赞")
    private Boolean isLike;

    @ApiModelProperty(value = "模块id")
    private Long articleId;

    @ApiModelProperty(value = "父级评论id")
    private Long parentId;

    @ApiModelProperty(value = "回复对象")
    private String target;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value="子评论")
    private List<CommentLikeVo> children;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
