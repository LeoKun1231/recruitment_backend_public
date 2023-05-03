package com.hqk.recruitment.vo.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hqk.recruitment.model.base.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class MajorQueryVo implements Serializable, TreeNode<Long> {

    @ApiModelProperty(value = "id")
    private Long value;

    @ApiModelProperty(value = "父id")
    @JsonIgnore
    private Long parentId;

    @ApiModelProperty(value = "系名或专业名")
    private String title;
    @ApiModelProperty(value = "下级")
    @TableField(exist = false)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<MajorQueryVo> children;

    @Override
    public Long id() {
        return this.value;
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
