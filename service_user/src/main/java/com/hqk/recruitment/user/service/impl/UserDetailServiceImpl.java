package com.hqk.recruitment.user.service.impl;


import com.hqk.recruitment.user.config.entity.SecurityUser;
import com.hqk.recruitment.user.service.RoleService;
import com.hqk.recruitment.user.service.UserRoleService;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userService.getByAccount(username);
        System.out.println("user = " + user);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new SecurityUser(user.getId(),user.getAccount(),user.getPassword(),getUserAuthority(user.getId()));
    }

    /**
     * 获取用户角色信息 菜单、权限
     * @param id
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long id){
        //角色 菜单操作权限 authorityInfo用逗号隔开 ROLE_admin,ROLE_student,system:user:query
        String authorityInfo=roleService.getUserAuthorityInfo(id);

        //根据用户id查询所有相关角色
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorityInfo);
    }

}
