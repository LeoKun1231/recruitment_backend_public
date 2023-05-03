package com.hqk.recruitment.user.controller;


import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.user.service.RolePermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 角色权限 前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/acl/menus")
public class RolePermissionController {

    @Resource
    private RolePermissionService rolePermissionService;

    @GetMapping("/{id}")
    public RList getMenusByRoleId(@PathVariable Long id){
      return rolePermissionService.getMenusByRoleId(id);
    }
}

