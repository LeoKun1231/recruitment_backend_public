package com.hqk.recruitment.common.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hqk.recruitment.common.entity.Dict;
import com.hqk.recruitment.common.listener.MyExcelLisener;
import com.hqk.recruitment.common.mapper.DictMapper;
import com.hqk.recruitment.common.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-16
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Override
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("公司列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();

        List<Dict> dicts = baseMapper.selectList(queryWrapper);
        EasyExcel.write(response.getOutputStream(), Dict.class).sheet("公司列表").doWrite(dicts);
    }

    //批量上传
    @Override
    @CacheEvict(value = "abc",allEntries = true)
    public void upload(MultipartFile file) {
        try {
            System.out.println("file = " + file);
            EasyExcel.read(file.getInputStream(),Dict.class,new MyExcelLisener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    @Cacheable(value = "abc",keyGenerator = "keyGenerator")
//    public List<DictEeVo> getAllDictData() {
//        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("parent_id",0).or()
//                     .isNull("parent_id").orderByAsc("id");
//        // 得到一级节点资源列表
//        List<Dict> dicts = this.baseMapper.selectList(queryWrapper);
//        List<DictEeVo> dictEeVoArrayList = new ArrayList<>(dicts.size());
//        for (Dict dict : dicts) {
//            DictEeVo dictEeVo = new DictEeVo();
//            BeanUtils.copyProperties(dict,dictEeVo);
//            dictEeVoArrayList.add(dictEeVo);
//        }
//        if ( dictEeVoArrayList.size() > 0) {
//            dictEeVoArrayList.forEach(this::findAllChild);
//        }
//        return dictEeVoArrayList;
//    }
//
//    public void findAllChild(DictEeVo dict ) {
//        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("parent_id", dict.getId()).orderByAsc("id");
//        // 首次进入这个方法时，查出的是二级节点列表
//        // 递归调用时，就能依次查出三、四、五等等级别的节点列表，
//        List<Dict> list = this.baseMapper.selectList(queryWrapper);
//        List<DictEeVo> dictEeVoArrayList = new ArrayList<>(list.size());
//        for (Dict dict1 : list) {
//            DictEeVo dictEeVo = new DictEeVo();
//            BeanUtils.copyProperties(dict1,dictEeVo);
//            dictEeVoArrayList.add(dictEeVo);
//        }
//        dict.setChildren(dictEeVoArrayList);
//        if ( dictEeVoArrayList.size() > 0) {
//            dictEeVoArrayList.forEach(this::findAllChild);
//        }
//    }
}
