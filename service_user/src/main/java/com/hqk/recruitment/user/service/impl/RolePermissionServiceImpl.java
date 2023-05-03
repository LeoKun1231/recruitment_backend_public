package com.hqk.recruitment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hqk.recruitment.model.user.Permission;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.user.mapper.RolePermissionMapper;
import com.hqk.recruitment.user.service.PermissionService;
import com.hqk.recruitment.user.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.user.RolePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Service
@Slf4j
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private PermissionService permissionService;

    @Override
    public RList getMenusByRoleId(Long id) {
        List<Long> list = this.list(new QueryWrapper<RolePermission>().eq("role_id", id)).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        log.warn(list+"list");
        if(list.size()>0){
            List<Permission> permission = permissionService.getPermissionByListIds(list);
            return RList.ok().data(permission).message(null);
        }
        return RList.ok().data(list).message(null);
    }
}
