package com.hqk.recruitment.user.service.impl;

import com.hqk.recruitment.user.mapper.PermissionMapper;
import com.hqk.recruitment.user.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hqk.recruitment.model.user.Permission;
import com.hqk.recruitment.utils.TreeUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-21
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<Permission> getAllPermission() {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        //1.按id排序查询所有菜单
        List<Permission> permissions = baseMapper.selectList(queryWrapper);
        //2.得到结果
        List<Permission> tree = TreeUtils.generateTrees(permissions);

//        List<Permission> resultList =findTopChildren(permissions);
        return tree;
    }

    @Override
    public List<Permission> getPermissionByListIds(List<Long> ids) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id").in("id", ids);
        //1.按id排序查询所有菜单
        List<Permission> permissions = baseMapper.selectList(queryWrapper);
        //2.得到结果
        List<Permission> tree = TreeUtils.generateTrees(permissions);
        return tree;
    }

    @Override
    public Boolean deletePermission(Long id) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);
        List<Long> list = new ArrayList<>();
        List<Long> idList = selectAllChildren(id, list, permissionList);
        idList.add(id);
//        int i = baseMapper.deleteBatchIds(idList);
        return true;
    }


    //把返回所有菜单list集合进行封装的方法
    public static List<Permission> findTopChildren(List<Permission> permissionList) {
//        //创建list集合，用于数据最终封装
//        List<Permission> finalNode = new ArrayList<>();
//        //把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
//        for(Permission permissionNode : permissionList) {
//            //得到顶层菜单 pid=0菜单
//            if(0==permissionNode.getParentId()) {
//                //设置顶层菜单的level是1
//                permissionNode.setLevel(1);
//                //根据顶层菜单，向里面进行查询子菜单，封装到finalNode里面
//                finalNode.add(selectChildren(permissionNode,permissionList));
//            }
//        }
//        return finalNode;
        return null;
    }

    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        //1 因为向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
        permissionNode.setChildren(new ArrayList<Permission>());

        //2 遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
        for (Permission it : permissionList) {
            //判断 id和pid值是否相同
            if (permissionNode.getId().equals(it.getParentId())) {
                //把父菜单的level值+1
                int level = permissionNode.getLevel() + 1;
                it.setLevel(level);
                //如果children为空，进行初始化操作
                if (permissionNode.getChildren() == null) {
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                //把查询出来的子菜单放到父菜单里面
                permissionNode.getChildren().add(selectChildren(it, permissionList));
            }
        }
        return permissionNode;
    }


    private List<Long> selectAllChildren(Long id, List<Long> list, List<Permission> permissionList) {
        permissionList.stream().forEach(item -> {
            if (id == item.getParentId()) {
                list.add(item.getId());
                selectAllChildren(item.getId(), list, permissionList);
            }
        });
        return list;
    }
}
