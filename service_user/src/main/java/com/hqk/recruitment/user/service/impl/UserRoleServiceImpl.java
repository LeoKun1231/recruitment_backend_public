package com.hqk.recruitment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.user.mapper.UserRoleMapper;
import com.hqk.recruitment.user.service.RoleService;
import com.hqk.recruitment.user.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;


    /**
     * 为用户添加角色
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public boolean AddUserRole(Long userId, Long roleId) {
        if(Objects.isNull(userId)){
            throw  new MyCustomException(20000,"用户不能为空");
        }
        if(Objects.isNull(roleId)){
            throw  new MyCustomException(20000,"角色不能为空");
        }
        this.remove(new QueryWrapper<UserRole>().eq("user_id",userId));
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        return this.save(userRole);
    }
}
