package com.hqk.recruitment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.model.user.UserMajor;
import com.hqk.recruitment.user.mapper.UserMajorMapper;
import com.hqk.recruitment.user.service.MajorService;
import com.hqk.recruitment.user.service.UserMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class  UserMajorServiceImpl extends ServiceImpl<UserMajorMapper, UserMajor> implements UserMajorService{

    @Autowired
    private MajorService majorService;

    @Override
    public Map<String,List> getMajorIdsAndNamesByUserId(Long userId) {
        List<UserMajor> list = this.list(new QueryWrapper<UserMajor>().eq("user_id", userId));
        List<String> stringList = new ArrayList<>(list.size());
        for (UserMajor userMajor : list) {
            Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", userMajor.getMajorId()));
            if(!Objects.isNull(major)){
                stringList.add(major.getMajorName());
            }
        }
        Map<String, List> map = new HashMap<>();
        map.put("ids",list.stream().map(item->item.getMajorId()).collect(Collectors.toList()));
        map.put("names",stringList);
        return map;
    }

    @Override
    public Major getMajorByUserId(Long userId) {
        UserMajor userMajor = this.getOne(new QueryWrapper<UserMajor>().eq("user_id", userId));
        Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", userMajor.getMajorId()));
        Map<String, Object> map = new HashMap<>();
        return major;

    }

    @Override
    public boolean addUserMajors(Long userId, List<Long> ids) {
        boolean user_id = this.remove(new QueryWrapper<UserMajor>().eq("user_id", userId));
        List<UserMajor> userMajors = new ArrayList<>();
        for (Long id : ids) {
            UserMajor userMajor = new UserMajor();
            userMajor.setUserId(userId);
            userMajor.setMajorId(id);
            userMajors.add(userMajor);
        }
        return this.saveBatch(userMajors);
    }

    @Override
    public boolean addUserMajor(Long userId, Long majorId) {
        this.remove(new QueryWrapper<UserMajor>().eq("user_id", userId));
        UserMajor userMajor = new UserMajor();
        userMajor.setMajorId(majorId);
        userMajor.setUserId(userId);
        return this.save(userMajor);
    }
}
