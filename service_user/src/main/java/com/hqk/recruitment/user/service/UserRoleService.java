package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.user.UserRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface UserRoleService extends IService<UserRole> {

    boolean AddUserRole(Long userId,Long RoleId);
}
