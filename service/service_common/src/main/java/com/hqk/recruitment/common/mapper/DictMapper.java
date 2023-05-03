package com.hqk.recruitment.common.mapper;

import com.hqk.recruitment.common.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 组织架构表 Mapper 接口
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-16
 */
public interface DictMapper extends BaseMapper<Dict> {

     int insertBatch(List<Dict> list);
}
