package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.user.Permission;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getAllPermission();

    List<Permission> getPermissionByListIds(List<Long> ids);


    Boolean deletePermission(Long id);
}
