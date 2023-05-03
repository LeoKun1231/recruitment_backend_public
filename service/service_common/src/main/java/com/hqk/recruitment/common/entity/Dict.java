package com.hqk.recruitment.common.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织架构表
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Dict implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @ExcelProperty(value = "id",index = 0)
    private Long id;

    /**
     * 上级id
     */
    @ExcelProperty(value = "上级id",index = 1)
    private Long parentId;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称",index = 2)
    private String name;

    /**
     * 值
     */
    @ExcelProperty(value = "值",index = 3)
    private Long value;

    /**
     * 编码
     */
    @ExcelProperty(value = "编码",index = 4)
    private String dictCode;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @ColumnWidth(40)
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    @ColumnWidth(40)
    private Date updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    @ExcelProperty("删除标记")
    @ColumnWidth(40)
    private Integer isDeleted;

    @TableField(exist = false)
    private List<Dict> children;
}
