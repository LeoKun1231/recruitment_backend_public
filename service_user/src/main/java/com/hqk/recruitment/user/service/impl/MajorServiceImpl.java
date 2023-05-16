package com.hqk.recruitment.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.client.CommonArticleClient;
import com.hqk.recruitment.constants.RoleCode;
import com.hqk.recruitment.exception.MyCustomException;
import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.model.user.Role;
import com.hqk.recruitment.model.user.UserMajor;
import com.hqk.recruitment.user.mapper.MajorMapper;
import com.hqk.recruitment.user.service.MajorService;
import com.hqk.recruitment.user.service.RoleService;
import com.hqk.recruitment.user.service.UserMajorService;
import com.hqk.recruitment.user.service.UserService;
import com.hqk.recruitment.utils.TreeUtils;
import com.hqk.recruitment.vo.common.MajorQueryVo;
import com.hqk.recruitment.vo.common.MajorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系别表 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-27
 */
@Service
@Slf4j
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {

    @Autowired
    private UserMajorService userMajorService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MajorService majorService;

    @Resource
    private CommonArticleClient commonArticleClient;

    @Resource
    private UserService userService;


    @Override
    public boolean addMajor(MajorVo majorVo) {
        if(Objects.isNull(majorVo.getMajorName())){
            System.out.println("majorVo = " + majorVo);
            throw new MyCustomException(20000,"系名或专业名不能为空");
        }
        if(Objects.isNull(majorVo.getParentId())){
            majorVo.setParentId(1L);
        }
        Major major = new Major();
        BeanUtils.copyProperties(majorVo,major);
        boolean save = this.save(major);
        //如果添加成功，为添加所有的用户补上这个，
        if(save){
            Long parentId = majorVo.getParentId();
            if(parentId!=1L){
                List<Long> childrenIds = majorService.list(new QueryWrapper<Major>().eq("parent_id", parentId)).stream().map(Major::getId).collect(Collectors.toList());
                List<Long> userIds = userMajorService.list(new QueryWrapper<UserMajor>().select("distinct user_id").in("major_id", childrenIds)).stream().map(UserMajor::getUserId).collect(Collectors.toList());
                for (Long userId : userIds) {
                    Map<String, List> majorIdsAndNamesByUserId = userMajorService.getMajorIdsAndNamesByUserId(userId);
                    List<Long> ids = majorIdsAndNamesByUserId.get("ids");
                    List<Long> list = childrenIds.stream().filter(item -> item != major.getId()).collect(Collectors.toList());
                    if(ids.containsAll(list)){
                        UserMajor userMajor = new UserMajor();
                        userMajor.setUserId(userId);
                        userMajor.setMajorId(major.getId());
                        userMajorService.save(userMajor);
                    }
                }
                return true;
            }else{
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean deleteMajors(List<Long> ids) {


        if(ids.size()==0){
            throw new MyCustomException(20000,"请选择系或专业进行删除");
        }
        //先删除一批
        boolean isDelete=this.removeByIds(ids);
        //如果删除成功，则删除对应用户的负责的id
        userMajorService.remove(new QueryWrapper<UserMajor>().in("major_id",ids));

        List<Long> list = new ArrayList<>();
        //删除父节点所有子节点
        for (Long id : ids) {
            List<Long> parent_id = this.list(new QueryWrapper<Major>().eq("parent_id", id)).stream().map(Major::getId).collect(Collectors.toList());
            list.addAll(parent_id);
        }
        if(list.size()>0){
            boolean b = this.removeByIds(list);
            if(b){
                userMajorService.remove(new QueryWrapper<UserMajor>().in("major_id",list));
            }
            return b;
        }else{
            return isDelete;
        }
    }

    @Override
    public boolean updateMajor(MajorVo majorVo) {

        Major major = new Major();
        BeanUtils.copyProperties(majorVo,major);
        return this.updateById(major);
    }

    @Override
    public List<MajorQueryVo> getAllMajorList(Long id) {

        Role role =null;
        List<Major> list =null;
        if(id!=-1L){
            role = roleService.getRoleByUserId(id);
        }

        //如果是admin或者id==-1则查询所有
        if( id==-1L ||role.getId()==RoleCode.ADMIN ){
            list=this.list();
        }else{
            //如果是其他，则查询自己相关的专业
            Map<String, List> map = userMajorService.getMajorIdsAndNamesByUserId(id);
            List ids = map.get("ids");
            //获取二级Id
            List<Long> parentIdList = this.list(new QueryWrapper<Major>().eq("parent_id", 1)).stream().map(item -> item.getId()).collect(Collectors.toList());
            ids.add(1);
            ids.addAll(parentIdList);
            list= this.list(new QueryWrapper<Major>().in("id",ids));
        }

        List<MajorQueryVo> list1 = new ArrayList<>();
        for (Major major : list) {
            MajorQueryVo majorQueryVo = new MajorQueryVo();
            majorQueryVo.setParentId(major.getParentId());
            majorQueryVo.setTitle(major.getMajorName());
            majorQueryVo.setValue(major.getId());
            if(major.getParentId()==1L){
                majorQueryVo.setChildren(new ArrayList(0));
            }
            list1.add(majorQueryVo);
        }
        List<MajorQueryVo> majors = TreeUtils.generateTrees(list1);
        if(id!=-1L){
            if(majors.size()>0){
                List<MajorQueryVo> children = majors.get(0).getChildren();
                List<MajorQueryVo> collect = children.stream().filter(item ->!Objects.isNull(item.getChildren()) && item.getChildren().size() > 0).collect(Collectors.toList());
                return collect;
            }else{
                return null;
            }
        }else{
            return  majors.get(0).getChildren();
        }
    }


    @Override
    public List<Major> getMajorNoTreeList() {
        List<Long> majorIds = commonArticleClient.getMajorIds();
        if(majorIds.size()==0){
            return new ArrayList<Major>();
        }

        List<Major> id = this.list(new QueryWrapper<Major>().in("id", majorIds.stream().filter(item->item!=1).collect(Collectors.toList())));
        return id;
    }

    @Override
    public String getMajorNameById(Long id) {
        Major one = this.getOne(Wrappers.<Major>lambdaQuery().eq(Major::getId, id));
        if(!Objects.isNull(one)){
            return one.getMajorName();
        }
        return "";
    }

}
