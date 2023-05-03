package com.hqk.recruitment.user.controller.common;

import com.hqk.recruitment.client.CommonTopicClient;
import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.TopicUpdateVo;
import com.hqk.recruitment.vo.common.TopicVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/acl/common/topic")
public class CommonTopicController {

    @Resource
    private CommonTopicClient commonTopicClient;

    @PostMapping("/add")
    public R addTopic(@RequestBody TopicVo topicVo){
        return commonTopicClient.addTopic(topicVo);
    };

    @DeleteMapping("/delete/{id}")
    public R deleteMajor(@PathVariable Long id){
        return commonTopicClient.deleteTopic(id);
    };


    @PutMapping("/update")
    public R updateMajor(@RequestBody TopicUpdateVo topicUpdateVo){
        return commonTopicClient.updateTopic(topicUpdateVo);
    };

}
