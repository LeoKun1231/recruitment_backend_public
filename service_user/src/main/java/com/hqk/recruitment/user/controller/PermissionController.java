package com.hqk.recruitment.user.controller;


import com.hqk.recruitment.user.service.PermissionService;
import com.hqk.recruitment.model.user.Permission;
import com.hqk.recruitment.result.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hong QinKun
 * @since 2023-02-21
 */
@RestController
@RequestMapping("/acl/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('admin')")
    public R getAllPermission(){
        List<Permission> list = permissionService.getAllPermission();
        return R.ok().data("list",list);
    }

    //
    @DeleteMapping("/{id}")
    public R deletePermisson(@PathVariable("id")Long id){
        Boolean isDelete= permissionService.deletePermission(id);
        if(isDelete){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }

}

