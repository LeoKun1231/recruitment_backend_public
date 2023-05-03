package com.hqk.recruitment.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hqk.recruitment.model.user.User;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
public interface UserMapper extends BaseMapper<User> {

    int insertBatch(List<User> list);

}
