package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.user.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface RoleService extends IService<Role> {

    boolean addRole2Permission(Long roleId, List<Long> permissionIds);

    List<Role> getUserAllRoleByUserId(Long id);

    String getUserAuthorityInfo(Long id);

    Role getRoleByUserId(Long userId);

}
