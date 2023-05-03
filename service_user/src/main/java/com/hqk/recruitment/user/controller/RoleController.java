package com.hqk.recruitment.user.controller;


import com.hqk.recruitment.model.user.Role;
import com.hqk.recruitment.user.service.RoleService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.role.RolePermissionVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/acl/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping()
    public R addRolePermission(@RequestBody RolePermissionVo rolePermissionVo){
        System.out.println("rolePermissionVo = " + rolePermissionVo);
        boolean b=roleService.addRole2Permission(rolePermissionVo.getRoleId(),rolePermissionVo.getPermissionsIds());
        if(b){
            return R.ok().message("添加成功");
        }else{
            return R.error().message("添加失败");
        }
    }

    @GetMapping("/getRole/{id}")
    public Role getRoleByUserId(Long userId){
       return roleService.getRoleByUserId(userId);
    }
}

