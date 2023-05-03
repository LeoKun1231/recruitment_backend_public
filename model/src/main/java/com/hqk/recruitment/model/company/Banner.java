package com.hqk.recruitment.model.company;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @since 2023-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Banner对象", description="")
public class Banner implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    private String creator;

    private String govUrl;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String imgUrl;
    private Long userId;

    @ApiModelProperty(value = "上线状态")
    @TableField("`status`")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @JsonIgnore
    private Boolean isDeleted;


}
