package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.vo.common.MajorQueryVo;
import com.hqk.recruitment.vo.common.MajorVo;

import java.util.List;

/**
 * <p>
 * 系别表 服务类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-27
 */
public interface MajorService extends IService<Major> {


    boolean addMajor(MajorVo majorVo);

    boolean deleteMajors(List<Long> ids);

    boolean updateMajor(MajorVo majorVo);

    List<MajorQueryVo> getAllMajorList(Long id);

    List<Major> getMajorNoTreeList();

    String getMajorNameById(Long id);
}
