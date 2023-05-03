package com.hqk.recruitment.vo.role;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("给角色添加（菜单）权限")
public class RolePermissionVo  implements Serializable{

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单列表")
    private List<Long> permissionsIds;
}
