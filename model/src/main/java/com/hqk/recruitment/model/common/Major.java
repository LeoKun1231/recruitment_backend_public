package com.hqk.recruitment.model.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hqk.recruitment.model.base.TreeNode;
import com.hqk.recruitment.model.user.Permission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系别表
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Major对象", description="系别表")
@TableName(value = "sys_major")
public class Major implements Serializable, TreeNode<Long> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "系名或专业名")
    private String majorName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @JsonIgnore
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "下级")
    @TableField(exist = false)
    private List<Major> children;

    @ApiModelProperty(value = "是否选中")
    @TableField(exist = false)
    private boolean isSelect;

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public Long parentId() {
        return this.parentId;
    }

    @Override
    public boolean root() {
        return Objects.equals(this.parentId,0L);
    }

    @Override
    public void setChildren(List children) {
        this.children=children;
    }

}
