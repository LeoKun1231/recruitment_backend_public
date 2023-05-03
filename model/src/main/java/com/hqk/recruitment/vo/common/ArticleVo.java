package com.hqk.recruitment.vo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Article对象", description="")
public class ArticleVo  implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "文章类型")
    private Long typeId;

    @ApiModelProperty(value = "文章预览内容")
    private String contentPreview;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

}
