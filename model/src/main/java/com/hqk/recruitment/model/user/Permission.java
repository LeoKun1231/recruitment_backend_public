package com.hqk.recruitment.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hqk.recruitment.model.base.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value="Permission对象", description="权限")
public class Permission implements Serializable, TreeNode<Long> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "所属上级")
    private Long parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "权限值")
    private String permission;

    @ApiModelProperty(value = "访问路径")
    private String path;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @JsonIgnore
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @JsonIgnore
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonIgnore
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "层级")
    @TableField(exist = false)
    @JsonIgnore
    private Integer level;

    @ApiModelProperty(value = "下级")
    @TableField(exist = false)
    private List<Permission> children;

    @ApiModelProperty(value = "是否选中")
    @TableField(exist = false)
    @JsonIgnore
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
        return Objects.equals(this.parentId, 0L);
    }


    @Override
    public void setChildren(List children) {
        this.children=children;
    }
}
