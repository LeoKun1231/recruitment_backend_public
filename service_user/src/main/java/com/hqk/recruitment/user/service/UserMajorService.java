package com.hqk.recruitment.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.model.user.UserMajor;

import java.util.List;
import java.util.Map;

public interface UserMajorService extends IService<UserMajor> {

     Map<String,List> getMajorIdsAndNamesByUserId(Long userId);

     Major getMajorByUserId(Long userId);

     boolean addUserMajors(Long userId,List<Long> ids);

     boolean addUserMajor(Long userId,Long majorId);

}
