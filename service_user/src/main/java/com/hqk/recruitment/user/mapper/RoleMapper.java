package com.hqk.recruitment.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hqk.recruitment.model.user.Role;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    //获取用户所有角色
    @Select("select * from sys_role r,sys_user_role ur where r.id=ur.role_id and user_id=#{id}")
    List<Role> getAllUserRoles(Long id);

}
