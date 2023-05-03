package com.hqk.recruitment.client;

import com.hqk.recruitment.result.R;
import com.hqk.recruitment.vo.common.TopicUpdateVo;
import com.hqk.recruitment.vo.common.TopicVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "service-common",contextId = "common-topic")
@RequestMapping("/common/topic")
public interface CommonTopicClient {


    @PostMapping("/add")
    public R addTopic(TopicVo topicVo);

    @DeleteMapping("/delete/{id}")
    public  R deleteTopic(Long id);

    @PutMapping("/update")
    public  R updateTopic(@RequestBody TopicUpdateVo topicUpdateVo);


}
