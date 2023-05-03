package com.hqk.recruitment.vo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

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
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Comment对象", description = "")
public class CommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "模块id")
    private Long articleId;

    @ApiModelProperty(value = "根评论id")
    private Long rootId;

    @ApiModelProperty(value = "父级评论id")
    private Long parentId;

    @ApiModelProperty(value = "回复对象")
    private Long targetId;
}
