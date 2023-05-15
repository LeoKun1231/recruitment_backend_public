package com.hqk.recruitment.model.common;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Report对象", description="")
public class Report implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文章id")
    private Long articleId;

    @ApiModelProperty(value = "评论id")
    private Long commentId;

    @ApiModelProperty(value = "评论次数")
    private Long reportCount;

    @ApiModelProperty(value = "原因")
    private String reason;

    private Date updateTime;
}
