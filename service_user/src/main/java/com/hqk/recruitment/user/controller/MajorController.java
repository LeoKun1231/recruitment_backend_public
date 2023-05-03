package com.hqk.recruitment.user.controller;


import com.hqk.recruitment.model.common.Major;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.result.RList;
import com.hqk.recruitment.user.service.MajorService;
import com.hqk.recruitment.vo.common.MajorQueryVo;
import com.hqk.recruitment.vo.common.MajorVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 系别表 前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-02-27
 */
@RestController
@RequestMapping("/acl/major")
public class MajorController {

    @Resource
    private MajorService majorService;

    @GetMapping("/list/{id}")
    public RList getMajorList(@PathVariable Long id){
        List<MajorQueryVo> allList= majorService.getAllMajorList(id);
        return RList.ok().data(allList).message(null);
    }

    @GetMapping("/list")
    public RList getAllMajorList(){
        List<MajorQueryVo> allList= majorService.getAllMajorList(-1L);
        return RList.ok().data(allList).message(null);
    }


    @PostMapping("/add")
    public R addMajor(@RequestBody MajorVo majorVo){
        boolean isAdd= majorService.addMajor(majorVo);
        if(isAdd){
            return R.ok().message("添加专业成功");
        }else{
            return R.error().code(20000).message("添加专业失败");
        }
    }

//
    @DeleteMapping("/batchDelete")
    public R deleteMajors(@RequestBody List<Long> ids){

        boolean b = majorService.deleteMajors(ids);
        if(b){
            return R.ok().message("删除成功");
        }else{
            return R.error().code(20000).message("删除失败");
        }
    }

    @PutMapping("/update")
    public R updateMajor(@RequestBody MajorVo majorVo){
        boolean isUpdate= majorService.updateMajor(majorVo);
        if(isUpdate){
            return R.ok().message("修改成功");
        }else{
            return R.error().code(20000).message("修改失败");
        }
    };

    @GetMapping("/noTreeList")
    public R getMajorList(){
        List<Major> allList= majorService.getMajorNoTreeList();
        return R.ok().message(null).data("list",allList);
    }


    @GetMapping("/{id}")
    public String getMajorNameById(@PathVariable("id") Long id){
        return majorService.getMajorNameById(id);
    }

}

