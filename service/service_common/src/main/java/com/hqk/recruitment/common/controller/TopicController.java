package com.hqk.recruitment.common.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hqk.recruitment.model.common.Topic;
import com.hqk.recruitment.common.service.TopicService;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.base.PageVo;
import com.hqk.recruitment.vo.common.TopicUpdateVo;
import com.hqk.recruitment.vo.common.TopicVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hong QinKun
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/common/topic")
public class TopicController {

    @Resource
    private TopicService topicService;

    @PostMapping("/list")
    public R getTopicList(@RequestBody PageVo pageVo){
        return topicService.listByPage(pageVo);
    }

    @GetMapping("/randomList")
    public R getRandomTopicList(){
        return topicService.getRandomList();
    }

    @PostMapping("/add")
    public  R addTopic(@RequestBody Map map){
        String content = (String) map.get("content");
        return topicService.addTopic(content);
    }


    @DeleteMapping("/delete/{id}")
    public  R deleteTopic(@PathVariable("id") Long id){
        return topicService.deleteTopById(id);
    }


    @GetMapping("/detail/{id}")
    public R getTopicDetail(@PathVariable Long id){
        return topicService.getTopicDetailById(id);
    }

    @PutMapping("/update")
    public  R updateTopic(@RequestBody TopicUpdateVo topicUpdateVo){
        boolean isUpdate=  topicService.updateTopic(topicUpdateVo);
        if(isUpdate){
            return R.ok().message("修改话题成功");
        }else{
            return R.error().code(20000).message("修改话题失败");
        }
    }

}

