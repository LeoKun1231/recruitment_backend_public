package com.hqk.recruitment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hqk.recruitment.model.user.UserRole;
import com.hqk.recruitment.user.mapper.PermissionMapper;
import com.hqk.recruitment.user.mapper.RoleMapper;
import com.hqk.recruitment.user.service.RolePermissionService;
import com.hqk.recruitment.user.service.RoleService;
import com.hqk.recruitment.user.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.user.Permission;
import com.hqk.recruitment.model.user.Role;
import com.hqk.recruitment.model.user.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private  RoleMapper  roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserRoleService userRoleService;



    @Override
    public boolean addRole2Permission(Long roleId, List<Long> permissionIds) {
        //1.删除原有的
        UpdateWrapper<RolePermission> roleUpdateWrapper = new UpdateWrapper<>();
        roleUpdateWrapper.eq("role_id",roleId);
        rolePermissionService.remove(roleUpdateWrapper);
        //2.添加
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        boolean b = rolePermissionService.saveBatch(rolePermissions);
        return b;
    }

    /**
     * 获取用户所有角色信息
     * @param id
     * @return
     */
    @Override
    public List<Role> getUserAllRoleByUserId(Long id) {
        List<Role> allUserRoles = roleMapper.getAllUserRoles(id);
        return allUserRoles;
    }



    //获取用户角色以及权限
    @Override
    @Cacheable(value = "authorityInfo：",keyGenerator = "keyGenerator")
    public String getUserAuthorityInfo(Long id) {

        String authorityInfo="";
        //1。获取角色名
        List<Role> userRoles = roleMapper.getAllUserRoles(id);
        if(userRoles.size()>0){
            //获取所有角色名，并用逗号隔开
            String roleNames = userRoles.stream().map(item ->"ROLE_"+item.getRoleName()).collect(Collectors.joining(","));
            authorityInfo=roleNames;
        }

        //2.获取用户权限
        List<Permission> permissions = permissionMapper.getAllPermissionByUserId(id);

        if(permissions.size()>0){
            String permissionNames = permissions.stream().map(item -> item.getPermission()).collect(Collectors.joining(","));
            if(authorityInfo.equals("")){
                authorityInfo=permissionNames;
            }else{
                authorityInfo=authorityInfo.concat(",")+permissionNames;
            }
        }

        return authorityInfo;
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        UserRole role = userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id",userId));
        return this.getOne(new QueryWrapper<Role>().eq("id", role.getRoleId()));
    }
}
