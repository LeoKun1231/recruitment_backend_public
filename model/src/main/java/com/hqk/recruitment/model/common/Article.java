package com.hqk.recruitment.model.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Article对象", description="")
public class Article implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "文章类型")
    private String type;

    @ApiModelProperty(value = "文章预览内容")
    private String contentPreview;

    @ApiModelProperty(value = "组织id")
    private Long majorId;

    @ApiModelProperty(value = "文章评论数")
    private Long commentCount;

    @ApiModelProperty(value = "文章浏览量")
    private Long watchCount;


    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "话题id")
    private Long topicId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
