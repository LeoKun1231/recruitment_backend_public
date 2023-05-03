package com.hqk.recruitment.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hqk.recruitment.model.user.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getAllPermissionByUserId(Long Id);
}
