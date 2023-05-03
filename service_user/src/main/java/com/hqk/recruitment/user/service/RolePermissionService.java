package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.user.RolePermission;
import com.hqk.recruitment.result.RList;

/**
 * <p>
 * 角色权限 服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface RolePermissionService extends IService<RolePermission> {

    RList getMenusByRoleId(Long id);
}
